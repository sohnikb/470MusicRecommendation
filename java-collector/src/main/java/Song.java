public class Song {
    private String mbid;
    private String title;
    private String artist;
    private long lengthMs;
    private String releaseDate;
    private String category;

    public Song(String mbid, String title, String artist,
                long lengthMs, String releaseDate, String category) {
        this.mbid = mbid;
        this.title = title;
        this.artist = artist;
        this.lengthMs = lengthMs;
        this.releaseDate = releaseDate;
        this.category = category;
    }

    public String getMbid() {
        return mbid;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public long getLengthMs() {
        return lengthMs;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getCategory() {
        return category;
    }
}
