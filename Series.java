import java.util.ArrayList;
import java.util.List;

public class Series extends Media
{
    private String director;
    private String company;
    private List<Episode> episodes;

    public Series(String title, String releaseDate, String genre, String description,
                  String director, String company) {

        super(title, releaseDate, genre, description);
        this.director = director;
        this.company = company;
        this.episodes = new ArrayList<>();
    }

    public void addEpisode(Episode episode) {
        episodes.add(episode);
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public String getDirector() {
        return director;
    }
}



