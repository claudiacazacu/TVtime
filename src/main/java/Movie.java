public class Movie extends Media
{
    private String director;
    private String company;

    public Movie(String title, String releaseDate, String genre, String description,
                  String director, String company) {

        super(title, releaseDate, genre, description);
        this.director = director;
        this.company = company;
    }

    public String getDirector() {
        return director;
    }

    @Override
    public String toString() {
        return "Movie{" +
            "title='" + title + '\'' +
            ", releaseDate='" + releaseDate + '\'' +
            ", genre='" + genre + '\'' +
            ", director='" + director + '\'' +
            ", company='" + company + '\'' +
            '}';
    }
}
