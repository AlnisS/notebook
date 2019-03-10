package org.alniss.notebook;

import org.alniss.notebook.latex.LatexManager;
import org.alniss.notebook.notebookdata.NotebookDataManager;
import org.alniss.notebook.superentry.SuperEntryManager;

public class Main {
    public static void main(String[] args) {
        NotebookDataManager ndm = new NotebookDataManager();
        SuperEntryManager sem = new SuperEntryManager();

        sem.loadSlackEntries(ndm.userMap, ndm.messageFile, ndm.messageFile2);

        sem.updateAllUnquestioningly();
        sem.serializeSave(NotebookDataManager.saveFile);

        sem = new SuperEntryManager();
        sem.deserializeSave(NotebookDataManager.saveFile);

        ndm.prepareNotebookData(sem);
        ndm.printEntries();

        LatexManager.createLatex(ndm);
        LatexManager.compileLatex();
        LatexManager.openLatexPDF();
    }
}
