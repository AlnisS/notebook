package org.alniss.notebook;

import org.alniss.notebook.latex.LatexManager;
import org.alniss.notebook.notebookdata.NotebookDataManager;
import org.alniss.notebook.superentry.SuperEntryManager;

public class Main {
    public static void main(String[] args) {
        NotebookDataManager notebookDataManager = new NotebookDataManager();

        notebookDataManager.superEntryManager.updateAllUnquestioningly();
        notebookDataManager.superEntryManager.serializeEntries(NotebookDataManager.saveFile);

        notebookDataManager.superEntryManager = new SuperEntryManager();
        notebookDataManager.superEntryManager.deserializeAndAddEntries(NotebookDataManager.saveFile);

        notebookDataManager.prepareNotebookData();
        notebookDataManager.printEntries();

        LatexManager.createLatex(notebookDataManager);
        LatexManager.compileLatex();
        LatexManager.openLatexPDF();
    }
}
