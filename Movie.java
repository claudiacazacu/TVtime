import java.util.ArrayList;
import java.util.List;

public class Movie extends Media
{
    private String director;
    private String company;

    public Series(String title, String releaseDate, String genre, String description,
                  String director, String company) {

        super(title, releaseDate, genre, description);
        this.director = director;
        this.company = company;
    }

    public String getDirector() {
        return director;
    }

}
