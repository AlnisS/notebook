package org.alniss.notebook.notebookdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotebookSubsection {
    private String name;
    private List<NotebookEntry> notebookEntries;

    private NotebookSubsection(String name) {
        this.name = name;
        notebookEntries = new ArrayList<>();
    }

    public static void sort(List<NotebookEntry> entries, Map<String, NotebookSubsection> subsections) {
        for (NotebookEntry entry : entries) {
            if (!entry.anyHasTag("sub0"))
                addToOrMakeCategory(entry, "unspecified", subsections);
            else
                addToOrMakeCategory(entry, (String) entry.anyGetTagValue("sub0"), subsections);
        }
    }

    public int entryCount() {
        return notebookEntries.size();
    }

    public List<NotebookEntry> getNotebookEntries() {
        return notebookEntries;
    }

    public String getName() {
        return name;
    }

    private void addEntry(NotebookEntry entry) {
        notebookEntries.add(entry);
    }

    private static void addToOrMakeCategory(NotebookEntry entry, String category,
                                           Map<String, NotebookSubsection> subsections) {
        if (!subsections.containsKey(category))
            subsections.put(category, new NotebookSubsection(category));
        subsections.get(category).addEntry(entry);
    }
}
