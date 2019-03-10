package org.alniss.notebook.superentry;

import com.google.gson.Gson;
import org.alniss.notebook.notebookdata.DataInfo;
import org.alniss.notebook.slackdata.SlackEntry;
import org.alniss.notebook.slackdata.SlackUser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuperEntryManager {
    private Map<String, SuperEntry> superEntries;
    private Map<String, SlackUser> userMap = generateUserMap(loadUsers(DataInfo.userFile));

    public SuperEntryManager() {
        superEntries = new HashMap<>();
    }


    // save serialization related things

    public void serializeSave(File out) {
        SuperTemporaryContainer.serializeEntries(superEntries, out);
    }

    public void deserializeSave(File input) {
        SuperEntry[] entries = SuperTemporaryContainer.deserializeEntries(input);
        for (SuperEntry entry : superEntries.values())
            entry.initAfterDeserialization();
        addSuperEntries(entries);
    }

    public void addSuperEntries(SuperEntry[] entries) {
        for (SuperEntry entry : entries) {
            superEntries.put(entry.getUniqueID(), entry);
        }
    }


    // loading related things

    public void loadSlackEntries(File... files) {
        for (File file : files) {
            addSlackEntries(loadSlackEntriesSingle(file));
        }
    }

    public static SlackEntry[] loadSlackEntriesSingle(File messageFile) {
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

    public void addSlackEntries(SlackEntry[] slackEntries) {
        for (SlackEntry slackEntry : slackEntries) {
            SuperEntry newEntry = new SuperEntry(slackEntry, userMap);
            String newEntryID = newEntry.getUniqueID();

            if (superEntries.containsKey(newEntryID)) {
                SuperEntry c = superEntries.get(newEntryID);
                c.setConflict(slackEntry);
                c.simpleMaybeResolveConflict();
            } else
                superEntries.put(newEntryID, newEntry);
        }
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


    // misc getter things

    public Map<String, SlackUser> getUserMap() {
        return userMap;
    }

    public SlackEntry[] getAllAsSlackEntries() {
        return getAllAsSlackEntries(getAllSuperEntries());
    }

    public List<SuperEntry> getAllSuperEntries() {
        return new ArrayList<>(superEntries.values());
    }

    public SlackEntry[] getAllAsSlackEntries(List<SuperEntry> superEntries) {
        SlackEntry[] result = new SlackEntry[superEntries.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = superEntries.get(i).toSlackEntry();
        }
        return result;
    }

    public List<SuperEntry> getConflictingSuperEntries() {
        List<SuperEntry> result = new ArrayList<>();
        for (SuperEntry entry : superEntries.values()) {
            if (entry.hasConflict())
                result.add(entry);
        }
        return result;
    }


    // misc other things

    public void resolveAllUnquestioningly() {
        List<SuperEntry> entries = getAllSuperEntries();
        for (SuperEntry entry : entries) {
            entry.forceConflictResolve();
        }
    }

    public void pushAllUnquestioningly() {
        List<SuperEntry> entries = getAllSuperEntries();
        for (SuperEntry entry : entries) {
            entry.pushText();
        }
    }
}
