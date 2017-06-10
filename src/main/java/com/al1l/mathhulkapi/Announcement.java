package com.al1l.mathhulkapi;

import lombok.Getter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Announcement {

    @Getter
    private final int id;
    @Getter
    private final String message;
    @Getter
    private final Date date;

    public Announcement(int id, String message, String date) throws ParseException {
        this.id = id;
        this.message = message;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date = formatter.parse(date);
    }
}
