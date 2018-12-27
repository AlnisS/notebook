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
        System.out.println("documentation day: " + date.toString().substring(0, 10));
        int i = 0;
        for (NotebookEntry notebookEntry : notebookEntries)
            System.out.println("entry " + i++ + ":\n"
                    + notebookEntry.formattedSlackEntries + "\n\t\t- " + notebookEntry.author.real_name + "\n");
    }
}
