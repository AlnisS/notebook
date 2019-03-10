package org.alniss.notebook.superentry;

import com.google.gson.Gson;
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

    public SuperEntryManager() {
        superEntries = new HashMap<>();
    }

    public void deserializeSave(File input) {
        addSuperEntries(SuperTemporaryContainer.deserializeEntries(input));
        for (SuperEntry entry : superEntries.values())
            entry.initAfterDeserialization();
    }

    public void serializeSave(File out) {
        SuperTemporaryContainer.serializeEntries(superEntries, out);
    }

    public void addSuperEntries(SuperEntry[] entries) {
        for (SuperEntry entry : entries) {
            superEntries.put(entry.getUniqueID(), entry);
        }
    }

    public void loadSlackEntries(Map<String, SlackUser> userMap, File... files) {
        for (File file : files) {
            addSlackEntries(loadSlackEntries(file), userMap);
        }
    }

    public static SlackEntry[] loadSlackEntries(File messageFile) {
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

    public void addSlackEntries(SlackEntry[] slackEntries, Map<String, SlackUser> users) {
        for (SlackEntry slackEntry : slackEntries) {
            SuperEntry newEntry = new SuperEntry(slackEntry, users);
            String newEntryID = newEntry.getUniqueID();

            if (superEntries.containsKey(newEntryID)) {
                SuperEntry c = superEntries.get(newEntryID);
                c.setConflict(slackEntry);
                c.simpleMaybeResolveConflict();
            } else
                superEntries.put(newEntryID, newEntry);
        }
    }

    public List<SuperEntry> getAllSuperEntries() {
        return new ArrayList<>(superEntries.values());
    }

    public List<SuperEntry> getConflictingSuperEntries() {
        List<SuperEntry> result = new ArrayList<>();
        for (SuperEntry entry : superEntries.values()) {
            if (entry.hasConflict())
                result.add(entry);
        }
        return result;
    }

    public SlackEntry[] getAllAsSlackEntries(List<SuperEntry> superEntries) {
        SlackEntry[] result = new SlackEntry[superEntries.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = superEntries.get(i).toSlackEntry();
        }
        return result;
    }

    public SlackEntry[] getAllAsSlackEntries() {
        return getAllAsSlackEntries(getAllSuperEntries());
    }

    public void updateAllUnquestioningly() {
        List<SuperEntry> entries = getAllSuperEntries();
        for (SuperEntry entry : entries) {
            entry.update();
            entry.pushText();
        }
    }
}
