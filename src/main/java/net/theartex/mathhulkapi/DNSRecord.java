package net.theartex.mathhulkapi;

import lombok.Getter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DNSRecord {

    @Getter
    private final String name;
    @Getter
    private final String id;
    @Getter
    private final String creator;
    @Getter
    private final String domain;
    @Getter
    private final Date date;
    @Getter
    private final MathhulkAPI api;

    public DNSRecord(MathhulkAPI api, String id, String name, String creator, String domain, String date) throws ParseException {
        this.name = name;
        this.id = id;
        this.creator = creator;
        this.domain = domain;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date = formatter.parse(date);
        this.api = api;
    }


}
