package org.alniss.notebook.notebookdata;

import com.google.gson.Gson;
import org.alniss.notebook.slackdata.SlackEntry;
import org.alniss.notebook.slackdata.SlackUser;

import java.io.*;
import java.util.*;

public class NotebookDataManager {
    public File messageFile = new File(System.getProperty("user.dir") + "\\data\\testdata.json");
    public File userFile = new File(System.getProperty("user.dir") + "\\data\\users.json");
    public File latexFile = new File(System.getProperty("user.dir") + "\\data\\latex\\notebook.tex");

    public SlackEntry[] slackEntries;
    public SlackUser[] users;
    public Map<String, SlackUser> userMap;

    public NotebookEntry[] notebookEntries;
    public NotebookDay[] notebookDays;
    public static final Date seasonStart = new GregorianCalendar(2018, Calendar.SEPTEMBER, 3).getTime();

    public NotebookDataManager() {
        users = loadUsers(userFile);
        userMap = generateUserMap(users);
        slackEntries = loadEntries(messageFile);
        notebookEntries = createNotebookEntries(slackEntries, userMap);
        notebookDays = generateNotebookDays(notebookEntries);
    }

    public static NotebookDay[] generateNotebookDays(NotebookEntry[] notebookEntries) {
        Map<String, NotebookDay> arrangedNotebookDays = new HashMap<>();
        for (NotebookEntry notebookEntry : notebookEntries) {
            String id = notebookEntry.docDay.toString().substring(0, 10);
            if (arrangedNotebookDays.get(id) == null)
                arrangedNotebookDays.put(id, new NotebookDay());
            arrangedNotebookDays.get(id).notebookEntries.add(notebookEntry);
            arrangedNotebookDays.get(id).date = notebookEntry.docDay;
        }
        return arrangedNotebookDays.values().toArray(new NotebookDay[arrangedNotebookDays.size()]);
    }

    public static NotebookEntry[] createNotebookEntries(SlackEntry[] context, Map<String, SlackUser> users) {
        List<NotebookEntry> notebookEntries = new ArrayList<>();
        for (int i = 0; i < context.length; i++) {
            if (context[i].extractDocDate() != null)
                notebookEntries.add(new NotebookEntry(context, i, users));
        }
        return notebookEntries.toArray(new NotebookEntry[notebookEntries.size()]);
    }

    public static SlackEntry[] loadEntries(File messageFile) {
        Gson gson = new Gson();
        SlackEntry[] entries = new SlackEntry[0];
        try {
            entries = gson.fromJson(new FileReader(messageFile), SlackEntry[].class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        for (SlackEntry slackEntry : entries)
            slackEntry.tagString();
        return entries;
    }

    public static SlackUser[] loadUsers(File userFile) {
        Gson gson = new Gson();
        SlackUser[] users = new SlackUser[0];
        try {
            users = gson.fromJson(new FileReader(userFile), SlackUser[].class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return users;
    }

    public static Map<String, SlackUser> generateUserMap(SlackUser[] slackUsers) {
        Map<String, SlackUser> userMap = new HashMap<>();
        for (SlackUser user : slackUsers) {
            userMap.put(user.id, user);
        }
        return userMap;
    }

    public void printEntries() {
        for (NotebookDay notebookDay : notebookDays)
            notebookDay.printEntries();
    }

    public void createLatex() {
        try (PrintWriter printWriter = new PrintWriter(latexFile))
        {
            printWriter.println("\\documentclass{article}" +
                    "\n\\title{Notebook Entries}" +
                    "\n\\begin{document}" +
                    "\n\\maketitle\n");
            for (NotebookDay notebookDay : notebookDays) {
                printWriter.println("\\section{" + notebookDay.date.toString().substring(0, 10) + "}");
                for (NotebookEntry notebookEntry : notebookDay.notebookEntries)
                    printWriter.println("\\paragraph{entry:} " + notebookEntry.formattedSlackEntries
                            + " \\textbf{-" + notebookEntry.author.real_name + "}");
            }
            printWriter.println("\\end{document}");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void compileLatex() {
        try {
            String folder = latexFile.getAbsolutePath();
            folder = folder.substring(0, folder.length() - "notebook.tex".length());
            ProcessBuilder builder = new ProcessBuilder(
                    "cmd.exe", "/c", "cd \"" + folder + "\" && pdflatex notebook.tex");
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
}
