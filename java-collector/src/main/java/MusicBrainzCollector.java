import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.HashSet;

public class MusicBrainzCollector {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Integer> categories = new LinkedHashMap<>();

        // using these to get an variety of songs
        categories.put("rock", 143);
        categories.put("pop", 143);
        categories.put("hip-hop", 143);
        categories.put("jazz", 143);
        categories.put("electronic", 143);
        categories.put("classical", 143);
        categories.put("folk", 142);

        Set<String> collectedIds = new HashSet<>();

        try (FileWriter writer = new FileWriter("music_dataset.csv")) {
            writer.write("mbid,title,artist,length_ms,first_release_date,category\n");

            for (Map.Entry<String, Integer> entry : categories.entrySet()) {
                String category = entry.getKey();
                int target = entry.getValue();

                int collected = 0;
                int offset = 0;

                System.out.println("Collecting category: " + category);

                while (collected < target) {
                    String query = URLEncoder.encode(
                            "tag:" + category,
                            StandardCharsets.UTF_8
                    );

                    String url = "https://musicbrainz.org/ws/2/recording/"
                            + "?query=" + query
                            + "&fmt=json"
                            + "&limit=100"
                            + "&offset=" + offset;

                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .header("User-Agent",
                                    "MoodMatch/0.1 (sohniballard@tamu.edu)")
                            .header("Accept", "application/json")
                            .GET()
                            .build();

                    HttpResponse<String> response = null;

                    for (int attempt = 1; attempt <= 4; attempt++) {
                        response = client.send(
                                request,
                                HttpResponse.BodyHandlers.ofString()
                        );

                        if (response.statusCode() == 200) {
                            break;
                        }

                        System.out.println("Request failed with status "
                                + response.statusCode()
                                + ". Attempt " + attempt + " of 4.");

                        if (response.statusCode() != 503
                                && response.statusCode() != 429
                                && response.statusCode() < 500) {
                            break;
                        }

                        if (attempt < 4) {
                            Thread.sleep(2000L * attempt);
                        }
                    }

                    if (response == null || response.statusCode() != 200) {
                        System.out.println("Skipping category after failed requests.");
                        break;
                    }

                    JsonNode root = mapper.readTree(response.body());
                    JsonNode recordings = root.path("recordings");

                    if (!recordings.isArray() || recordings.isEmpty()) {
                        break;
                    }

                    for (JsonNode recording : recordings) {
                        if (collected >= target) {
                            break;
                        }

                        String mbid = recording.path("id").asText();

                        if (mbid.isEmpty() || !collectedIds.add(mbid)) {
                            continue;
                        }

                        String title = recording.path("title").asText();

                        String artist = recording.path("artist-credit")
                                .path(0)
                                .path("name")
                                .asText("Unknown");

                        long length = recording.path("length").asLong(0);
                        String releaseDate = recording
                                .path("first-release-date").asText();

                        writer.write(
                                csv(mbid) + "," +
                                csv(title) + "," +
                                csv(artist) + "," +
                                length + "," +
                                csv(releaseDate) + "," +
                                csv(category) + "\n"
                        );

                        collected++;
                    }

                    offset += recordings.size();

                    System.out.println(category + ": "
                            + collected + "/" + target);

                    if (recordings.size() < 100) {
                        break;
                    }

                    Thread.sleep(1100);
                }

                System.out.println("Finished " + category
                        + ": collected " + collected + " recordings.");
                Thread.sleep(1100);
            }
        }

        System.out.println("Dataset collection finished!");
        System.out.println("Total unique recordings: " + collectedIds.size());
    }

    private static String csv(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }




}
