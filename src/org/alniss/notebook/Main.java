package org.alniss.notebook;

import org.alniss.notebook.notebookdata.NotebookDataManager;
import org.alniss.notebook.notebookdata.NotebookEntry;
import org.alniss.notebook.slackdata.SlackEntry;

public class Main {
    public static void main(String[] args) {
        //MySQLTests.insertTest();
        //DBManager dbManager = new DBManager();
        //NotebookDataManager notebookDataManager = new NotebookDataManager();
        //notebookDataManager.printData();
        //System.out.println("\n");

        NotebookDataManager notebookDataManager = new NotebookDataManager();
        //notebookDataManager.printData();
        for (NotebookEntry notebookEntry : notebookDataManager.notebookEntries) {
            System.out.println(notebookEntry.formattedSlackEntries + "\n- " + notebookEntry.author.real_name
                    + " (" + notebookEntry.docDay.toString().substring(0, 10) + ")");
        }
    }
}
