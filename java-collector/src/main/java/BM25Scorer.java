import java.util.*;

public class BM25Scorer {

    private static final double K1 = 1.5;
    private static final double B = 0.75;

    // Converts text into searchable words
    private static List<String> tokenize(String text) {
        String cleaned = text.toLowerCase()
                .replaceAll("[^a-z0-9]+", " ")
                .trim();

        if (cleaned.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> words = Arrays.asList(cleaned.split("\\s+"));

        List<String> filteredWords = new ArrayList<>();

        for (String word : words) {
            if (!STOP_WORDS.contains(word)) {
                filteredWords.add(word);
            }
        }

        return filteredWords;
    }

    // Creates searchable text for each song
    public static String createDocument(Song song) {
        return song.getTitle() + " "
                + song.getArtist() + " "
                + song.getCategory();
    }

    
    public static double score(
            String query,
            String document,
            List<String> allDocuments) {

        List<String> queryTerms = tokenize(query);
        List<String> documentTerms = tokenize(document);

        int documentLength = documentTerms.size();

        // Calculate average document length
        double totalLength = 0;

        for (String doc : allDocuments) {
            totalLength += tokenize(doc).size();
        }

        double averageLength = totalLength / allDocuments.size();

        double score = 0;

        for (String term : queryTerms) {

            // Count occurrences in this document
            int termFrequency = Collections.frequency(
                    documentTerms, term);

            // Count documents containing this term
            int documentFrequency = 0;

            for (String doc : allDocuments) {
                if (tokenize(doc).contains(term)) {
                    documentFrequency++;
                }
            }

            // Calculate inverse document frequency
            double idf = Math.log(
                    1 + (allDocuments.size() - documentFrequency + 0.5)
                    / (documentFrequency + 0.5)
            );

            // Calculate BM25 term score
            double numerator = termFrequency * (K1 + 1);

            double denominator = termFrequency
                    + K1 * (1 - B + B * documentLength / averageLength);

            if (termFrequency > 0) {
                score += idf * numerator / denominator;
            }
        }

        return score;
    }

    
    private static final Set<String> STOP_WORDS = new HashSet<>(
        Arrays.asList(
            "i", "want", "a", "an", "the", "to",
            "for", "and", "or", "of", "in", "on",
            "with", "me", "my", "some", "music", "please",
            "would", "like", "similar"
        )
    );


}
