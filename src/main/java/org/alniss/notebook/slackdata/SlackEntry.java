package org.alniss.notebook.slackdata;

import org.alniss.notebook.slackdata.taggedstring.TaggedString;

import java.util.Date;

public class SlackEntry {
    public String type;
    public String text;
    public String user;
    public String ts;
    public SlackFile[] files;
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

    public SlackEntry(String text, String user, String ts) {
        this.type = "message";
        this.text = text;
        this.user = user;
        this.ts = ts;
        tagString();
    }

    public void tagString() {
        taggedString = new TaggedString(text);
    }

    public boolean isStart() {
        return taggedString.hasTag("date0");
    }

    public Date extractDocDate() {
        return (Date) taggedString.getTagValue("date0");
    }

    public boolean isDocumentation() {
        return taggedString.hasTag("cont0") || taggedString.hasTag("date0");
    }
}
