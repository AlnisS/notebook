package org.alniss.notebook.slackdata;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlackEntry {
    public String type;
    public String text;
    public String user;
    public String ts;
    public String client_msg_id;
    public SlackFile[] files;
    public static final String dateRegex = "\\[(\\d|\\d\\d)\\-(\\d|\\d\\d)\\]";

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

    public boolean isStart() {
        return extractDocDate() != null;
    }

    public Date extractDocDate() {
        //TODO: add explicit year too
        Pattern pattern = Pattern.compile(dateRegex);
        Matcher matcher = pattern.matcher(text);
        String dateString = null;

        if (matcher.find())
            dateString = matcher.group();
        else return null;

        int month = Integer.parseInt(dateString.substring(1, dateString.indexOf("-")));
        int day = Integer.parseInt(dateString.substring(dateString.indexOf("-") + 1, dateString.indexOf("]")));
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), month - 1, day);
        if (!calendar.before(Calendar.getInstance()))  //TODO: make this logic dependent on explicit season start
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
        return calendar.getTime();
    }

    public boolean isDocumentation() {
        return text.indexOf("[cont]") != -1 || extractDocDate() != null;
    }
}
