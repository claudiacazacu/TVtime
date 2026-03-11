class Movie
{
    private String title;
    private String releaseDate;
    private String genre;
    private String director;
    private String description;
    private String company;

    private Movie(String title, String releaseDate, String genre, String director, String description, String company)
    {
        this.title = title;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.director = director;
        this.description = description;
        this.company = company;
    }
}
