import java.util.ArrayList;
import java.util.List;

public class Service
{
    private List<User> users;
    private List<Media> mediaLibrary;

    public Service() {
        this.users = new ArrayList<>();
        this.mediaLibrary = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addMedia(Media media){
        mediaLibrary.add(media);
    }

    public void showAllUsers() {
        for (User user : users) {
            System.out.println(user.getUsername());
        }
    }

    public void showAllMedia(){
        for(Media media : mediaLibrary){
            System.out.println(media.getTitle());
        }
    }
}