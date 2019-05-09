package org.alniss.notebook.notebookdata;

import org.alniss.notebook.slackdata.SlackEntry;
import org.alniss.notebook.slackdata.SlackFile;
import org.alniss.notebook.slackdata.SlackUser;

import java.util.*;

/**
 * Represents a single notebook entry written by one person.
 */
public class NotebookEntry {
    /**
     * The Slack entries used to build the notebook entry.
     */
    public List<SlackEntry> slackEntries;
    /**
     * Files held by the entry (doesn't do anything yet). It is intended to map
     * between a fig. n and the appropriate file n.
     */
    public Map<Integer, SlackFile> slackFiles;
    /**
     * Author of this entry.
     */
    public SlackUser author;
    /**
     * Day about which the entry is written.
     */
    public Date docDay;
    /**
     * The compiled text of the entries without tags etc.
     */
    public String formattedSlackEntries;
    /**
     * String which starts entries.
     */
    public static final String ENTRY_START_STRING = "";
    /**
     * String which separates Slack messages when compiled to the entry.
     */
    public static final String MESSAGE_SEPARATOR_STRING = " ";

    /**
     * Creates a NotebookEntry using data from Slack.
     * @param context array of all Slack entries used in this generation session.
     * @param entry index of start Slack message for this NotebookEntry.
     * @param users Map from internal Slack user id to a SlackUser.
     */
    public NotebookEntry(SlackEntry[] context, int entry, Map<String, SlackUser> users) {
        author = users.get(context[entry].user);
        compileEntries(context, entry);
        this.docDay = slackEntries.get(0).extractDocDate();
        formatSlackEntries();
    }

    /**
     * Compiles the Slack message entries into the full notebook entry.
     * @param context array of all Slack entries used in this generation session.
     * @param entry index of start Slack message for this NotebookEntry.
     */
    private void compileEntries(SlackEntry[] context, int entry) {
        slackEntries = new ArrayList<>();
        slackFiles = new HashMap<>();

        for (int i = entry; i < context.length  // continue iff:
                && (i == entry            // not the beginning entry
                || !context[i].isStart()  // or not a starting entry
                || (context[i].isStart()  // or starting entry, but other user
                && !context[i].user.equals(this.author.id))); i++) {
            SlackEntry currentEntry = context[i];

            // if this entry's user sent that message
            if (currentEntry.user.equals(this.author.id)) {
                addData(currentEntry);
            }
        }
    }

    private void formatSlackEntries() {
        if (slackEntries.size() == 0)
            return;
        formattedSlackEntries = ENTRY_START_STRING
                + slackEntries.get(0).taggedString.getProcessedString();
        for (int i = 1; i < slackEntries.size(); i++)
            formattedSlackEntries += MESSAGE_SEPARATOR_STRING
                    + slackEntries.get(i).taggedString.getProcessedString();
    }

    private void addData(SlackEntry currentEntry) {
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
