package org.alniss.notebook.notebookdata;

import org.alniss.notebook.slackdata.SlackEntry;
import org.alniss.notebook.slackdata.SlackUser;
import org.alniss.notebook.superentry.SuperEntryManager;

import java.util.*;

public class NotebookDataManager {
    public static final Date seasonStart = new GregorianCalendar(2018, Calendar.SEPTEMBER, 3).getTime();

    public Map<String, SlackUser> userMap;
    public NotebookDay[] notebookDays;

    public NotebookDataManager(Map<String, SlackUser> userMap) {
        this.userMap = userMap;
    }

    public void prepareNotebookData(SuperEntryManager superEntryManager) {
        SlackEntry[] slackEntries = superEntryManager.getAllAsSlackEntries();
        NotebookEntry[] notebookEntries = createNotebookEntries(slackEntries, userMap);
        notebookDays = generateNotebookDays(notebookEntries);
        sortDayEntries(notebookDays);
        setupTitles(notebookDays);
    }

    public static void setupTitles(NotebookDay[] notebookDays) {
        for (NotebookDay day : notebookDays)
            day.setupTitle();
    }

    public static void sortDayEntries(NotebookDay[] notebookDays) {
        for (NotebookDay day : notebookDays)
            day.sortEntries();
    }

    public static NotebookDay[] generateNotebookDays(NotebookEntry[] notebookEntries) {
        //TODO: make this less janky
        Map<String, NotebookDay> arrangedNotebookDays = new HashMap<>();
        for (NotebookEntry notebookEntry : notebookEntries) {
            String id = notebookEntry.docDay.toString().substring(0, 10);
            if (arrangedNotebookDays.get(id) == null)
                arrangedNotebookDays.put(id, new NotebookDay());
            arrangedNotebookDays.get(id).notebookEntries.add(notebookEntry);
            arrangedNotebookDays.get(id).date = notebookEntry.docDay;
            arrangedNotebookDays.get(id).initTimesIfApplicable();
        }
        return arrangedNotebookDays.values().toArray(new NotebookDay[arrangedNotebookDays.size()]);
    }

    public static NotebookEntry[] createNotebookEntries(SlackEntry[] context, Map<String, SlackUser> users) {
        List<NotebookEntry> notebookEntries = new ArrayList<>();
        for (int i = 0; i < context.length; i++) {
            if (context[i].extractDocDate() != null)
                notebookEntries.add(new NotebookEntry(context, i, users));
        }
        return notebookEntries.toArray(new NotebookEntry[notebookEntries.size()]);
    }

    public void printEntries() {
        for (NotebookDay notebookDay : notebookDays)
            notebookDay.printEntries();
    }
}
