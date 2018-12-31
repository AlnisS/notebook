package org.alniss.notebook.notebookdata;

import com.google.gson.Gson;
import org.alniss.notebook.slackdata.SlackEntry;
import org.alniss.notebook.slackdata.SlackUser;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.*;

public class NotebookDataManager {
    public static File messageFile = new File(System.getProperty("user.dir") + "\\data\\testdata.json");
    public static File userFile = new File(System.getProperty("user.dir") + "\\data\\users.json");
    public static File latexFile = new File(System.getProperty("user.dir") + "\\data\\latex\\notebook.tex");
    public static File latexPDFFile = new File(System.getProperty("user.dir") + "\\data\\latex\\notebook.pdf");

    public SlackEntry[] slackEntries;
    public SlackUser[] users;
    public Map<String, SlackUser> userMap;

    public NotebookEntry[] notebookEntries;
    public NotebookDay[] notebookDays;
    public static final Date seasonStart = new GregorianCalendar(2018, Calendar.SEPTEMBER, 3).getTime();

    public NotebookDataManager() {
        users = loadUsers(userFile);
        userMap = generateUserMap(users);
        slackEntries = loadEntries(messageFile);
        notebookEntries = createNotebookEntries(slackEntries, userMap);
        notebookDays = generateNotebookDays(notebookEntries);
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

    public static SlackEntry[] loadEntries(File messageFile) {
        Gson gson = new Gson();
        SlackEntry[] entries = new SlackEntry[0];
        try {
            entries = gson.fromJson(new FileReader(messageFile), SlackEntry[].class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        for (SlackEntry slackEntry : entries)
            slackEntry.tagString();
        return entries;
    }

    public static SlackUser[] loadUsers(File userFile) {
        Gson gson = new Gson();
        SlackUser[] users = new SlackUser[0];
        try {
            users = gson.fromJson(new FileReader(userFile), SlackUser[].class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return users;
    }

    public static Map<String, SlackUser> generateUserMap(SlackUser[] slackUsers) {
        Map<String, SlackUser> userMap = new HashMap<>();
        for (SlackUser user : slackUsers) {
            userMap.put(user.id, user);
        }
        return userMap;
    }

    public void printEntries() {
        for (NotebookDay notebookDay : notebookDays)
            notebookDay.printEntries();
    }
}
