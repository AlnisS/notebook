package org.alniss.notebook.notebookdata;

import java.util.*;

/**
 * Represents a day in the notebook.
 */
public class NotebookDay {
    /**
     * Entries for the day.
     */
    public List<NotebookEntry> notebookEntries = new ArrayList<>();
    /**
     * Subsections for the day (i.e. the categories on the left).
     */
    public Map<String, NotebookSubsection> subsections;
    /**
     * The day of the notebook page/day.
     */
    public Date date;
    /**
     * Title for the page.
     */
    public String title = null;

    /**
     * Basically a tuple for the times (e.g. {15, 45} for 3:45 PM).
     */
    public int[] start, end;
    /**
     * Nice start time String (e.g. "15:45" for 3:45 PM).
     */
    public String startTime = null;
    /**
     * Nice end time String (e.g. "18:00" for 6:00 PM).
     */
    public String endTime = null;

    /**
     * Sets the title if any entry specifies one.
     */
    public void setupTitle() {
        if (!anyHasTag("title0"))
            return;
        title = (String) anyGetTagValue("title0");
    }

    /**
     * Pigeonholes entries into their categories.
     */
    public void sortEntries() {
        subsections = new HashMap<>();
        NotebookSubsection.sort(notebookEntries, subsections);
    }

    /**
     * Prints all entries to the console.
     */
    public void printEntries() {
        System.out.println("documentation day: " + date.toString().substring(0, 10));
        int i = 0;
        for (NotebookSubsection subsection : subsections.values())
            for (NotebookEntry notebookEntry : subsection.getNotebookEntries())
                System.out.println("entry " + i++ + " " + subsection.getName() + ":\n"
                        + notebookEntry.formattedSlackEntries + "\n\t\t- " + notebookEntry.author.real_name + "\n");
    }

    /**
     * Sets the start time if any entry specifies one.
     */
    public void initStartTime() {
        if (!anyHasTag("ms0"))
            return;
        startTime = timeArrayToString(start = (int[]) anyGetTagValue("ms0"));
    }

    /**
     * Sets the end time if any entry specifies one.
     */
    public void initEndTime() {
        if (!anyHasTag("me0"))
            return;
        endTime = timeArrayToString(end = (int[]) anyGetTagValue("me0"));
    }

    /**
     * Initializes both start and end times if they haven't been set.
     */
    public void initTimesIfApplicable() {
        if (startTime == null)
            initStartTime();
        if (endTime == null)
            initEndTime();
    }

    /**
     * @param tag the Tag in question.
     * @return list of booleans for whether each entry has that tag in order.
     */
    public List<Boolean> hasTag(String tag) {
        List<Boolean> result = new ArrayList<>();
        for (NotebookEntry entry : notebookEntries)
            result.add(entry.anyHasTag(tag));
        return result;
    }

    /**
     * @param tag the Tag in question.
     * @return list of values reported by each entry in order.
     */
    public List<Object> getTagValue(String tag) {
        List<Object> result = new ArrayList<>();
        for (NotebookEntry entry : notebookEntries)
            result.add(entry.anyGetTagValue(tag));
        return result;
    }

    /**
     * @param tag the Tag in question.
     * @return whether any entry has that Tag.
     */
    public boolean anyHasTag(String tag) {
        List<Boolean> hasTag = hasTag(tag);
        for (Boolean b : hasTag)
            if (b)
                return true;
        return false;
    }

    /**
     * @param tag the Tag in question.
     * @return the first non-null value reported for that tag by any entry.
     */
    public Object anyGetTagValue(String tag) {
        List<Object> tagValue = getTagValue(tag);
        for (Object o : tagValue)
            if (o != null)
                return o;
        return null;
    }

    /**
     * Converts the time tuple int array dealy to a nice String.
     * @param time time tuple int array dealy.
     * @return a nice String representation of the time tuple int array dealy.
     */
    public static String timeArrayToString(int[] time) {
        return twoDigit(time[0]) + ":" + twoDigit(time[1]);
    }

    /**
     * Pads a number with a zero to make it two digits if needed. This is not a
     * very smart method.
     * @param number number to possibly pad if needed.
     * @return nicely formatted number padded to be two digits long.
     */
    public static String twoDigit(Integer number) {
        if (number >= 10)
            return number.toString();
        return "0" + number;
    }
}
