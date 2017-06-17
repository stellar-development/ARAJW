package net.theartex.mathhulkapi;

import net.theartex.mathhulkapi.skript.Documentation;

import java.io.IOException;

public class TestMain {

    private static final boolean LOGIN = false;

    public static void main(String args[]) {
        if (args.length != 4)
            return;
        MathhulkAPI api = new MathhulkAPI(args[0], "");
        api.setDebugMode(true);
        User user = null;
        if (LOGIN) {
            try {
                user = new User(api, args[2], args[3]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (user == null)
                return;
        }


        String[] plugins = {};
        try {
            Documentation doc = api.newDocumentation("name", "description", plugins, "effect", "pattern", "example", "Test Java Hook");
            doc.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }


        /*
        List<Addon> addons = null;
        try {
            addons = api.getAddons();
        } catch (IOException e) {
            e.printStackTrace();
        }
        api.setDebugMode(false);
        System.out.println("Addons:");
        for (Addon addon : addons) {
            System.out.println(addon.getName() + ":");
            System.out.println("  name: " + addon.getName());
            System.out.println("  author: " + addon.getAuthor());
            System.out.println("  version: " + addon.getVersion());
            System.out.println("  date: \"" + addon.getDate().toString() + "\"");
            try {
                List<Documentation> docs = addon.getDocumentations();
                System.out.println("  docs:");
                for (Documentation doc : docs) {
                    System.out.println("    '" + doc.getId() + "':");
                    System.out.println("      id: " + doc.getId());
                    System.out.println("      name: \"" + doc.getName() + "\"");
                    System.out.println("      type: " + doc.getType());
                    System.out.println("      description: \"" + doc.getDescription() + "\"");
                    System.out.println("      pattern: \"" + doc.getPattern() + "\"");
                    System.out.println("      example: \"" + doc.getExample().replaceAll("\n", "").replaceAll("\r", "") + "\"");
                    System.out.println("      date: \"" + doc.getDate() + "\"");
                    if (doc.getPlugins().length > 0) {
                        System.out.println("      plugins:");
                        for (String str : doc.getPlugins()) {
                            System.out.println("        - " + str);
                        }
                    }
                }
            } catch (Exception e) {

            }
        }
        */

    }
}
