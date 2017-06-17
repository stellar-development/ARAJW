package com.al1l.mathhulkapi.skript;

import com.al1l.mathhulkapi.MathhulkAPI;
import lombok.Getter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Addon {

    @Getter
    private final String author;
    @Getter
    private final Date date;
    @Getter
    private final MathhulkAPI api;
    @Getter
    private boolean enabled;
    @Getter
    private String name;
    @Getter
    private String version;

    public Addon(MathhulkAPI api, String name, String version, String author, boolean enabled, String date) throws ParseException {
        this.name = name;
        this.version = version;
        this.author = author;
        this.enabled = enabled;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date = formatter.parse(date);
        this.api = api;
    }

    public List<Documentation> getDocumentations() throws IOException {
        return api.getAddonDocumentation(name);
    }

}
