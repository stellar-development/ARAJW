package net.theartex.mathhulkapi;

import net.theartex.mathhulkapi.skript.Addon;
import net.theartex.mathhulkapi.skript.Documentation;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;

public class MathhulkAPI {
    @Getter
    private final String DEV_KEY;
    @Getter
    private final String API_URL;
    @Setter
    @Getter
    private boolean debugMode = false;

    public MathhulkAPI(String DEV_KEY, String APIURL) {
        this.DEV_KEY = DEV_KEY;
        if (APIURL == null || APIURL.trim() == "" || APIURL.trim() == " ") {
            this.API_URL = "https://www.theartex.net/cloud/api/";
        } else {
            this.API_URL = APIURL;
        }
    }

    // Information API

    public List<Announcement> getAnnouncements() throws IOException {
        JSONObject jsonObject = new JSONObject(post("announcements", new HashMap<>()));
        List<Announcement> ancs = new ArrayList<>();
        for (Object obj : jsonObject.getJSONArray("data")) {
            try {
                JSONObject json = (JSONObject) obj;
                Announcement anc = new Announcement(Integer.parseInt(json.getString("id")), json.getString("message"), json.getString("trn-date"));
                ancs.add(anc);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return ancs;
    }

    // Minecraft API

    public UUID shortUUIDtoUUID(String uuid) {
        if (uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"))
            return UUID.fromString("uuid");
        if (!uuid.matches("[0-9a-f]{8}[0-9a-f]{4}[0-9a-f]{4}[0-9a-f]{4}[0-9a-f]{12}")) {
            new IllegalArgumentException("Invalid short UUID string: " + uuid).printStackTrace();
            return null;
        }
        StringBuilder sb = new StringBuilder(uuid);
        final String res = sb.insert(8, "-").insert(13, "-").insert(18, "-").insert(23, "-").toString();
        return UUID.fromString(res);
    }

    // Codity API

    public List<Paste> getUserPastes(String username) throws IOException {
        List<Paste> pastes = new ArrayList<>();
        Map<String, String> args = new HashMap<>();
        args.put("username", username);
        JSONObject jsonObject = new JSONObject(post("pastes", args));
        for (Object obj : jsonObject.getJSONArray("data")) {
            JSONObject json = (JSONObject) obj;
            try {
                Paste paste = new Paste(this, json.getString("paste"), json.getString("name"), username, json.getString("url"), json.getString("trn_date"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return pastes;
    }

    public Paste getPaste(String id) throws IOException {
        Map<String, String> args = new HashMap<>();
        args.put("id", id);
        JSONObject jsonObject = new JSONObject(post("paste", args));
        JSONObject json = jsonObject.getJSONObject("data");
        Paste paste = null;
        try {
            paste = new Paste(this, id, json.getString("name"), json.getString("username"), json.getString("url"), json.getString("trn_date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return paste;
    }

    // MH-DNS API

    public List<DNSRecord> getUserDNSRecods(String username) throws IOException {
        List<DNSRecord> records = new ArrayList<>();
        Map<String, String> args = new HashMap<>();
        args.put("username", username);
        JSONObject jsonObject = new JSONObject(post("records", args));
        for (Object obj : jsonObject.getJSONArray("data")) {
            JSONObject json = (JSONObject) obj;
            try {
                DNSRecord record = new DNSRecord(this, "UNKNOWN", json.getString("name"), username, json.getString("domain"), json.getString("trn_date"));
                records.add(record);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return records;
    }

    public DNSRecord getDNSRecod(String id) throws IOException {
        Map<String, String> args = new HashMap<>();
        args.put("id", id);
        JSONObject jsonObject = new JSONObject(post("paste", args));
        JSONObject json = jsonObject.getJSONObject("data");
        DNSRecord record = null;
        try {
            record = new DNSRecord(this, id, json.getString("name"), json.getString("username"), json.getString("url"), json.getString("trn_date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return record;
    }

    // Skript API (This is going to get messy!)

    public List<Addon> getAddons() throws IOException {
        List<Addon> addons = new ArrayList<>();
        Map<String, String> args = new HashMap<>();
        JSONObject jsonObject = new JSONObject(skriptPost("addons", args));
        for (Object obj : jsonObject.getJSONArray("data")) {
            JSONObject json = (JSONObject) obj;
            boolean enabled = false;
            if (json.getString("status").equalsIgnoreCase("on"))
                enabled = true;
            Addon addon = null;
            try {
                addon = new Addon(this, json.getString("name"), json.getString("version"), json.getString("author"), enabled, json.getString("trn_date"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            addons.add(addon);
        }
        return addons;
    }

    public Addon getAddon(String addonName) throws IOException {
        Map<String, String> args = new HashMap<>();
        args.put("name", addonName);
        JSONObject jsonObject = new JSONObject(skriptPost("addon", args));
        JSONObject json = jsonObject.getJSONObject("data");
        boolean enabled = false;
        if (json.getString("status").equalsIgnoreCase("on"))
            enabled = true;
        Addon addon = null;
        try {
            addon = new Addon(this, json.getString("name"), json.getString("version"), json.getString("author"), enabled, json.getString("trn_date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return addon;
    }

    public List<Documentation> getAddonDocumentation(String addonName) throws IOException {
        List<Documentation> docs = new ArrayList<>();
        Map<String, String> args = new HashMap<>();
        args.put("name", addonName);
        JSONObject jsonObject = new JSONObject(skriptPost("docs", args));
        for (Object obj : jsonObject.getJSONArray("data")) {
            JSONObject json = (JSONObject) obj;
            try {
                Documentation doc = new Documentation(this, addonName, Integer.parseInt(json.getString("id")), json.getString("name"), json.getString("type"),
                        json.getString("description"), json.getString("plugins").split(", "), json.getString("pattern"), json.getString("example"),
                        json.getString("trn_date"));
                docs.add(doc);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return docs;

    }

    public Documentation getDocumentation(String addon, int id) throws IOException {
        for (Documentation doc : getAddonDocumentation(addon))
            if (doc.getId() == id)
                return doc;
        return null;
    }

    public List<Documentation> searchDocumentation(String query) throws IOException {
        List<Documentation> docs = new ArrayList<>();
        Map<String, String> args = new HashMap<>();
        args.put("search", query);
        JSONObject jsonObject = new JSONObject(skriptPost("search", args));
        for (Object obj : jsonObject.getJSONArray("data")) {
            JSONObject json = (JSONObject) obj;
            try {
                Documentation doc = new Documentation(this, json.getString("addon"), Integer.parseInt(json.getString("id")), json.getString("name"), json.getString("type"),
                        json.getString("description"), json.getString("plugins").split(", "), json.getString("pattern"), json.getString("example"),
                        json.getString("trn_date"));
                docs.add(doc);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return docs;

    }

    public Documentation newDocumentation(String name, String description, String[] plugins, String type, String pattern, String example, String addon) throws IOException {
        Map<String, String> args = new HashMap<>();
        args.put("name", name);
        args.put("description", description);
        args.put("doc_type", type);
        args.put("pattern", pattern);
        args.put("example", example);
        args.put("addon", addon);
        if (plugins.length == 0) {
            plugins = new String[1];
            plugins[0] = "None";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < plugins.length; i++) {
            sb.append(plugins[i]);
            if (i != plugins.length - 1)
                sb.append(", ");
        }
        args.put("plugins", sb.toString());
        JSONObject jsonObject = new JSONObject(skriptPost("new", args));
        int id = Integer.parseInt(jsonObject.getJSONObject("data").getString("id"));
        return getDocumentation(addon, id);
    }

    public void deleteDocumentation(int id) throws IOException {
        Map<String, String> args = new HashMap<>();
        args.put("id", String.valueOf(id));
        skriptPost("delete", args);
    }

    // Http

    public String post(String section, Map<String, String> arguments) throws IOException {
        return post(new URL(API_URL), section, arguments);
    }

    public String skriptPost(String section, Map<String, String> arguments) throws IOException {
        String url = API_URL;
        if (!url.endsWith("/"))
            url += "/";
        url += "skript/";
        return post(new URL(url), section, arguments);
    }

    private String post(URL url, String section, Map<String, String> arguments) throws IOException {
        if (debugMode)
            System.out.println("POST   : \"" + section + "\" at \"" + url.toString() + "\"");
        arguments.put("sec", section);
        arguments.put("dev_key", DEV_KEY);
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection) con;
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("User-Agent", "Java Mathhulk API by AL_1 / 0.0.1");
        http.setRequestProperty("sec", section);
        StringJoiner sj = new StringJoiner("&");
        for (Map.Entry<String, String> entry : arguments.entrySet())
            sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));

        if (debugMode)
            System.out.println("OUT    : \"" + sj.toString() + "\"");
        byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
        int length = out.length;
        http.setFixedLengthStreamingMode(length);
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        http.connect();
        try (OutputStream os = http.getOutputStream()) {
            os.write(out);
        }
        InputStream in = http.getInputStream();
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(
                new InputStreamReader(in, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        String rtn = textBuilder.toString();
        if (debugMode)
            System.out.println("RETURN : \"" + rtn + "\"");
        JSONObject jsonObject = new JSONObject(rtn);
        if (!jsonObject.getString("code").equalsIgnoreCase("0")) {
            throw new IOException(jsonObject.getString("message"));
        }
        return rtn;
    }
}
