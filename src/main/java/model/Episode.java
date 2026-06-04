package model;

public class Episode {
    private String title;
    private String releaseDate;
    private int episodeNumber;
    private int seasonNumber;
    private int duration; // in mins

    public Episode(String title, String releaseDate, int episodeNumber, int seasonNumber, int duration) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.episodeNumber = episodeNumber;
        this.seasonNumber = seasonNumber;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public int getDuration() {
        return duration;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "title='" + title + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", episodeNumber=" + episodeNumber +
                ", seasonNumber=" + seasonNumber +
                ", duration=" + duration +
                '}';
    }
}