import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {

    private static final String CSV_PATH = "data/audit.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static AuditService instance;

    private AuditService() {
        // Scriem header-ul doar daca fisierul e nou/gol
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_PATH, true))) {
            java.io.File f = new java.io.File(CSV_PATH);
            if (f.length() == 0) {
                pw.println("nume_actiune,timestamp");
            }
        } catch (IOException e) {
            System.err.println("AuditService: nu s-a putut initializa fisierul CSV: " + e.getMessage());
        }
    }

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public void log(String actionName) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_PATH, true))) {
            pw.println(actionName + "," + timestamp);
        } catch (IOException e) {
            System.err.println("AuditService: eroare la scriere: " + e.getMessage());
        }
    }
}
