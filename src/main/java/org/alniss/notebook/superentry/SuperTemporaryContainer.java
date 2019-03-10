package org.alniss.notebook.superentry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;

public class SuperTemporaryContainer {
    public SuperEntry[] super_entries;

    public SuperTemporaryContainer(Map<String, SuperEntry> superEntries) {
        SuperEntry[] tmp0 = new SuperEntry[superEntries.values().size()];
        superEntries.values().toArray(tmp0);
        this.super_entries = tmp0;
    }

    public static SuperEntry[] deserializeEntries(File input) {
        SuperTemporaryContainer result = null;
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(input))) {
            result = gson.fromJson(bufferedReader, SuperTemporaryContainer.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result.super_entries;
    }

    public static void serializeEntries(Map<String, SuperEntry> superEntries, File out) {
        SuperTemporaryContainer tmp = new SuperTemporaryContainer(superEntries);
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();

        try {
            FileWriter writer = new FileWriter(out);
            writer.write(gson.toJson(tmp));
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
