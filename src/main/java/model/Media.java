package model;

import java.util.HashMap;
import java.util.Map;

public abstract class Media {
    protected String title;
    protected String releaseDate;
    protected String genre;
    protected String description;

    protected Map<String, Character> cast;

    public Media(String title, String releaseDate, String genre, String description) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.description = description;
        this.cast = new HashMap<>();
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Character> getCast() {
        return cast;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addCast(String actor, Character character) {
        if (actor != null && character != null) {
            cast.put(actor, character);
        }
    }

    @Override
    public String toString() {
        return "Media{" +
                "title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}