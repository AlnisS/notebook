package org.alniss.notebook;

import org.alniss.notebook.latex.LatexManager;
import org.alniss.notebook.notebookdata.DataInfo;
import org.alniss.notebook.notebookdata.NotebookDataManager;
import org.alniss.notebook.superentry.SuperEntryManager;

public class Main {
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
