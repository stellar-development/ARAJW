package net.theartex.mathhulkapi;

import lombok.Getter;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Alert {

    @Getter
    private final int id;
    @Getter
    private final String title;
    @Getter
    private final String message;
    @Getter
    private final Date date;
    @Getter
    private final User user;
    @Getter
    private boolean isNew;

    public Alert(int id, String title, String message, boolean isNew, String date, User user) throws ParseException {
        this.id = id;
        this.title = title;
        this.message = message;
        this.isNew = isNew;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date = formatter.parse(date);
        this.user = user;
    }

    public void markAsRead() throws IOException {
        if (!isNew)
            return;
        Map<String, String> args = new HashMap<>();
        args.put("key", user.getKey());
        args.put("id", String.valueOf(id));
        JSONObject jsonObject = new JSONObject(user.getApi().post("read", args));
        if (!jsonObject.getString("code").equalsIgnoreCase("0")) {
            new IOException(jsonObject.getString("message")).printStackTrace();
            return;
        }
        isNew = false;
    }
}
