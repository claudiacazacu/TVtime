public class AdminService extends UserService {

    public AdminService() {
        super();
    }

    public AdminService(ServiceData data) {
        super(data);
    }

    public boolean addMedia(Admin admin, Media media) {
        if (!hasAdminAccess(admin) || media == null) {
            return false;
        }

        data.getMediaLibrary().add(media);
        data.getGenres().add(media.getGenre());
        return true;
    }

    public boolean deleteMedia(Admin admin, String mediaTitle) {
        if (!hasAdminAccess(admin)) {
            return false;
        }

        Media media = findMediaByExactTitle(mediaTitle);
        if (media == null) {
            return false;
        }

        data.getMediaLibrary().remove(media);
        data.getWatchEntries().removeIf(watchEntry ->
                watchEntry.getMedia().getTitle().equalsIgnoreCase(media.getTitle()));
        refreshGenres();
        return true;
    }

    public boolean addEpisode(Admin admin, String seriesTitle, Episode episode) {
        if (!hasAdminAccess(admin) || episode == null) {
            return false;
        }

        Media media = findMediaByExactTitle(seriesTitle);
        if (!(media instanceof Series)) {
            return false;
        }

        ((Series) media).addEpisode(episode);
        return true;
    }

    public boolean deleteUser(Admin admin, String username) {
        if (!hasAdminAccess(admin)) {
            return false;
        }

        User user = findUserByUsername(username);
        if (user == null) {
            return false;
        }

        data.getUsers().remove(user);
        data.getWatchEntries().removeIf(watchEntry ->
                watchEntry.getUser().getUsername().equalsIgnoreCase(user.getUsername()));
        return true;
    }

    public boolean createPost(Admin admin, String text) {
        if (!hasAdminAccess(admin) || text == null || text.trim().isEmpty()) {
            return false;
        }

        data.getPosts().add(new Post(admin, text.trim()));
        return true;
    }

    private boolean hasAdminAccess(Admin admin) {
        return admin != null && admin.isAdmin();
    }

    private void refreshGenres() {
        data.getGenres().clear();
        for (Media media : data.getMediaLibrary()) {
            data.getGenres().add(media.getGenre());
        }
    }
}
