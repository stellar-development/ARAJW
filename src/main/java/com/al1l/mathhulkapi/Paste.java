package com.al1l.mathhulkapi;

import lombok.Getter;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Paste {

    @Getter
    private final String name;
    @Getter
    private final String id;
    @Getter
    private final String creator;
    @Getter
    private final String url;
    @Getter
    private final Date date;
    @Getter
    private final MathhulkAPI api;

    public Paste(MathhulkAPI api, String id) {
        Map<String, String> args = new HashMap<>();
        args.put("id", id);
        JSONObject jsonObject = new JSONObject(api.post("paste", args));
        JSONObject json = jsonObject.getJSONObject("data");
        this.name = json.getString("name");
        this.id = json.getString("paste");
        this.creator = json.getString("username");
        this.url = json.getString("url");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = formatter.parse(json.getString("trn_date"));
        } catch (ParseException e) {
            date = new Date(System.currentTimeMillis());
            e.printStackTrace();
        }
        this.date = date;
        this.api = api;
    }

    public Paste(MathhulkAPI api, String id, String name, String creator, String url, String date) throws ParseException {
        this.name = name;
        this.id = id;
        this.creator = creator;
        this.url = url;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date = formatter.parse(date);
        this.api = api;
    }


}
