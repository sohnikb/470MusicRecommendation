import java.util.HashMap;
import java.util.Map;

public class MoodScorer {

    // categorizes mood keywords
    public static Map<String, Integer> analyzeMood(String description) {

        Map<String, Integer> moodScores = new HashMap<>();

        moodScores.put("happy", 0);
        moodScores.put("sad", 0);
        moodScores.put("relaxed", 0);
        moodScores.put("energetic", 0);
        moodScores.put("romantic", 0);
        moodScores.put("angry", 0);
        moodScores.put("focused", 0);

        String text = description.toLowerCase();

        if (text.contains("happy") || text.contains("cheerful") || text.contains("joyful")) {
            moodScores.put("happy", 1);
        }

        if (text.contains("sad") || text.contains("lonely") || text.contains("heartbroken")) {
            moodScores.put("sad", 1);
        }

        if (text.contains("calm") || text.contains("peaceful") || text.contains("mellow") || text.contains("relaxing")) {
            moodScores.put("relaxed", 1);
        }

        if (text.contains("energetic") || text.contains("upbeat") || text.contains("intense")) {
            moodScores.put("energetic", 1);
        }

        if (text.contains("love") || text.contains("romantic") || text.contains("intimate")) {
            moodScores.put("romantic", 1);
        }

        if (text.contains("angry") || text.contains("aggressive") || text.contains("mad")) {
            moodScores.put("angry", 1);
        }

        if (text.contains("studying") || text.contains("concentration") || text.contains("focus")) {
            moodScores.put("focused", 1);
        }

        return moodScores;
    }

    public static int scoreSong(Song song, Map<String, Integer> moods) {
        int score = 0;

        String genre = song.getCategory().toLowerCase();

        if (moods.get("energetic") == 1) {
            if (genre.equals("rock") || genre.equals("electronic") || genre.equals("hip-hop")) {
                score += 2;
            }
        }

        if (moods.get("relaxed") == 1) {
            if (genre.equals("jazz") || genre.equals("classical") || genre.equals("folk")) {
                score += 2;
            }
        }

        if (moods.get("happy") == 1) {
            if (genre.equals("pop") || genre.equals("folk")) {
                score += 2;
            }
        }

        if (moods.get("sad") == 1) {
            if (genre.equals("jazz") || genre.equals("classical")) {
                score += 2;
            }
        }

        return score;
    }

    public static void main(String[] args) {
        String description = "I want happy, upbeat music.";

        Map<String, Integer> results = analyzeMood(description);

        System.out.println("User description: " + description);
        System.out.println("Detected moods:");

        for (Map.Entry<String, Integer> entry : results.entrySet()) {
            if (entry.getValue() > 0) {
                System.out.println(entry.getKey());
            }
        }
    }



}