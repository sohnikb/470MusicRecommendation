MusicRecommendation uses a locally collected music dataset and the BM25 ranking algorithm to find songs relevant to a user's description.

## Technology Stack
- Java 17
- Maven
- Jackson Databind
- MusicBrainz API
- BM25 information retrieval algorithm
- Ruby on Rails (website development)

## Requirements/Setup

- Java 17 or later
- Maven 3.6 or later

1. Clone the repository:

```bash
git clone YOUR_GITHUB_REPOSITORY_URL
```

2. Navigate to the Java project:

```bash
cd musicRecommender/java-collector
```

3. Compile the project:

```bash
mvn clean compile
```

## Running the BM25 Test

Run the test using:

```bash
mvn exec:java -Dexec.mainClass="BM25Test"
```

The test uses a sample collection of music-related documents and checks whether documents matching the query receive higher BM25 scores.

A successful test prints:

```text
TEST PASSED!
Relevant documents received higher BM25 scores.
```

## Running the Recommendation Engine

From the `java-collector` directory, run:

```bash
mvn exec:java -Dexec.mainClass="MusicRecommendationEngine"
```

The recommendation engine loads the local CSV dataset and ranks songs based on the text query.

The dataset must be located at:

```text
src/music_dataset.csv
```
