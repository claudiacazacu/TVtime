import java.sql.SQLException;
import java.util.List;

// Interfata generica pentru operatii CRUD
public interface Repository<T> {
    void create(T entity) throws SQLException;
    T findById(int id) throws SQLException;
    List<T> findAll() throws SQLException;
    void update(T entity) throws SQLException;
    void delete(int id) throws SQLException;
}
