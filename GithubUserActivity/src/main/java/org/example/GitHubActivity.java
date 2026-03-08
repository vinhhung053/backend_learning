package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GitHubActivity {

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Usage: github-activity <username>");
            return;
        }

        String username = args[0];

        try {

            String apiUrl = "https://api.github.com/users/" + username + "/events";

            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int status = conn.getResponseCode();

            if (status != 200) {
                System.out.println("Error: Unable to fetch activity. HTTP Code: " + status);
                return;
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            parseEvents(response.toString());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void parseEvents(String json) {

        String[] events = json.split("\\{");

        for (String event : events) {

            String type = extract(event, "\"type\":\"", "\"");
            String repo = extract(event, "\"name\":\"", "\"");

            if (type.equals("PushEvent")) {

                String commits = extract(event, "\"size\":", ",");
                System.out.println("- Pushed " + commits + " commits to " + repo);

            } else if (type.equals("IssuesEvent")) {

                System.out.println("- Opened an issue in " + repo);

            } else if (type.equals("WatchEvent")) {

                System.out.println("- Starred " + repo);

            } else if (type.equals("ForkEvent")) {

                System.out.println("- Forked " + repo);

            } else if (type.equals("PullRequestEvent")) {

                System.out.println("- Opened a pull request in " + repo);

            } else if (type.equals("CreateEvent")) {

                System.out.println("- Created something in " + repo);

            } else if (type.equals("PublicEvent")) {

                System.out.println("- Made repository public: " + repo);

            }
        }
    }

    static String extract(String text, String start, String end) {

        try {

            int i = text.indexOf(start);

            if (i == -1) return "";

            int j = text.indexOf(end, i + start.length());

            return text.substring(i + start.length(), j);

        } catch (Exception e) {
            return "";
        }
    }
}