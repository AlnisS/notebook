package org.alniss.notebook.notebookdata;

import org.alniss.notebook.slackdata.SlackEntry;
import org.alniss.notebook.slackdata.SlackUser;
import org.alniss.notebook.superentry.SuperEntryManager;

import java.util.*;

public class NotebookDataManager {
    /** The start date of the season */
    public static final Date seasonStart = new GregorianCalendar(2018, Calendar.SEPTEMBER, 3).getTime();

    /** Map from Slack String user ID to the appropriate SlackUser */
    public Map<String, SlackUser> userMap;
    /** Array with NotebookDay for each day with documentation */
    public NotebookDay[] notebookDays;

    public NotebookDataManager(Map<String, SlackUser> userMap) {
        this.userMap = userMap;
    }

    /**
     * Creates days of documentation by intaking the edited entries. It gets
     * all of the entries as SlackEntries, creates NotebookEntries using those
     * SlackEntries, compiles those NotebookEntries into days, sorts their
     * entries, and adds appropriate titles.
     * @param superEntryManager entries to use.
     */
    public void prepareNotebookData(SuperEntryManager superEntryManager) {
        SlackEntry[] slackEntries = superEntryManager.getAllAsSlackEntries();
        NotebookEntry[] notebookEntries = createNotebookEntries(slackEntries, userMap);
        notebookDays = generateNotebookDays(notebookEntries);
        sortDayEntries(notebookDays);
        setupTitles(notebookDays);
    }

    /**
     * Sets up the title in each NotebookDay in notebookDays.
     * @param notebookDays days to add title to.
     */
    public static void setupTitles(NotebookDay[] notebookDays) {
        for (NotebookDay day : notebookDays)
            day.setupTitle();
    }

    /**
     * Sorts the entries within each NotebookDay.
     * @param notebookDays days in which to sort entries.
     */
    public static void sortDayEntries(NotebookDay[] notebookDays) {
        for (NotebookDay day : notebookDays)
            day.sortEntries();
    }

    /**
     * Assembles NotebookDays from NotebookEntries.
     * @param notebookEntries NotebookEntries to assemble into days.
     * @return assembled array of NotebookDays.
     */
    public static NotebookDay[] generateNotebookDays(NotebookEntry[] notebookEntries) {

        // maps date (e.g. "Tue Oct 09") to that day
        Map<String, NotebookDay> arrangedNotebookDays = new HashMap<>();

        for (NotebookEntry notebookEntry : notebookEntries) {

            // id is the date (e.g. "Tue Oct 09")
            String id = notebookEntry.docDay.toString().substring(0, 10);

            // if there isn't that day yet, make a new day
            if (arrangedNotebookDays.get(id) == null)
                arrangedNotebookDays.put(id, new NotebookDay());

            NotebookDay entryDay = arrangedNotebookDays.get(id);

            // adds the entry to the day and sets its date and meeting times
            entryDay.notebookEntries.add(notebookEntry);
            entryDay.date = notebookEntry.docDay;
            entryDay.initTimesIfApplicable();
        }

        // returns the NotebookDays as array
        return arrangedNotebookDays.values().toArray(new NotebookDay[arrangedNotebookDays.size()]);
    }

    /**
     * Creates NotebookEntries using messages sent (SlackEntries). Requires a
     * map from the Slack user ID to the SlackUser.
     * @param context SlackEntry representation of Slack messages.
     * @param users map holding Slack user ID -> SlackUser object.
     * @return assembled NotebookEntries.
     */
    public static NotebookEntry[] createNotebookEntries(SlackEntry[] context, Map<String, SlackUser> users) {
        List<NotebookEntry> notebookEntries = new ArrayList<>();
        for (int i = 0; i < context.length; i++) {
            // if the entry is a documentation start (has a doc date), add it
            if (context[i].extractDocDate() != null)
                notebookEntries.add(new NotebookEntry(context, i, users));
        }
        return notebookEntries.toArray(new NotebookEntry[notebookEntries.size()]);
    }

    /**
     * Prints the entries in each NotebookDay.
     */
    public void printEntries() {
        for (NotebookDay notebookDay : notebookDays)
            notebookDay.printEntries();
    }
}
