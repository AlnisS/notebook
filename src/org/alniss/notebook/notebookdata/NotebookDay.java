package org.alniss.notebook.notebookdata;

import org.alniss.notebook.slackdata.SlackEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotebookDay {
    public List<NotebookEntry> notebookEntries;
    public Date date;

    public int sh, sm, eh, em;
    public String startTime = null;
    public String endTime = null;

    NotebookDay() {
        notebookEntries = new ArrayList<>();
    }

    public void printEntries() {
        System.out.println("documentation day: " + date.toString().substring(0, 10));
        int i = 0;
        for (NotebookEntry notebookEntry : notebookEntries)
            System.out.println("entry " + i++ + ":\n"
                    + notebookEntry.formattedSlackEntries + "\n\t\t- " + notebookEntry.author.real_name + "\n");
    }

    public void initTimesIfApplicable() {
        //TODO: also make this less janky
        if (startTime == null) {
            for (NotebookEntry notebookEntry : notebookEntries) {
                for (SlackEntry slackEntry : notebookEntry.slackEntries) {
                    if (slackEntry.taggedString.hasTag("ms0")) {
                        int[] ms = (int[]) slackEntry.taggedString.getTagValue("ms0");
                        sh = ms[0];
                        sm = ms[1];
                    }
                }
            }
            String hour = "" + sh;
            String minute = "" + sm;
            if (sh < 10)
                hour = "0" + hour;
            if (sm < 10)
                minute = "0" + minute;
            startTime = hour + ":" + minute;
            if (sh == 0 && sm == 0)
                startTime = null;
        }

        if (endTime == null) {
            for (NotebookEntry notebookEntry : notebookEntries) {
                for (SlackEntry slackEntry : notebookEntry.slackEntries) {
                    if (slackEntry.taggedString.hasTag("me0")) {
                        int[] me = (int[]) slackEntry.taggedString.getTagValue("me0");
                        eh = me[0];
                        em = me[1];
                    }
                }
            }
            String hour = "" + eh;
            String minute = "" + em;
            if (eh < 10)
                hour = "0" + hour;
            if (em < 10)
                minute = "0" + minute;
            endTime = hour + ":" + minute;
            if (eh == 0 && em == 0)
                endTime = null;
        }
    }
}
