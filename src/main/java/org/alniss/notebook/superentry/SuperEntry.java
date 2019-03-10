package org.alniss.notebook.superentry;

import org.alniss.notebook.slackdata.SlackEntry;
import org.alniss.notebook.slackdata.SlackUser;
import org.alniss.notebook.slackdata.taggedstring.TaggedString;

import java.util.Date;
import java.util.Map;

public class SuperEntry {
    private String base;
    private String staging;
    private transient TaggedString published;
    private SlackUser author;  // warning: deserialization -> references not equal
    private String ts;
    private transient Date timestamp;
    private boolean requiresInspection = false;
    private String uniqueID;
    private SlackEntry conflict = null;

    public SuperEntry(SlackEntry slackEntry, Map<String, SlackUser> users) {
        base = slackEntry.text;
        pushText();
        publish();
        author = users.get(slackEntry.user);
        ts = slackEntry.ts;
        uniqueID = author.id + ts;
    }

    public void initAfterDeserialization() {
        initTimestamp();
        publish();
    }

    public boolean isUpdate(String text) {
        return !base.equals(text);
    }

    public void updateConflictStatus() {
        if (conflict == null)
            return;

        if (isUpdate(conflict.text)) {
            requiresInspection = true;
            base = conflict.text;
        } else {
            requiresInspection = false;
            conflict = null;
        }
    }

    public void publish() {
        published = new TaggedString(staging);
    }

    public void pushText() {
        staging = base;
        updateConflictStatus();
    }

    public String getStaging() {
        return staging;
    }

    public void setStaging(String text) {
        staging = text;
    }

    private void initTimestamp() {
        timestamp = new Date(Long.parseLong(ts.substring(0, ts.indexOf('.'))) * 1000);
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

        updateConflictStatus();
    }

    public boolean hasConflict() {
        return conflict != null;
    }

    public void setConflict(SlackEntry conflict) {
        this.conflict = conflict;
    }

    public TaggedString getPublished() {
        return published;
    }

    public SlackUser getAuthor() {
        return author;
    }

    public SlackEntry toSlackEntry() {
        return new SlackEntry(staging, author.id, ts);
    }
}
