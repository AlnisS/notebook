package org.alniss.notebook.slackdata;

/**
 * Represents a Slack user by holding the person's internal Slack id and real
 * name. These are serialized and deserialized without regard to sanity, so make
 * sure to check for field equality rather than just == for the whole thing.
 */
public class SlackUser {
    /**
     * Internal Slack user id.
     */
    public String id;
    /**
     * Real name of the user.
     */
    public String real_name;
}
