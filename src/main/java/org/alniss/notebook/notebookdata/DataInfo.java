package org.alniss.notebook.notebookdata;

import java.io.File;

public class DataInfo {
    public static File messageFile = relativeFile("/data/testdata.json");
    public static File messageFile2 = relativeFile("/data/testdata2.json");
    public static File saveFile = relativeFile("/data/save.json");
    public static File userFile = relativeFile("/data/users.json");
    public static File latexFile = relativeFile("/data/latex/notebook.tex");
    public static File latexPDFFile = relativeFile("/data/latex/notebook.pdf");

    public static File relativeFile(String path) {
        return new File(System.getProperty("user.dir") + path);
    }
}
