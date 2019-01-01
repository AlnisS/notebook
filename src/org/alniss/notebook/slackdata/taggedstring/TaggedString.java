package org.alniss.notebook.slackdata.taggedstring;

import org.alniss.notebook.notebookdata.NotebookDataManager;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a String with Tags associated with it.
 *
 * <p> Tags are specified within [ ] characters. They are specified as anonymous
 * classes implementing the Tag interface. A TaggedString extracts the info
 * from such tags and holds the original, raw String, the processed String
 * with tag text removed, and the key-value pairs of the tags themselves.
 *
 * <p> When tags are to be retrieved, they are looked up in the form tagname#.
 * tagname represents the name of the Tag from the getName() interface method.
 * # specifies which tag to get if/when there are multiple. It should be 0 for
 * the first one regardless of whether or not there are multiple. For example,
 * the 0th date tag would be referred to as date0.
 *
 * @see org.alniss.notebook.slackdata.taggedstring.Tag
 */
public class TaggedString {
    private String rawString;
    private String processedString;
    private Map<String, Object> tagValues;

    private static Tag[] allTags = new Tag[] {
            //  meeting start date
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

                    Date result = new GregorianCalendar(year - 1, month - 1, day).getTime();
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
            //  meeting continue
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
            },
            //  meeting start time
            new Tag() {
                @Override
                public String getInnerRegex() {
                    return "ms \\d\\d:\\d\\d";
                }

                @Override
                public Object getValue(String tag) {
                    return new int[] {Integer.parseInt(tag.substring(3, 5)), Integer.parseInt(tag.substring(6, 8))};
                }

                @Override
                public String getReplacement(String tag) {
                    return "";
                }

                @Override
                public String getName() {
                    return "ms";
                }
            },
            //  meeting end time
            new Tag() {
                @Override
                public String getInnerRegex() {
                    return "me \\d\\d:\\d\\d";
                }

                @Override
                public Object getValue(String tag) {
                    return new int[] {Integer.parseInt(tag.substring(3, 5)), Integer.parseInt(tag.substring(6, 8))};
                }

                @Override
                public String getReplacement(String tag) {
                    return "";
                }

                @Override
                public String getName() {
                    return "me";
                }
            },
            //  subsection
            new Tag() {
                @Override
                public String getInnerRegex() {
                    return "sub [^\\]]+";
                }

                @Override
                public Object getValue(String tag) {
                    return tag.substring(4);
                }

                @Override
                public String getReplacement(String tag) {
                    return "";
                }

                @Override
                public String getName() {
                    return "sub";
                }
            },
            //  newline
            new Tag() {
                @Override
                public String getInnerRegex() {
                    return "(nl|nl \\d+)";
                }

                @Override
                public Object getValue(String tag) {
                    return null;
                }

                @Override
                public String getReplacement(String tag) {
                    if (tag.length() == 2)
                        return "\\\\newline";
                    int x = Integer.parseInt(tag.substring(3));
                    String res = "";
                    for (int i = 0; i < x; i++)
                        res += "\\\\newline";
                    return res;
                }

                @Override
                public String getName() {
                    return "nl";
                }
            }
    };

    /**
     * Processes input against tags specified in allTags, produces key-value map, and creates cleaned String.
     * @param rawString raw String to be processed and used.
     */
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

    /**
     * Gets the value of a tag from the original String using a name-# pair (e.g. date0).
     * @param tag tag to get value from.
     * @return value of tag specified.
     */
    public Object getTagValue(String tag) {
        return tagValues.get(tag);
    }

    /**
     * Gets whether or not a tag exists in the original String using a name-# pair (e.g. date0).
     * @param tag tag to get value from.
     * @return whether the original String has that tag.
     */
    public boolean hasTag(String tag) {
        return tagValues.containsKey(tag);
    }

    /**
     * Gets the original, unprocessed String given during creation of the TaggedString.
     * @return original, unprocessed String given during creation of the TaggedString.
     */
    public String getRawString() {
        return rawString;
    }

    /**
     * Gets the processed String with tags removed from the text.
     * @return processed String with tags removed from the text.
     */
    public String getProcessedString() {
        return processedString;
    }
}
