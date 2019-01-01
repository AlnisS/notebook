package org.alniss.notebook.latex;
import org.alniss.notebook.notebookdata.NotebookDataManager;
import org.alniss.notebook.notebookdata.NotebookDay;
import org.alniss.notebook.notebookdata.NotebookEntry;
import org.alniss.notebook.notebookdata.NotebookSubsection;

import java.util.List;

import static org.alniss.notebook.latex.LatexConnector.*;

public class LatexHolder {
    public static void execute(NotebookDataManager ndm) {
        documentclass("11pt", "article");
        usepackage("letterpaper, portrait, margin=.5in", "geometry");
        usepackage("tabularx");
        usepackage("multirow");
        usepackage("graphicx");
        usepackage("table", "xcolor");

        title("Notebook Entries");
        begin("document");
        //maketitle();

        for (NotebookDay notebookDay : ndm.notebookDays) {
            //TODO: this is bad
            String section = notebookDay.date.toString().substring(0, 10);
            String time = notebookDay.startTime + " to " + notebookDay.endTime;

            //latexOut.println("\\pagebreak\n"); //uncomment to separate days into separate pages
            noindent();
            header(section, time, notebookDay.title);
            linebreak(3);
            begintabularx("p{1in} X");

            for (String subsection : notebookDay.subsections.keySet()) {
                hline();
                NotebookSubsection notebookSubsection = notebookDay.subsections.get(subsection);
                List<NotebookEntry> notebookEntries = notebookSubsection.getNotebookEntries();
                latexOut.print(multirow(notebookSubsection.entryCount(), "1in", bold(subsection)));
                for (NotebookEntry entry : notebookEntries)
                    entryrow(entry.formattedSlackEntries, entry.author.real_name);
            }
            hline();
            endtabularx();
            linebreak(6);
        }
        end("document");
    }
}
