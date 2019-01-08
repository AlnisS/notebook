package org.alniss.notebook.superentry;

import org.alniss.notebook.slackdata.SlackEntry;
import org.alniss.notebook.slackdata.SlackUser;
import org.alniss.notebook.slackdata.taggedstring.TaggedString;

import java.util.Date;
import java.util.Map;

public class SuperEntry {
    private String originalText;
    private String editText;
    private transient TaggedString outText;
    private SlackUser author;  // warning: deserialization -> references not equal
    private String timestampString;
    private transient Date timestamp;
    private boolean requiresInspection = false;
    private String uniqueID;
    private SlackEntry conflict = null;

    public SuperEntry(SlackEntry slackEntry, Map<String, SlackUser> users) {
        originalText = slackEntry.text;
        overwriteEditWithOriginal();
        pushEditToOut();
        author = users.get(slackEntry.user);
        timestampString = slackEntry.ts;
        uniqueID = author.id + timestampString;  //this is janky
    }

    public void initAfterDeserialization() {
        initTimestamp();
        pushEditToOut();
    }

    public boolean isUpdate(String text) {
        return !originalText.equals(text);
    }

    public void update(String text) {
        requiresInspection = isUpdate(text);
        originalText = text;
    }

    public void update() {
        if (conflict == null)
            return;
        update(conflict.text);
    }

    public void pushEditToOut() {
        outText = new TaggedString(editText);
    }

    public void overwriteEditWithOriginal() {
        editText = originalText;
    }

    public String getEditText() {
        return editText;
    }

    public void setEditText(String text) {
        editText = text;
    }

    private void initTimestamp() {
        timestamp = new Date(Long.parseLong(timestampString.substring(0, timestampString.indexOf('.'))) * 1000);
    }

    public Date getTimestamp() {
        if (timestamp == null)
            initTimestamp();
        return timestamp;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void simpleMaybeResolveConflict() {
        if (!hasConflict())
            return;

        if (!isUpdate(conflict.text)) {
            conflict = null;
            return;
        }
    }

    public boolean hasConflict() {
        return conflict != null;
    }

    public void setConflict(SlackEntry conflict) {
        this.conflict = conflict;
    }

    public TaggedString getOutText() {
        return outText;
    }

    public SlackUser getAuthor() {
        return author;
    }

    public SlackEntry toSlackEntry() {
        return new SlackEntry(editText, author.id, timestampString);
    }
}
