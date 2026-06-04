package repository;

import config.DatabaseConnection;
import model.Episode;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EpisodeRepository implements Repository<Episode> {

    private static EpisodeRepository instance;

    private EpisodeRepository() {}

    public static EpisodeRepository getInstance() {
        if (instance == null) {
            instance = new EpisodeRepository();
        }
        return instance;
    }

    private Connection conn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    public void create(Episode episode, int seriesId) throws SQLException {
        String sql = "INSERT INTO episodes (series_id, title, release_date, season_number, episode_number, duration) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, seriesId);
            ps.setString(2, episode.getTitle());
            ps.setString(3, episode.getReleaseDate());
            ps.setInt(4, episode.getSeasonNumber());
            ps.setInt(5, episode.getEpisodeNumber());
            ps.setInt(6, episode.getDuration());
            ps.executeUpdate();
        }
    }

    @Override
    public void create(Episode episode) throws SQLException {
        throw new UnsupportedOperationException("Foloseste create(Episode, seriesId).");
    }

    @Override
    public Episode findById(int id) throws SQLException {
        String sql = "SELECT * FROM episodes WHERE id = ?";
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
    public List<Episode> findAll() throws SQLException {
        List<Episode> list = new ArrayList<>();
        String sql = "SELECT * FROM episodes ORDER BY series_id, season_number, episode_number";
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Episode> findBySeriesId(int seriesId) throws SQLException {
        List<Episode> list = new ArrayList<>();
        String sql = "SELECT * FROM episodes WHERE series_id = ? ORDER BY season_number, episode_number";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, seriesId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public void update(Episode episode) throws SQLException {
        throw new UnsupportedOperationException("Foloseste updateById(id, episode).");
    }

    public void updateById(int id, Episode episode) throws SQLException {
        String sql = "UPDATE episodes SET title = ?, release_date = ?, season_number = ?, " +
                     "episode_number = ?, duration = ? WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, episode.getTitle());
            ps.setString(2, episode.getReleaseDate());
            ps.setInt(3, episode.getSeasonNumber());
            ps.setInt(4, episode.getEpisodeNumber());
            ps.setInt(5, episode.getDuration());
            ps.setInt(6, id);
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM episodes WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Episode mapRow(ResultSet rs) throws SQLException {
        return new Episode(
                rs.getString("title"),
                rs.getString("release_date"),
                rs.getInt("episode_number"),
                rs.getInt("season_number"),
                rs.getInt("duration")
        );
    }
}
