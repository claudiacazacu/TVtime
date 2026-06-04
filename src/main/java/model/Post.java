package model;

public class Post {
    private final Admin author;
    private final String text;

    public Post(Admin author, String text) {
        this.author = author;
        this.text = text;
    }

    public Admin getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Post{" +
                "author=" + author.getUsername() +
                ", text='" + text + '\'' +
                '}';
    }
}
