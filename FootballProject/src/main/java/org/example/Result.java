package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class Result {
    private static final String API_URL = "https://jsonmock.hackerrank.com/api/football_matches";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    public static int getTotalGoals(String team, int year) {
        return getGoals(team, year, "team1") + getGoals(team, year, "team2");
    }

    private static int getGoals(String team, int year, String teamRole) {
        int currentPage = 1;
        int totalGoals = 0;
        int totalPages = 1;

        try {
            while (currentPage <= totalPages) {
                // Sử dụng URLEncoder để thay thế cho replace(" ", "%20") thủ công
                String encodedTeam = URLEncoder.encode(team, StandardCharsets.UTF_8);
                String url = String.format("%s?year=%d&%s=%s&page=%d", API_URL, year, teamRole, encodedTeam, currentPage);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
                    totalPages = jsonResponse.get("total_pages").getAsInt();
                    
                    JsonArray dataMatch = jsonResponse.getAsJsonArray("data");
                    for (int i = 0; i < dataMatch.size(); i++) {
                        JsonObject match = dataMatch.get(i).getAsJsonObject();
                        // Lấy giá trị bàn thắng tùy theo vai trò team1 hoặc team2
                        totalGoals += match.get(teamRole + "goals").getAsInt();
                    }
                    currentPage++;
                } else {
                    System.out.println("Bug");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalGoals;
    }

}