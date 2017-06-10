package com.al1l.mathhulkapi;

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
    private final String APIKey;
    private final String APIURL;

    public MathhulkAPI(String APIKey, String APIURL) {
        this.APIKey = APIKey;
        if (APIURL == null || APIURL.trim() == "" || APIURL.trim() == " ") {
            this.APIURL = "https://www.theartex.net/cloud/api/";
        } else {
            this.APIURL = APIURL;
        }
    }

    // Information API

    public List<Announcement> getAnnouncements() {
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

    public List<Paste> getUserPastes(String username) {
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

    // Http

    public String post(String section, Map<String, String> arguments) {
        System.out.println("Posting to: " + section + "...");
        try {
            arguments.put("sec", section);
            arguments.put("APIkey", APIKey);
            URL url = new URL(APIURL);
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection) con;
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("User-Agent", "Java Mathhulk API by AL_1 / 0.0.1");
            http.setRequestProperty("sec", section);
            StringJoiner sj = new StringJoiner("&");
            for (Map.Entry<String, String> entry : arguments.entrySet())
                sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
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
            System.out.println("Returned: " + rtn);
            return rtn;
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"status\":\"error\",\"code\":\"404\",\"message\":\"Post failed (" + e.getMessage() + ")\"}";
        }
    }
}
