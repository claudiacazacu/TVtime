import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements Repository<User> {

    private static UserRepository instance;

    private UserRepository() {}

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    private Connection conn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void create(User user) throws SQLException {
        String sql = "INSERT INTO users (username, age, email, is_admin) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setInt(2, user.getAge());
            ps.setString(3, user.getEmail());
            ps.setBoolean(4, user.isAdmin());
            ps.executeUpdate();
        }
    }

    @Override
    public User findById(int id) throws SQLException {
        throw new UnsupportedOperationException("Foloseste findByUsername pentru User.");
    }

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY username";
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapRow(rs));
            }
        }
        return users;
    }

    @Override
    public void update(User user) throws SQLException {
        String sql = "UPDATE users SET age = ?, email = ?, is_admin = ? WHERE username = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, user.getAge());
            ps.setString(2, user.getEmail());
            ps.setBoolean(3, user.isAdmin());
            ps.setString(4, user.getUsername());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        throw new UnsupportedOperationException("Foloseste deleteByUsername pentru User.");
    }

    public void deleteByUsername(String username) throws SQLException {
        String sql = "DELETE FROM users WHERE username = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        boolean isAdmin = rs.getBoolean("is_admin");
        if (isAdmin) {
            return new Admin(
                    rs.getString("username"),
                    rs.getInt("age"),
                    rs.getString("email")
            );
        } else {
            return new User(
                    rs.getString("username"),
                    rs.getInt("age"),
                    rs.getString("email")
            );
        }
    }
}
