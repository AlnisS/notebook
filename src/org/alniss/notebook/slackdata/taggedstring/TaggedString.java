package org.alniss.notebook.slackdata.taggedstring;

import org.alniss.notebook.notebookdata.NotebookDataManager;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaggedString {
    public String rawString;
    public String processedString;
    public Map<String, Object> tagValues;

    public static Tag[] allTags = new Tag[] {
            new Tag() {
                @Override
                public String getInnerRegex() {
                    return "(\\d|\\d\\d)\\-(\\d|\\d\\d)";
                }

                @Override
                public Object getValue(String tag) {
                    int month = Integer.parseInt(tag.substring(0, tag.indexOf("-")));
                    int day = Integer.parseInt(tag.substring(tag.indexOf("-") + 1));
                    int year = Calendar.getInstance().get(Calendar.YEAR);

                    Date result = new GregorianCalendar(year - 1, month - 1, day).getTime(); //new Date(new Date().getYear() - 1, month - 1, day);
                    if (result.before(NotebookDataManager.seasonStart))
                        result = new GregorianCalendar(year, month - 1, day).getTime();
                    return result;
                }

                @Override
                public String getReplacement(String tag) {
                    return "";
                }

                @Override
                public String getName() {
                    return "date";
                }
            },
            new Tag() {
                @Override
                public String getInnerRegex() {
                    return "cont";
                }

                @Override
                public Object getValue(String tag) {
                    return null;
                }

                @Override
                public String getReplacement(String tag) {
                    return "";
                }

                @Override
                public String getName() {
                    return "cont";
                }
            }
    };

    public TaggedString(String rawString) {
        tagValues = new HashMap<>();
        this.rawString = rawString;
        this.processedString = rawString;

        for (Tag tag : allTags) {
            String tagRegex = "\\[" + tag.getInnerRegex() + "\\]";
            Pattern pattern = Pattern.compile(tagRegex);
            Matcher matcher = pattern.matcher(rawString);

            int i = 0;
            while (matcher.find()){
                String tagString = matcher.group();
                tagString = tagString.substring(1, tagString.length() - 1);
                tagValues.put(tag.getName() + i++, tag.getValue(tagString));
                processedString = processedString.replaceFirst(tagRegex, tag.getReplacement(tagString)).trim();
            }
        }
    }
}
