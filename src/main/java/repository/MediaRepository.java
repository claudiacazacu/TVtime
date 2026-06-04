package repository;

import config.DatabaseConnection;
import model.Media;
import model.Movie;
import model.Series;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MediaRepository implements Repository<Media> {

    private static MediaRepository instance;

    private MediaRepository() {}

    public static MediaRepository getInstance() {
        if (instance == null) {
            instance = new MediaRepository();
        }
        return instance;
    }

    private Connection conn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void create(Media media) throws SQLException {
        String sql = "INSERT INTO media (title, release_date, genre, description, media_type, director, company) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, media.getTitle());
            ps.setString(2, media.getReleaseDate());
            ps.setString(3, media.getGenre());
            ps.setString(4, media.getDescription());
            if (media instanceof Movie) {
                ps.setString(5, "MOVIE");
                ps.setString(6, ((Movie) media).getDirector());
                ps.setString(7, ((Movie) media).getCompany());
            } else {
                ps.setString(5, "SERIES");
                ps.setString(6, ((Series) media).getDirector());
                ps.setString(7, ((Series) media).getCompany());
            }
            ps.executeUpdate();
        }
    }

    @Override
    public Media findById(int id) throws SQLException {
        String sql = "SELECT * FROM media WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    public Media findByTitle(String title) throws SQLException {
        String sql = "SELECT * FROM media WHERE LOWER(title) = LOWER(?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, title);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    @Override
    public List<Media> findAll() throws SQLException {
        List<Media> list = new ArrayList<>();
        String sql = "SELECT * FROM media ORDER BY title";
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Media> findByType(String type) throws SQLException {
        List<Media> list = new ArrayList<>();
        String sql = "SELECT * FROM media WHERE media_type = ? ORDER BY title";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, type.toUpperCase());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public void update(Media media) throws SQLException {
        String sql = "UPDATE media SET title = ?, release_date = ?, genre = ?, description = ?, " +
                     "director = ?, company = ? WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, media.getTitle());
            ps.setString(2, media.getReleaseDate());
            ps.setString(3, media.getGenre());
            ps.setString(4, media.getDescription());
            if (media instanceof Movie) {
                ps.setString(5, ((Movie) media).getDirector());
                ps.setString(6, ((Movie) media).getCompany());
            } else {
                ps.setString(5, ((Series) media).getDirector());
                ps.setString(6, ((Series) media).getCompany());
            }
            ps.setString(7, media.getTitle());
            ps.executeUpdate();
        }
    }

    public void updateById(int id, Media media) throws SQLException {
        String sql = "UPDATE media SET title = ?, release_date = ?, genre = ?, description = ?, " +
                     "director = ?, company = ? WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, media.getTitle());
            ps.setString(2, media.getReleaseDate());
            ps.setString(3, media.getGenre());
            ps.setString(4, media.getDescription());
            if (media instanceof Movie) {
                ps.setString(5, ((Movie) media).getDirector());
                ps.setString(6, ((Movie) media).getCompany());
            } else {
                ps.setString(5, ((Series) media).getDirector());
                ps.setString(6, ((Series) media).getCompany());
            }
            ps.setInt(7, id);
            ps.executeUpdate();
        }
    }

    public int findIdByTitle(String title) throws SQLException {
        String sql = "SELECT id FROM media WHERE LOWER(title) = LOWER(?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, title);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        }
        return -1;
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM media WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Media mapRow(ResultSet rs) throws SQLException {
        String type = rs.getString("media_type");
        String title = rs.getString("title");
        String releaseDate = rs.getString("release_date");
        String genre = rs.getString("genre");
        String description = rs.getString("description");
        String director = rs.getString("director");
        String company = rs.getString("company");

        if ("MOVIE".equals(type)) {
            return new Movie(title, releaseDate, genre, description, director, company);
        } else {
            return new Series(title, releaseDate, genre, description, director, company);
        }
    }
}
