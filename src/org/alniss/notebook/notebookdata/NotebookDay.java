package org.alniss.notebook.notebookdata;

import org.alniss.notebook.slackdata.SlackEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotebookDay {
    public List<NotebookEntry> notebookEntries;
    public Date date;

    public int[] start, end;
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

    public void initStartTime() {
        if (!anyHasTag("ms0"))
            return;
        startTime = timeArrayToString(start = (int[]) anyGetTagValue("ms0"));
    }

    public void initEndTime() {
        if (!anyHasTag("me0"))
            return;
        endTime = timeArrayToString(end = (int[]) anyGetTagValue("me0"));
    }

    public void initTimesIfApplicable() {
        if (startTime == null)
            initStartTime();
        if (endTime == null)
            initEndTime();
    }

    public List<Boolean> hasTag(String tag) {
        List<Boolean> result = new ArrayList<>();
        for (NotebookEntry entry : notebookEntries)
            result.add(entry.anyHasTag(tag));
        return result;
    }

    public List<Object> getTagValue(String tag) {
        List<Object> result = new ArrayList<>();
        for (NotebookEntry entry : notebookEntries)
            result.add(entry.anyGetTagValue(tag));
        return result;
    }

    public boolean anyHasTag(String tag) {
        List<Boolean> hasTag = hasTag(tag);
        for (Boolean b : hasTag)
            if (b)
                return true;
        return false;
    }

    public Object anyGetTagValue(String tag) {
        List<Object> tagValue = getTagValue(tag);
        for (Object o : tagValue)
            if (o != null)
                return o;
        return null;
    }

    public static String timeArrayToString(int[] time) {
        return twoDigit(time[0]) + ":" + twoDigit(time[1]);
    }

    public static String twoDigit(Integer number) {
        if (number >= 10)
            return number.toString();
        return "0" + number;
    }
}
