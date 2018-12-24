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

    public NotebookEntry(SlackEntry[] context, int entry, Map<String, SlackUser> users) {
        author = users.get(context[entry].user);
        compileEntries(context, entry);
        this.docDay = slackEntries.get(0).extractDocDate();
        formatSlackEntries();
    }

    void formatSlackEntries() {
        if (slackEntries.size() == 0)
            return;
        formattedSlackEntries = slackEntries.get(0).text;
        for (int i = 1; i < slackEntries.size(); i++)
            formattedSlackEntries += " -message break- " + slackEntries.get(i).text;
    }

    void compileEntries(SlackEntry[] context, int entry) {
        slackEntries = new ArrayList<>();
        slackFiles = new HashMap<>();

        for (int i = entry; i < context.length
                && (i == entry || context[i].extractDocDate() == null || context[i].user != this.author.id);
                i++) {
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
}
