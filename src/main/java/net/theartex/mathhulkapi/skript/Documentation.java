package net.theartex.mathhulkapi.skript;

import net.theartex.mathhulkapi.MathhulkAPI;
import lombok.Getter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Documentation {

    @Getter
    private final int id;
    @Getter
    private final String name;
    @Getter
    private final String type;
    @Getter
    private final String description;
    @Getter
    private final String addonName;
    @Getter
    private final String[] plugins;
    @Getter
    private final String pattern;
    @Getter
    private final String example;
    @Getter
    private final Date date;
    @Getter
    private final MathhulkAPI api;

    public Documentation(MathhulkAPI api, String addonName, int id, String name, String type, String description, String[] plugins, String pattern, String example, String date) throws ParseException {
        this.name = name;
        this.id = id;
        this.type = type;
        this.plugins = plugins;
        this.pattern = pattern;
        this.example = example;
        this.description = description;
        this.addonName = addonName;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date = formatter.parse(date);
        this.api = api;
    }

    public Addon getAddon() throws IOException {
        return api.getAddon(addonName);
    }

    public void delete() throws IOException {
        api.deleteDocumentation(id);
    }
}
