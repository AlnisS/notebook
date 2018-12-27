package org.alniss.notebook.slackdata.taggedstring;

public interface Tag {
    String getInnerRegex();
    Object getValue(String tag);
    String getReplacement(String tag);
    String getName();
}
