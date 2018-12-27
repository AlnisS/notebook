package org.alniss.notebook.slackdata;

import org.alniss.notebook.slackdata.taggedstring.TaggedString;

import java.util.Date;

public class SlackEntry {
    public String type;
    public String text;
    public String user;
    public String ts;
    public String client_msg_id;
    public SlackFile[] files;
    public static final String dateRegex = "\\[(\\d|\\d\\d)\\-(\\d|\\d\\d)\\]";
    public TaggedString taggedString;

    /* JSON template
    {
        "client_msg_id": "",
        "type": "message",
        "text": "",
        "user": "U75P2QJH2",
        "ts": "1544626427.221200"
    }
     */

    public SlackEntry(String text, String user) {
        this.type = "message";
        this.text = text;
        this.user = user;
        this.ts = "";
        this.client_msg_id = "";
        this.files = null;
    }

    public void tagString() {
        taggedString = new TaggedString(text);
    }

    public boolean isStart() {
        return taggedString.tagValues.containsKey("date0");
    }

    public Date extractDocDate() {
        return (Date) taggedString.tagValues.get("date0");
    }

    public boolean isDocumentation() {
        return taggedString.tagValues.containsKey("cont0") || taggedString.tagValues.containsKey("date0");
    }
}
