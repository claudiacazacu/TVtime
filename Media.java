public abstract class Media {
    protected String title;
    protected String releaseDate;
    protected String genre;
    protected String description;

    public Media(String title, String releaseDate, String genre, String director, String description, String company)
    {
        this.title = title;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.description = description;
    }

    String getTitle() {
        return title;
    }
}