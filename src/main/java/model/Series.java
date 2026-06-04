package model;

import java.util.ArrayList;
import java.util.List;

public class Series extends Media {
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

    public String getDirector() {
        return director;
    }

    public String getCompany() {
        return company;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    public void addEpisode(Episode episode) {
        if (episode != null) {
            episodes.add(episode);
        }
    }

    @Override
    public String toString() {
        return "Series{" +
                "title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", director='" + director + '\'' +
                ", company='" + company + '\'' +
                ", episodes=" + episodes.size() +
                '}';
    }
}