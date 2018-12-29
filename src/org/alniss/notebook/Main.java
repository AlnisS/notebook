package org.alniss.notebook;

import org.alniss.notebook.notebookdata.NotebookDataManager;

public class Main {
    public static void main(String[] args) {
        NotebookDataManager notebookDataManager = new NotebookDataManager();
        notebookDataManager.printEntries();
        notebookDataManager.createLatex();
        notebookDataManager.compileLatex();
        notebookDataManager.openLatexPDF();
    }
}
