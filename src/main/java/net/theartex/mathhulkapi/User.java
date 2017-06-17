package net.theartex.mathhulkapi;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    @Getter
    private final int id;
    @Getter
    private final String username;
    @Getter
    private final String role;
    @Getter
    private final String email;
    @Getter
    private final boolean banned;
    @Getter
    private final boolean active;
    @Getter
    private final String key;
    @Getter
    private final String password;
    @Getter
    private final MathhulkAPI api;

    public User(MathhulkAPI api, String username, String password) throws Exception {
        this.api = api;
        String passwordHashed = "";
        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        MessageDigest md;
        md = MessageDigest.getInstance("MD5");
        passwordHashed = DatatypeConverter.printHexBinary(md.digest(passwordBytes));
        Map<String, String> args = new HashMap<>();
        args.put("username", username);
        args.put("password", passwordHashed);
        JSONObject jsonObject = new JSONObject(api.post("login", args));
        if (!jsonObject.getString("code").equalsIgnoreCase("0")) {
            throw new IOException(jsonObject.getString("message"));
        }
        JSONObject data = jsonObject.getJSONObject("data");
        this.id = Integer.parseInt(data.getString("id"));
        this.username = data.getString("username");
        this.role = data.getString("role");
        this.email = data.getString("email");
        this.key = data.getString("key");
        this.password = data.getString("val");
        this.banned = data.getString("banned").equalsIgnoreCase("yes");
        this.active = data.getString("active").equalsIgnoreCase("yes");
    }

    public Alert sendAlert(String title, String message) throws IOException {
        Map<String, String> args = new HashMap<>();
        args.put("key", key);
        args.put("title", title);
        args.put("message", message);
        JSONObject json = new JSONObject(api.post("alert", args));
        return getAlert(Integer.parseInt(json.getJSONObject("data").getString("id")));
    }

    public Alert getAlert(int id) throws IOException {
        for (Alert a : getAlerts())
            if (a.getId() == id)
                return a;
        return null;
    }

    public List<Alert> getAlerts() throws IOException {
        Map<String, String> args = new HashMap<>();
        args.put("key", key);
        JSONObject jsonObject = new JSONObject(api.post("alerts", args));
        JSONArray jsonAlerts = jsonObject.getJSONArray("data");
        List<Alert> alerts = new ArrayList<>();
        for (Object obj : jsonAlerts) {
            JSONObject jsonAlert = (JSONObject) obj;
            try {
                Alert alert = new Alert(Integer.parseInt(jsonAlert.getString("id")),
                        jsonAlert.getString("title"),
                        jsonAlert.getString("message"),
                        jsonAlert.getString("new").equalsIgnoreCase("yes"),
                        jsonAlert.getString("trn_date"),
                        this);
                alerts.add(alert);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return alerts;
    }

    public List<Paste> getPastes() throws IOException {
        return api.getUserPastes(username);
    }

    public List<DNSRecord> getDNSRecords() throws IOException {
        return api.getUserDNSRecods(username);
    }
}
