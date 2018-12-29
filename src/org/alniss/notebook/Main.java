package org.alniss.notebook;

import org.alniss.notebook.notebookdata.NotebookDataManager;
import org.alniss.notebook.slackdata.taggedstring.TaggedString;

public class Main {
    public static void main(String[] args) {
        //MySQLTests.insertTest();
        //DBManager dbManager = new DBManager();
        //NotebookDataManager notebookDataManager = new NotebookDataManager();
        //notebookDataManager.printData();
        //System.out.println("\n");

        NotebookDataManager notebookDataManager = new NotebookDataManager();
        notebookDataManager.printEntries();
        notebookDataManager.createLatex();
        notebookDataManager.compileLatex();
    }
}
