package org.alniss.notebook.notebookdata;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotebookDay {
    public List<NotebookEntry> notebookEntries;
    public Date date;

    NotebookDay() {
        notebookEntries = new ArrayList<>();
    }

    public void printEntries() {
        System.out.println("doumentation day: " + date.toString().substring(0, 10));
        for (NotebookEntry notebookEntry : notebookEntries)
            System.out.println("| " + notebookEntry.formattedSlackEntries + "\n| - " + notebookEntry.author.real_name);
    }
}
