package org.alniss.notebook.notebookdata;

import org.alniss.notebook.slackdata.SlackEntry;
import org.alniss.notebook.slackdata.SlackFile;
import org.alniss.notebook.slackdata.SlackUser;

import java.util.*;

public class NotebookEntry {
    public List<SlackEntry> slackEntries;
    public Map<Integer, SlackFile> slackFiles;
    public SlackUser author;
    public Date docDay;
    public String formattedSlackEntries;
    public static final String ENTRY_START_STRING = "\t";
    public static final String MESSAGE_SEPARATOR_STRING = "\n\t";

    public NotebookEntry(SlackEntry[] context, int entry, Map<String, SlackUser> users) {
        author = users.get(context[entry].user);
        compileEntries(context, entry);
        this.docDay = slackEntries.get(0).extractDocDate();
        formatSlackEntries();
    }

    void formatSlackEntries() {
        if (slackEntries.size() == 0)
            return;
        formattedSlackEntries = ENTRY_START_STRING + slackEntries.get(0).taggedString.getProcessedString();
        for (int i = 1; i < slackEntries.size(); i++)
            formattedSlackEntries += MESSAGE_SEPARATOR_STRING + slackEntries.get(i).taggedString.getProcessedString();
    }

    void compileEntries(SlackEntry[] context, int entry) {
        slackEntries = new ArrayList<>();
        slackFiles = new HashMap<>();

        for (int i = entry; i < context.length
                && (i == entry
                || !context[i].isStart()
                || (context[i].isStart() && !context[i].user.equals(this.author.id))); i++) {
            SlackEntry currentEntry = context[i];

            if (currentEntry.user.equals(this.author.id))
                addData(currentEntry);
        }
    }

    void addData(SlackEntry currentEntry) {
        if (currentEntry.isDocumentation())
            slackEntries.add(currentEntry);

        if (currentEntry.files != null) {
            switch (currentEntry.files.length) {
                case 0:
                    break;
                case 1:
                    if (currentEntry.text.substring(0, 5).equals("[fig "))
                        slackFiles.put(Integer.parseInt(currentEntry.text.substring(5,
                                currentEntry.text.indexOf("]"))), currentEntry.files[0]);
                    break;
                default:
                    System.out.println("multiple files in one message: " + currentEntry.text);
            }
        }
    }

    public List<Boolean> hasTag(String tag) {
        List<Boolean> result = new ArrayList<>();
        for (SlackEntry entry : slackEntries)  //TODO: check whether this is safe
            result.add(entry.taggedString.hasTag(tag));
        return result;
    }

    public List<Object> getTagValue(String tag) {
        List<Object> result = new ArrayList<>();
        for (SlackEntry entry : slackEntries)
            result.add(entry.taggedString.getTagValue(tag));
        return result;
    }

    public boolean anyHasTag(String tag) {
        List<Boolean> hasTag = hasTag(tag);
        for (Boolean b : hasTag)
            if (b)
                return true;
        return false;
    }

    public Object anyGetTagValue(String tag) {
        List<Object> tagValue = getTagValue(tag);
        for (Object o : tagValue)
            if (o != null)
                return o;
        return null;
    }
}
