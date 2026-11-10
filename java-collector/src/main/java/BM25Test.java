import java.util.*;

public class BM25Test {

    public static void main(String[] args) {

        // A small sample dataset of five songs
        List<String> documents = Arrays.asList(
            "rock guitar energetic",
            "rock guitar alternative",
            "jazz piano relaxing",
            "pop dance cheerful",
            "classical violin instrumental"
        );

        // User's search query
        String query = "rock guitar";

        System.out.println("BM25 Test");
        System.out.println("Query: " + query);
        System.out.println("\nResults:");

        double[] scores = new double[documents.size()];

        for (int i = 0; i < documents.size(); i++) {

            scores[i] = BM25Scorer.score(
                query,
                documents.get(i),
                documents
            );

            System.out.println(
                documents.get(i)
                + " | Score: " + scores[i]
            );
        }

        // Verify that relevant documents score higher
        boolean passed =
            scores[0] > scores[2] &&
            scores[1] > scores[2] &&
            scores[0] > 0 &&
            scores[1] > 0;

        if (passed) {
            System.out.println("\nTEST PASSED!");
            System.out.println(
                "Relevant documents received higher BM25 scores."
            );
        } else {
            System.out.println("\nTEST FAILED!");
            throw new RuntimeException("BM25 ranking test failed.");
        }
    }
}
