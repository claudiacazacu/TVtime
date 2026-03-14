public class WatchEntry {
    private User user;
    private Media media;
    private Episode episode;
    private String watchedDate;
    private double rating;
    private Comment comment;
    private Character favCharacter;

    public WatchEntry(User user, Media media, Episode episode, String watchedDate) {
        this.user = user;
        this.media = media;
        this.episode = episode;
        this.watchedDate = watchedDate;
        this.rating = 0.0;
        this.comment = null;
        this.favCharacter = null;
    }

    public void setRating(double rating){
        this.rating = rating;
    }

    public void setComment(Comment comment){
        this.comment = comment;
    }

    public void setFavCharacter(Character favCharacter){
        this.favCharacter = favCharacter;
    }

    public User getUser() {
        return user;
    }

    public Media getMedia() {
        return media;
    }

    public Episode getEpisode() {
        return episode;
    }

    public double getRating() {
        return rating;
    }

    public Comment getComment() {
        return comment;
    }

    public Character getFavCharacter() {
        return favCharacter;
    }

    @Override
    public String toString() {
        return user.getUsername() + " watched " +
                media.getTitle() +
                (episode != null ? " - " + episode.getTitle() : "") +
                " on " + watchedDate +
                ", rating: " + rating;
    }
}
