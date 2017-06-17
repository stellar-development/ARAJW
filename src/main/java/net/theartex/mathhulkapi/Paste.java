package net.theartex.mathhulkapi;

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
