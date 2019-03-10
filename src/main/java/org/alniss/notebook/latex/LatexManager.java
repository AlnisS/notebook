package org.alniss.notebook.latex;

import org.alniss.notebook.notebookdata.DataInfo;
import org.alniss.notebook.notebookdata.NotebookDataManager;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LatexManager {
    public static void createLatex(NotebookDataManager ndm) {
        LatexConnector.init();
        LatexHolder.execute(ndm);
        LatexConnector.close();
    }

    public static void compileLatex() {
        try {
            String folder = DataInfo.latexFile.getAbsolutePath();
            folder = folder.substring(0, folder.length() - "notebook.tex".length());
            ProcessBuilder builder = new ProcessBuilder(
                    "/bin/bash", "-c", "cd \"" + folder + "\" && pdflatex notebook.tex");
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) { break; }
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openLatexPDF() {
        try {
            Desktop.getDesktop().open(DataInfo.latexPDFFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
