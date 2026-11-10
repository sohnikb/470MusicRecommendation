import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MusicRecommendationEngine {
    public static void main(String[] args) {

    String description = "I want rock music.";

        recommendBM25(description);
    }

    // pulls songs from the csv file
    public static List<Song> loadSongs(String filename) {
        List<Song> songs = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(filename))) {

            String line = reader.readLine(); // skip csv header

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                String mbid = data[0].replace("\"", "");
                String title = data[1].replace("\"", "");
                String artist = data[2].replace("\"", "");
                long length = Long.parseLong(data[3]);
                String releaseDate = data[4].replace("\"", "");
                String category = data[5].replace("\"", "");

                Song song = new Song(
                        mbid, title, artist, length, releaseDate, category
                );

                songs.add(song);
            }

        } catch (Exception e) {
            System.out.println("Error loading songs: " + e.getMessage());
        }

        return songs;
    }

    public static void recommend(String description) {

        List<Song> songs = loadSongs("music_dataset.csv");

        Map<String, Integer> moods =
                MoodScorer.analyzeMood(description);

        songs.sort(Comparator.comparingInt(
                (Song song) -> MoodScorer.scoreSong(song, moods)
        ).reversed());

        System.out.println("\nYour description: " + description);
        System.out.println("\nTop 5 recommendations:");

        for (int i = 0; i < Math.min(5, songs.size()); i++) {
            Song song = songs.get(i);

            System.out.println(
                    (i + 1) + ". " + song.getTitle()
                    + " - " + song.getArtist()
                    + " | Score: "
                    + MoodScorer.scoreSong(song, moods)
            );
        }
    }

    
    public static void recommendBM25(String description) {

        List<Song> songs = loadSongs("music_dataset.csv");

        // Convert every song into a searchable document
        List<String> documents = new ArrayList<>();

        for (Song song : songs) {
            documents.add(BM25Scorer.createDocument(song));
        }

        // Store song scores
        Map<Song, Double> scores = new HashMap<>();

        for (int i = 0; i < songs.size(); i++) {

            double score = BM25Scorer.score(
                    description,
                    documents.get(i),
                    documents
            );

            scores.put(songs.get(i), score);
        }

        // Sort songs by BM25 score
        songs.sort((song1, song2) ->
                Double.compare(scores.get(song2), scores.get(song1))
        );

        System.out.println("\nYour description: " + description);
        System.out.println("\nTop 5 BM25 recommendations:");

        for (int i = 0; i < Math.min(5, songs.size()); i++) {

            Song song = songs.get(i);

            System.out.println(
                (i + 1) + ". " + song.getTitle()
                + " - " + song.getArtist()
                + " | Category: " + song.getCategory()
                + " | BM25 Score: " + scores.get(song)
            );
        }
    }




}