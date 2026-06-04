package service;

import exception.DuplicateMediaException;
import exception.MediaNotFoundException;
import exception.UnauthorizedAccessException;
import exception.UserNotFoundException;
import model.*;

public class AdminService extends UserService {

    public AdminService() {
        super();
    }

    public AdminService(ServiceData data) {
        super(data);
    }

    public void addMedia(Admin admin, Media media)
            throws UnauthorizedAccessException, DuplicateMediaException {
        if (admin == null || !admin.isAdmin()) {
            throw new UnauthorizedAccessException(admin != null ? admin.getUsername() : "null");
        }
        if (media == null) return;
        if (findMediaByExactTitle(media.getTitle()) != null) {
            throw new DuplicateMediaException(media.getTitle());
        }
        data.getMediaLibrary().add(media);
        data.getGenres().add(media.getGenre());
    }

    public void deleteMedia(Admin admin, String mediaTitle)
            throws UnauthorizedAccessException, MediaNotFoundException {
        if (admin == null || !admin.isAdmin()) {
            throw new UnauthorizedAccessException(admin != null ? admin.getUsername() : "null");
        }
        Media media = findMediaByExactTitle(mediaTitle);
        if (media == null) {
            throw new MediaNotFoundException(mediaTitle);
        }
        data.getMediaLibrary().remove(media);
        data.getWatchEntries().removeIf(we ->
                we.getMedia().getTitle().equalsIgnoreCase(media.getTitle()));
        refreshGenres();
    }

    public void addEpisode(Admin admin, String seriesTitle, Episode episode)
            throws UnauthorizedAccessException, MediaNotFoundException {
        if (admin == null || !admin.isAdmin()) {
            throw new UnauthorizedAccessException(admin != null ? admin.getUsername() : "null");
        }
        Media media = findMediaByExactTitle(seriesTitle);
        if (!(media instanceof Series)) {
            throw new MediaNotFoundException(seriesTitle);
        }
        ((Series) media).addEpisode(episode);
    }

    public void deleteUser(Admin admin, String username)
            throws UnauthorizedAccessException, UserNotFoundException {
        if (admin == null || !admin.isAdmin()) {
            throw new UnauthorizedAccessException(admin != null ? admin.getUsername() : "null");
        }
        User user = findUserByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(username);
        }
        data.getUsers().remove(user);
        data.getWatchEntries().removeIf(we ->
                we.getUser().getUsername().equalsIgnoreCase(user.getUsername()));
    }

    public void createPost(Admin admin, String text)
            throws UnauthorizedAccessException {
        if (admin == null || !admin.isAdmin()) {
            throw new UnauthorizedAccessException(admin != null ? admin.getUsername() : "null");
        }
        if (text != null && !text.trim().isEmpty()) {
            data.getPosts().add(new Post(admin, text.trim()));
        }
    }

    private void refreshGenres() {
        data.getGenres().clear();
        for (Media media : data.getMediaLibrary()) {
            data.getGenres().add(media.getGenre());
        }
    }
}
