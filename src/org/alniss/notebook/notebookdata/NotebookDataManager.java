package org.alniss.notebook.notebookdata;

import com.google.gson.Gson;
import org.alniss.notebook.slackdata.SlackEntry;
import org.alniss.notebook.slackdata.SlackUser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotebookDataManager {
    //public File messageFile = new File(System.getProperty("user.dir") + "\\data\\2018-12-12.json");
    public File messageFile = new File(System.getProperty("user.dir") + "\\data\\testdata.json");
    public File userFile = new File(System.getProperty("user.dir") + "\\data\\users.json");

    public SlackEntry[] slackEntries;
    public SlackUser[] users;
    public Map<String, SlackUser> userMap;

    public List<NotebookEntry> notebookEntries;

    public NotebookDataManager() {
        users = loadUsers(userFile);
        userMap = generateUserMap(users);
        slackEntries = loadEntries(messageFile);
        notebookEntries = createNotebookEntries(slackEntries, userMap);
    }

    public static List<NotebookEntry> createNotebookEntries(SlackEntry[] context, Map<String, SlackUser> users) {
        List<NotebookEntry> notebookEntries = new ArrayList<>();
        for (int i = 0; i < context.length; i++) {
            if (context[i].extractDocDate() != null)
                notebookEntries.add(new NotebookEntry(context, i, users));
        }
        return notebookEntries;
    }

    public static SlackEntry[] loadEntries(File messageFile) {
        Gson gson = new Gson();
        SlackEntry[] entries = new SlackEntry[0];
        try {
            entries = gson.fromJson(new FileReader(messageFile), SlackEntry[].class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    public void printData() {
        for (SlackEntry entry : slackEntries) {
            if (entry.files != null && entry.files.length != 0)
                System.out.print(entry.files[0].id + " ");
            System.out.println(entry.type + " " + userMap.get(entry.user).real_name + " "
                    + entry.ts + " " + entry.text);
            System.out.println(entry.extractDocDate());
        }
    }
}