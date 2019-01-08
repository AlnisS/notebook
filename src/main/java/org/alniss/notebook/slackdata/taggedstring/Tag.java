package org.alniss.notebook.slackdata.taggedstring;

/**
 * Tags hold data as key-value pairs, value-implied key pairs, or key-implied value pairs.
 *
 * <p> Tags can hold data in many ways. For example, a date Tag holding September 3rd
 * might look like [9-3]. It has a value of September 3rd with an implied key of date.
 * A continue Tag may exist or not, possibly looking like [cont]. It would be the key cont
 * with the implied boolean of whether or not it exists. There may also be Tags like
 * [fig 0] which might represent a fig tag with value 0. This would be a standard key-value.
 * Because Tags are implemented as anonymous classes and the way they hold their information is
 * defined programmatically within the implementing class, so they are rather versatile.
 *
 * <p> What is common between all Tags is that they appear within a [ ] pair within a String. Other
 * than that, they can really be anything.
 */
public interface Tag {
    /**
     * regex matching inner part of tag
     * @return regex matching the inner part of the tag, not including the [ ] characters.
     */
    String getInnerRegex();

    /**
     * value of a tag in String form
     * @param tag String matching inner regex representing the contents of the Tag.
     * @return value extracted from those contents.
     */
    Object getValue(String tag);

    /**
     * replacement for a tag in String form
     * @param tag String matching inner regex representing the contents of the Tag.
     * @return replacement for the entire Tag assuming [ ] is removed.
     */
    String getReplacement(String tag);

    /**
     * unique name for the tag
     * @return unique name of the Tag type used as key in tables holding key-tag value pairs.
     */
    String getName();
}
