package org.alniss.notebook.latex;

import org.alniss.notebook.notebookdata.NotebookDataManager;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class LatexConnector {
    static PrintWriter latexOut;

    public static void init() {
        try {
            latexOut = new PrintWriter(NotebookDataManager.latexFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void documentclass(String args, String type) {
        latexOut.println("\\documentclass[" + args + "]{" + type + "}");
    }
    public static void usepackage(String args, String type) {
        latexOut.println("\\usepackage[" + args + "]{" + type + "}");
    }
    public static void usepackage(String type) {
        latexOut.println("\\usepackage{" + type + "}");
    }
    public static void title(String title) {
        latexOut.println("\\title{" + title + "}");
    }
    public static void begin(String type) {
        latexOut.println("\\begin{" + type + "}");
    }
    public static void maketitle() {
        latexOut.println("\\maketitle");
    }
    public static void section(String section) {
        latexOut.println("\\section{" + section + "}");
    }
    public static void begintabularx(String args) {
        latexOut.println("\\begin{tabularx}{\\textwidth}{" + args + "}");
    }
    public static void hline() {
        latexOut.println("\\hline");
    }
    public static void tabularrow(String... args) {
        latexOut.print(args[0]);
        for (int i = 1; i < args.length; i++)
            latexOut.print(" & " + args[i]);
        latexOut.println(" \\\\");
    }
    public static void endtabularx() {
        latexOut.println("\\end{tabularx}");
    }
    public static void end(String type) {
        latexOut.println("\\end{" + type + "}");
    }
    public static void multirow(int rows, String width, String text) {
        latexOut.print("\\multirow[t]{" + rows + "}{" + width + "}{" + text + "}");
    }
    public static void entryrow(String entry, String author) {
        latexOut.println(" & " + entry + bold(" -" + author) + "\\\\\\cline{2-2}");
    }
    public static void header(String section, String time) {
        //TODO: this is really bad
        latexOut.println("\\begin{tabularx}{\\textwidth}{| p{2in} | X  X  X  X  X  X |}\n" +
                "\\multirow{3}{2in}[.3em]{$\\vcenter{\\includegraphics[width=2in]{logo2.png}}$} & \\multicolumn{4}{l}{\\textbf{\\huge{" + section + "}}} & \\multicolumn{2}{r|}{\\textbf{" + time + "}}\\\\\n" +
                " & \\multicolumn{6}{l|}{\\textbf{\\Large{Some Title}}}\\\\\n" +
                " & foo & bar & \\cellcolor{gray!25}\\textbf{biz} & baz & qux & qix\n" +
                "\\end{tabularx}");
    }
    public static void noindent() {
        latexOut.println("\\noindent");
    }
    public static void linebreak() {
        latexOut.println("\\linebreak");
    }
    public static void linebreak(int x) {
        for (int i = 0; i < x; i++)
            linebreak();
    }

    public static String bold(String text) {
        return "\\textbf{" + text + "}";
    }

    public static void close() {
        latexOut.close();
    }
}
