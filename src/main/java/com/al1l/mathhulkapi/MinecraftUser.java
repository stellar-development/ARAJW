package com.al1l.mathhulkapi;

import lombok.Getter;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MinecraftUser {

    @Getter
    private final String username;
    @Getter
    private final UUID uuid;
    @Getter
    private final MathhulkAPI api;

    public MinecraftUser(MathhulkAPI api, UUID uuid) {
        Map<String, String> args = new HashMap<>();
        args.put("uuid", uuid.toString().replaceAll("-", ""));
        JSONObject jsonObject = new JSONObject(api.post("username", args));
        JSONObject json = jsonObject.getJSONObject("data");
        this.username = json.getString("name");
        this.uuid = api.shortUUIDtoUUID(json.getString("id"));
        this.api = api;
    }

    public MinecraftUser(MathhulkAPI api, String username) {
        Map<String, String> args = new HashMap<>();
        args.put("username", username);
        JSONObject jsonObject = new JSONObject(api.post("username", args));
        JSONObject json = jsonObject.getJSONObject("data");
        this.username = json.getString("name");
        this.uuid = api.shortUUIDtoUUID(json.getString("id"));
        this.api = api;
    }

    public Map<String, Date> getNameHistory() {
        Map<String, Date> nameHistory = new HashMap<>();
        Map<String, String> args = new HashMap<>();
        args.put("uuid", uuid.toString().replaceAll("-", ""));
        JSONObject jsonObject = new JSONObject(api.post("history", args));
        for (Object obj : jsonObject.getJSONArray("data")) {
            JSONObject json = (JSONObject) obj;
            Long date = null;
            if (json.has("changedToAt"))
                date = json.getLong("changedToAt");
            nameHistory.put(json.getString("name"), new Date(date));
        }
        return nameHistory;
    }
}
