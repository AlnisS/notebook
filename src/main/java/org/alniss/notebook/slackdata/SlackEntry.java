package org.alniss.notebook.slackdata;

import org.alniss.notebook.slackdata.taggedstring.TaggedString;

import java.util.Date;

/**
 * Represents a Slack message.
 */
public class SlackEntry {
    /**
     * Slack message type (purpose is unknown).
     */
    public String type;
    /**
     * Raw text from Slack.
     */
    public String text;
    /**
     * Internal Slack user id.
     */
    public String user;
    /**
     * Internal Slack sent date.
     */
    public String ts;
    /**
     * Attached files (currently unimplemented).
     */
    public SlackFile[] files;
    /**
     * TaggedString which can lookup key/value pairs specified in the message.
     */
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

    /**
     * Creates a Slack entry from the raw data from the Slack JSON dump.
     * @param text message contents.
     * @param user internal Slack id for message author.
     * @param ts internal Slack message timestamp.
     */
    public SlackEntry(String text, String user, String ts) {
        this.type = "message";
        this.text = text;
        this.user = user;
        this.ts = ts;
        tagString();
    }

    /**
     * Sets up the taggedString from the raw message text.
     */
    public void tagString() {
        taggedString = new TaggedString(text);
    }

    /**
     * @return whether this message is the first in a chain of documentation.
     */
    public boolean isStart() {
        return taggedString.hasTag("date0");
    }

    /**
     * @return date stated in message in [MM-DD] format.
     */
    public Date extractDocDate() {
        return (Date) taggedString.getTagValue("date0");
    }

    /**
     * @return whether this is documentation (start or continuation of chain).
     */
    public boolean isDocumentation() {
        return taggedString.hasTag("cont0") || taggedString.hasTag("date0");
    }
}
