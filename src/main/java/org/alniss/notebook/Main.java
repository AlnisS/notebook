package org.alniss.notebook;

import org.alniss.notebook.latex.LatexManager;
import org.alniss.notebook.notebookdata.DataInfo;
import org.alniss.notebook.notebookdata.NotebookDataManager;
import org.alniss.notebook.superentry.SuperEntryManager;

/**
 * Main class for the whole thing.
 */
public class Main {
    /**
     * The main method (doesn't do anything with arguments yet).
     * @param args doesn't do anything yet.
     */
    public static void main(String[] args) {
        SuperEntryManager sem = new SuperEntryManager();

        sem.loadSlackEntries(DataInfo.messageFile);
        sem.loadSlackEntries(DataInfo.messageFile2);

        sem.resolveAllUnquestioningly();
        sem.pushAllUnquestioningly();
        sem.serializeSave(DataInfo.saveFile);

        sem = new SuperEntryManager();
        sem.deserializeSave(DataInfo.saveFile);

        NotebookDataManager ndm = new NotebookDataManager(sem.getUserMap());
        ndm.prepareNotebookData(sem);
        ndm.printEntries();

        LatexManager.createLatex(ndm);
        LatexManager.compileLatex();
        LatexManager.openLatexPDF();

    }
}
