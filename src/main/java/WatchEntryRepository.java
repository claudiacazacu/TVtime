import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WatchEntryRepository implements Repository<WatchEntry> {

    private static WatchEntryRepository instance;

    private WatchEntryRepository() {}

    public static WatchEntryRepository getInstance() {
        if (instance == null) {
            instance = new WatchEntryRepository();
        }
        return instance;
    }

    private Connection conn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    // Returneaza id-ul generat dupa insert
    public int create(WatchEntry we, int mediaId, Integer episodeId, Integer favCharacterId) throws SQLException {
        String sql = "INSERT INTO watch_entries " +
                     "(username, media_id, episode_id, watched_date, rating, comment_author, comment_text, fav_character_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, we.getUser().getUsername());
            ps.setInt(2, mediaId);
            if (episodeId != null) ps.setInt(3, episodeId); else ps.setNull(3, Types.INTEGER);
            ps.setDate(4, we.getWatchedDate() != null ? Date.valueOf(we.getWatchedDate()) : null);
            ps.setDouble(5, we.getRating());
            if (we.getComment() != null) {
                ps.setString(6, we.getComment().getAuthor());
                ps.setString(7, we.getComment().getText());
            } else {
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.VARCHAR);
            }
            if (favCharacterId != null) ps.setInt(8, favCharacterId); else ps.setNull(8, Types.INTEGER);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        }
        return -1;
    }

    @Override
    public void create(WatchEntry entity) throws SQLException {
        throw new UnsupportedOperationException("Foloseste create(WatchEntry, mediaId, episodeId, favCharacterId).");
    }

    @Override
    public WatchEntry findById(int id) throws SQLException {
        String sql = "SELECT we.*, u.age, u.email, u.is_admin " +
                     "FROM watch_entries we JOIN users u ON u.username = we.username " +
                     "WHERE we.id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    @Override
    public List<WatchEntry> findAll() throws SQLException {
        List<WatchEntry> list = new ArrayList<>();
        String sql = "SELECT we.*, u.age, u.email, u.is_admin " +
                     "FROM watch_entries we JOIN users u ON u.username = we.username " +
                     "ORDER BY we.watched_date DESC";
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<WatchEntry> findByUsername(String username) throws SQLException {
        List<WatchEntry> list = new ArrayList<>();
        String sql = "SELECT we.*, u.age, u.email, u.is_admin " +
                     "FROM watch_entries we JOIN users u ON u.username = we.username " +
                     "WHERE we.username = ? ORDER BY we.watched_date DESC";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public void update(WatchEntry we) throws SQLException {
        throw new UnsupportedOperationException("Foloseste updateById(id, rating, commentText).");
    }

    public void updateById(int id, double rating, String commentText) throws SQLException {
        String sql = "UPDATE watch_entries SET rating = ?, comment_text = ? WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setDouble(1, rating);
            ps.setString(2, commentText);
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM watch_entries WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private WatchEntry mapRow(ResultSet rs) throws SQLException {
        boolean isAdmin = rs.getBoolean("is_admin");
        User user = isAdmin
                ? new Admin(rs.getString("username"), rs.getInt("age"), rs.getString("email"))
                : new User(rs.getString("username"), rs.getInt("age"), rs.getString("email"));

        // Media si Episode sunt placeholder - pentru a le popula complet
        // ai nevoie de MediaRepository/EpisodeRepository, dar pastram simplu
        Media media = new Movie("id=" + rs.getInt("media_id"), "", "", "", "", "");

        Date watchedDate = rs.getDate("watched_date");
        WatchEntry we = new WatchEntry(user, media, null,
                watchedDate != null ? watchedDate.toLocalDate() : null);
        we.setRating(rs.getDouble("rating"));

        String commentAuthor = rs.getString("comment_author");
        String commentText = rs.getString("comment_text");
        if (commentAuthor != null && commentText != null) {
            we.setComment(new Comment(commentAuthor, commentText));
        }
        return we;
    }
}
