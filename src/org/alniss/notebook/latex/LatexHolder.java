package org.alniss.notebook.latex;
import org.alniss.notebook.notebookdata.NotebookDataManager;
import org.alniss.notebook.notebookdata.NotebookDay;
import org.alniss.notebook.notebookdata.NotebookEntry;

import static org.alniss.notebook.latex.LatexConnector.*;

public class LatexHolder {
    public static void execute(NotebookDataManager ndm) {
        documentclass("11pt", "article");
        usepackage("letterpaper, portrait, margin=.5in", "geometry");
        usepackage("tabularx");
        title("Notebook Entries");
        begin("document");
        maketitle();

        for (NotebookDay notebookDay : ndm.notebookDays) {
            String section = notebookDay.date.toString().substring(0, 10);
            section += ", start " + notebookDay.startTime + ", end " + notebookDay.endTime;
            section(section);
            begintabularx("| p{1in} | X |");
            hline();
            for (NotebookEntry notebookEntry : notebookDay.notebookEntries) {
                tabularrow(bold("entry"), notebookEntry.formattedSlackEntries + " "
                        + bold("-" + notebookEntry.author.real_name));
                hline();
            }
            endtabularx();
        }
        end("document");
    }
}
