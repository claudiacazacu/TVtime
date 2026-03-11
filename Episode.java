public class Episode 
{
    private String title;
    private String releaseDate;
    private int episodeNumber;
    private int seasonNumber;
    private int duration; // in minutes

    public Episode(String title, String releaseDate, int episodeNumber, int seasonNumber, int duration) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.episodeNumber = episodeNumber;
        this.seasonNumber = seasonNumber;
        this.duration = duration;
    }

    public String getTitle()
    {
        return title;
    }

    public int getEpisodeNumber()
    {
        return episodeNumber;
    }
}