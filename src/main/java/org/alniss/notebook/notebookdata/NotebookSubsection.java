package org.alniss.notebook.notebookdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The subsections/categories in the smaller left panel in the notebook pages.
 * Each has NotebookEntries and serves as a sort of intermediate organizational
 * layer in the holding of notebook data.
 */
public class NotebookSubsection {
    /**
     * The title of the subsection.
     */
    private String name;
    /**
     * The notebook entries within this subsection.
     */
    private List<NotebookEntry> notebookEntries = new ArrayList<>();

    /**
     * Creates a new subsection given a name.
     * @param name name of the new subsection.
     */
    private NotebookSubsection(String name) {
        this.name = name;
    }

    /**
     * Sorts the given notebook entries into categories/subsections (not sorting
     * the order). It will use existing subsections if the name matches or
     * create new ones as needed using the addToOrMakeCategory method. If there
     * is no subsection specified in the notebook entry, its subsection will be
     * the "unspecified" category.
     * @param entries notebook entries to sort into subsections.
     * @param subsections Map (category title -> subsection) as target of additions.
     */
    public static void sort(List<NotebookEntry> entries, Map<String, NotebookSubsection> subsections) {
        for (NotebookEntry entry : entries) {
            if (!entry.anyHasTag("sub0"))
                addToOrMakeCategory(entry, "unspecified", subsections);
            else
                addToOrMakeCategory(entry, (String) entry.anyGetTagValue("sub0"), subsections);
        }
    }

    /**
     * @return how many notebook entries this subsection has.
     */
    public int entryCount() {
        return notebookEntries.size();
    }

    /**
     * @return the List of notebook entries in this subsection.
     */
    public List<NotebookEntry> getNotebookEntries() {
        return notebookEntries;
    }

    /**
     * @return name/title of this subsection.
     */
    public String getName() {
        return name;
    }

    /**
     * Adds the notebook entry to this subsection/category.
     * @param entry notebook entry to add.
     */
    private void addEntry(NotebookEntry entry) {
        notebookEntries.add(entry);
    }

    /**
     * Adds the entry to the existing subsection in the Map or makes a new one
     * if needed.
     * @param entry notebook entry to file/sort/categorize/add.
     * @param category category/subsection in which the entry should be.
     * @param subsections catalog of existing subsections/place to add new one if needed.
     */
    private static void addToOrMakeCategory(NotebookEntry entry, String category,
                                           Map<String, NotebookSubsection> subsections) {
        if (!subsections.containsKey(category))  // TODO: maybe this should be case insensitive
            subsections.put(category, new NotebookSubsection(category));
        subsections.get(category).addEntry(entry);
    }
}
