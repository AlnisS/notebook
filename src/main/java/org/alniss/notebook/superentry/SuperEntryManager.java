package org.alniss.notebook.superentry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.alniss.notebook.slackdata.SlackEntry;
import org.alniss.notebook.slackdata.SlackUser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuperEntryManager {
    private Map<String, SuperEntry> superEntries;

    public SuperEntryManager() {
        superEntries = new HashMap<>();
    }

    public void deserializeAndAddEntries(File input) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(input));
            SuperTemporaryContainer tmp = gson.fromJson(bufferedReader, SuperTemporaryContainer.class);
            addSuperEntries(tmp.super_entries);
            bufferedReader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (SuperEntry entry : superEntries.values())
            entry.initAfterDeserialization();
    }

    public void serializeEntries(File out) {
        SuperTemporaryContainer temp = new SuperTemporaryContainer(superEntries.values().toArray(new SuperEntry[superEntries.values().size()]));
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();

        try {
            FileWriter writer = new FileWriter(out);
            writer.write(gson.toJson(temp));
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addSuperEntries(SuperEntry[] entries) {
        for (SuperEntry entry : entries) {
            superEntries.put(entry.getUniqueID(), entry);
        }
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
            entry.overwriteEditWithOriginal();
        }
    }
}
