import config.DatabaseConnection;
import exception.*;
import model.Admin;
import model.Comment;
import model.Episode;
import model.Media;
import model.Movie;
import model.Series;
import model.User;
import model.WatchEntry;
import repository.EpisodeRepository;
import repository.MediaRepository;
import repository.UserRepository;
import repository.WatchEntryRepository;
import service.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static UserService userService;
    static AdminService adminService;
    static AuditService audit;
    static Admin admin;

    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            System.out.println("Conectat la PostgreSQL: " + !conn.isClosed());
        } catch (SQLException e) {
            System.out.println("Eroare conexiune BD: " + e.getMessage());
            System.out.println("Aplicatia nu poate porni fara conexiune la baza de date.");
            return;
        }

        audit = AuditService.getInstance();
        ServiceData serviceData = new ServiceData();
        userService = new UserService(serviceData);
        adminService = new AdminService(serviceData);
        admin = new Admin("admin", 30, "admin@tvtime.com");

        // Incarcare date din PostgreSQL
        try {
            List<User> users = UserRepository.getInstance().findAll();
            for (User u : users) userService.addUser(u);
            System.out.println("Utilizatori incarcati din DB: " + users.size());

            List<Media> media = MediaRepository.getInstance().findAll();
            for (Media m : media) {
                try { adminService.addMedia(admin, m); }
                catch (Exception ignored) {}
            }
            System.out.println("Titluri incarcate din DB: " + media.size());
        } catch (SQLException e) {
            System.out.println("Eroare la incarcare date din DB: " + e.getMessage());
        }

        menuPrincipal();
    }

    static void menuPrincipal() {
        int opt;
        do {
            System.out.println("--> TV TIME");
            System.out.println("1. Top saptamanii");
            System.out.println("2. Recomandari personalizate");
            System.out.println("3. Adauga watch entry");
            System.out.println("4. Adauga review (rating + comentariu)");
            System.out.println("5. Afiseaza profil utilizator");
            System.out.println("99. ADMIN");
            System.out.println("0.  Iesire");
            System.out.print(">> ");
            opt = scanner.nextInt(); scanner.nextLine();

            switch (opt) {
                case 1:
                    audit.log("top_saptamana");
                    userService.topWeek(userService.getData().getWatchEntries());
                    break;
                case 2:
                    audit.log("recomandari");
                    System.out.print("Username: ");
                    String recUser = scanner.nextLine();
                    System.out.print("Cate recomandari? ");
                    int recN = scanner.nextInt(); scanner.nextLine();
                    try { userService.showRecommendationsForUser(recUser, recN); }
                    catch (UserNotFoundException e) { System.out.println("Eroare: " + e.getMessage()); }
                    break;
                case 3:
                    adaugaWatchEntry();
                    break;
                case 4:
                    adaugaReview();
                    break;
                case 5:
                    audit.log("profil_utilizator");
                    System.out.print("Username: ");
                    String profUser = scanner.nextLine();
                    try { userService.showUserProfile(profUser); }
                    catch (UserNotFoundException e) { System.out.println("Eroare: " + e.getMessage()); }
                    break;
                case 99:
                    menuAdmin();
                    break;
                case 0:
                    System.out.println("La revedere!");
                    break;
                default:
                    System.out.println("Optiune invalida.");
            }
        } while (opt != 0);
    }

    static void menuAdmin() {
        int opt;
        do {
            System.out.println("--> ADMIN");
            System.out.println("1. CRUD");
            System.out.println("2. Adauga episod la serial");
            System.out.println("3. Creeaza post");
            System.out.println("0. Inapoi");
            System.out.print(">> ");
            opt = scanner.nextInt(); scanner.nextLine();

            switch (opt) {
                case 1:
                    menuCrud();
                    break;
                case 2:
                    audit.log("adauga_episod");
                    System.out.print("Titlu serial: ");
                    String serTitle = scanner.nextLine();
                    System.out.print("Titlu episod: ");
                    String epTitle = scanner.nextLine();
                    System.out.print("Data lansarii: ");
                    String epDate = scanner.nextLine();
                    System.out.print("Nr. episod: ");
                    int epNr = scanner.nextInt();
                    System.out.print("Nr. sezon: ");
                    int epSez = scanner.nextInt();
                    System.out.print("Durata (minute): ");
                    int epDur = scanner.nextInt(); scanner.nextLine();
                    Episode ep = new Episode(epTitle, epDate, epNr, epSez, epDur);
                    try {
                        adminService.addEpisode(admin, serTitle, ep);
                        int sId = MediaRepository.getInstance().findIdByTitle(serTitle);
                        if (sId != -1) EpisodeRepository.getInstance().create(ep, sId);
                        System.out.println("Episod adaugat cu succes.");
                    } catch (MediaNotFoundException | UnauthorizedAccessException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    } catch (SQLException e) {
                        System.out.println("[DB] Eroare: " + e.getMessage());
                    }
                    break;
                case 3:
                    audit.log("creeaza_post");
                    System.out.print("Text post: ");
                    String postText = scanner.nextLine();
                    try {
                        adminService.createPost(admin, postText);
                        System.out.println("Post creat.");
                    } catch (UnauthorizedAccessException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Optiune invalida.");
            }
        } while (opt != 0);
    }

    static void menuCrud() {
        int opt;
        do {
            System.out.println("CRUD");
            System.out.println("1. Utilizator");
            System.out.println("2. Film");
            System.out.println("3. Serial");
            System.out.println("4. Episod");
            System.out.println("0. Inapoi");
            System.out.print(">> ");
            opt = scanner.nextInt(); scanner.nextLine();

            switch (opt) {
                case 1: crudUtilizator(); break;
                case 2: crudFilm(); break;
                case 3: crudSerial(); break;
                case 4: crudEpisod(); break;
                case 0: break;
                default: System.out.println("Optiune invalida.");
            }
        } while (opt != 0);
    }

    static void crudUtilizator() {
        int opt;
        do {
            System.out.println("\n--- CRUD Utilizator ---");
            System.out.println("1. Adauga (Create)");
            System.out.println("2. Afiseaza toti din DB (Read)");
            System.out.println("3. Actualizeaza (Update)");
            System.out.println("4. Sterge (Delete)");
            System.out.println("0. Inapoi");
            System.out.print(">> ");
            opt = scanner.nextInt(); scanner.nextLine();

            switch (opt) {
                case 1:
                    audit.log("create_user");
                    System.out.print("Username: ");
                    String uname = scanner.nextLine();
                    System.out.print("Varsta: ");
                    int uage = scanner.nextInt(); scanner.nextLine();
                    System.out.print("Email: ");
                    String uemail = scanner.nextLine();
                    User newU = new User(uname, uage, uemail);
                    userService.addUser(newU);
                    try {
                        UserRepository.getInstance().create(newU);
                        System.out.println("Utilizator adaugat.");
                    } catch (SQLException e) { System.out.println("[DB] " + e.getMessage()); }
                    break;
                case 2:
                    audit.log("read_users_db");
                    try {
                        List<User> dbU = UserRepository.getInstance().findAll();
                        System.out.println("Utilizatori in DB (" + dbU.size() + "):");
                        dbU.forEach(u -> System.out.println("  " + u));
                    } catch (SQLException e) { System.out.println("[DB] " + e.getMessage()); }
                    break;
                case 3:
                    audit.log("update_user");
                    System.out.print("Username de actualizat: ");
                    String updU = scanner.nextLine();
                    User toUpd = userService.findUserByUsername(updU);
                    if (toUpd == null) { System.out.println("Utilizatorul nu exista."); break; }
                    System.out.print("Varsta noua (" + toUpd.getAge() + "): ");
                    String newAge = scanner.nextLine();
                    System.out.print("Email nou (" + toUpd.getEmail() + "): ");
                    String newMail = scanner.nextLine();
                    if (!newAge.trim().isEmpty()) toUpd.setAge(Integer.parseInt(newAge.trim()));
                    if (!newMail.trim().isEmpty()) toUpd.setEmail(newMail.trim());
                    try {
                        UserRepository.getInstance().update(toUpd);
                        System.out.println("Utilizator actualizat.");
                    } catch (SQLException e) { System.out.println("[DB] " + e.getMessage()); }
                    break;
                case 4:
                    audit.log("delete_user");
                    System.out.print("Username de sters: ");
                    String delU = scanner.nextLine();
                    try {
                        adminService.deleteUser(admin, delU);
                        UserRepository.getInstance().deleteByUsername(delU);
                        System.out.println("Utilizator sters.");
                    } catch (UserNotFoundException | UnauthorizedAccessException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    } catch (SQLException e) { System.out.println("[DB] " + e.getMessage()); }
                    break;
                case 0: break;
                default: System.out.println("Optiune invalida.");
            }
        } while (opt != 0);
    }

    static void crudFilm() {
        int opt;
        do {
            System.out.println("\n--- CRUD Film ---");
            System.out.println("1. Adauga (Create)");
            System.out.println("2. Afiseaza toate din DB (Read)");
            System.out.println("3. Actualizeaza (Update)");
            System.out.println("4. Sterge (Delete)");
            System.out.println("0. Inapoi");
            System.out.print(">> ");
            opt = scanner.nextInt(); scanner.nextLine();

            switch (opt) {
                case 1:
                    audit.log("create_film");
                    System.out.print("Titlu: "); String mT = scanner.nextLine();
                    System.out.print("Data lansarii: "); String mD = scanner.nextLine();
                    System.out.print("Gen: "); String mG = scanner.nextLine();
                    System.out.print("Descriere: "); String mDesc = scanner.nextLine();
                    System.out.print("Director: "); String mDir = scanner.nextLine();
                    System.out.print("Companie: "); String mComp = scanner.nextLine();
                    Movie newM = new Movie(mT, mD, mG, mDesc, mDir, mComp);
                    try {
                        adminService.addMedia(admin, newM);
                        MediaRepository.getInstance().create(newM);
                        System.out.println("Film adaugat.");
                    } catch (DuplicateMediaException | UnauthorizedAccessException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    } catch (SQLException e) { System.out.println("[DB] " + e.getMessage()); }
                    break;
                case 2:
                    audit.log("read_films_db");
                    try {
                        List<Media> dbM = MediaRepository.getInstance().findByType("MOVIE");
                        System.out.println("Filme in DB (" + dbM.size() + "):");
                        dbM.forEach(m -> System.out.println("  " + m));
                    } catch (SQLException e) { System.out.println("[DB] " + e.getMessage()); }
                    break;
                case 3:
                    audit.log("update_film");
                    System.out.print("Titlu film de actualizat: ");
                    String updMT = scanner.nextLine();
                    Media toUpdM = userService.findMediaByExactTitle(updMT);
                    if (toUpdM == null) { System.out.println("Filmul nu exista."); break; }
                    System.out.print("Gen nou (" + toUpdM.getGenre() + "): "); String nG = scanner.nextLine();
                    System.out.print("Descriere noua (" + toUpdM.getDescription() + "): "); String nD = scanner.nextLine();
                    if (!nG.trim().isEmpty()) toUpdM.setGenre(nG.trim());
                    if (!nD.trim().isEmpty()) toUpdM.setDescription(nD.trim());
                    try {
                        MediaRepository.getInstance().update(toUpdM);
                        System.out.println("Film actualizat.");
                    } catch (SQLException e) { System.out.println("[DB] " + e.getMessage()); }
                    break;
                case 4:
                    audit.log("delete_film");
                    System.out.print("Titlu film de sters: ");
                    String delMT = scanner.nextLine();
                    try {
                        int delId = MediaRepository.getInstance().findIdByTitle(delMT);
                        adminService.deleteMedia(admin, delMT);
                        if (delId != -1) MediaRepository.getInstance().delete(delId);
                        System.out.println("Film sters.");
                    } catch (MediaNotFoundException | UnauthorizedAccessException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    } catch (SQLException e) { System.out.println("[DB] " + e.getMessage()); }
                    break;
                case 0: break;
                default: System.out.println("Optiune invalida.");
            }
        } while (opt != 0);
    }

    static void crudSerial() {
        int opt;
        do {
            System.out.println("\n--- CRUD Serial ---");
            System.out.println("1. Adauga (Create)");
            System.out.println("2. Afiseaza toate din DB (Read)");
            System.out.println("3. Actualizeaza (Update)");
            System.out.println("4. Sterge (Delete)");
            System.out.println("0. Inapoi");
            System.out.print(">> ");
            opt = scanner.nextInt(); scanner.nextLine();

            switch (opt) {
                case 1:
                    audit.log("create_serial");
                    System.out.print("Titlu: "); String sT = scanner.nextLine();
                    System.out.print("Data lansarii: "); String sD = scanner.nextLine();
                    System.out.print("Gen: "); String sG = scanner.nextLine();
                    System.out.print("Descriere: "); String sDesc = scanner.nextLine();
                    System.out.print("Director: "); String sDir = scanner.nextLine();
                    System.out.print("Companie: "); String sComp = scanner.nextLine();
                    Series newS = new Series(sT, sD, sG, sDesc, sDir, sComp);
                    try {
                        adminService.addMedia(admin, newS);
                        MediaRepository.getInstance().create(newS);
                        System.out.println("Serial adaugat.");
                    } catch (DuplicateMediaException | UnauthorizedAccessException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    } catch (SQLException e) { System.out.println("[DB] " + e.getMessage()); }
                    break;
                case 2:
                    audit.log("read_serials_db");
                    try {
                        List<Media> dbS = MediaRepository.getInstance().findByType("SERIES");
                        System.out.println("Seriale in DB (" + dbS.size() + "):");
                        dbS.forEach(s -> System.out.println("  " + s));
                    } catch (SQLException e) { System.out.println("[DB] " + e.getMessage()); }
                    break;
                case 3:
                    audit.log("update_serial");
                    System.out.print("Titlu serial de actualizat: ");
                    String updST = scanner.nextLine();
                    Media toUpdS = userService.findMediaByExactTitle(updST);
                    if (toUpdS == null) { System.out.println("Serialul nu exista."); break; }
                    System.out.print("Gen nou (" + toUpdS.getGenre() + "): "); String nSG = scanner.nextLine();
                    System.out.print("Descriere noua (" + toUpdS.getDescription() + "): "); String nSD = scanner.nextLine();
                    if (!nSG.trim().isEmpty()) toUpdS.setGenre(nSG.trim());
                    if (!nSD.trim().isEmpty()) toUpdS.setDescription(nSD.trim());
                    try {
                        MediaRepository.getInstance().update(toUpdS);
                        System.out.println("Serial actualizat.");
                    } catch (SQLException e) { System.out.println("[DB] " + e.getMessage()); }
                    break;
                case 4:
                    audit.log("delete_serial");
                    System.out.print("Titlu serial de sters: ");
                    String delST = scanner.nextLine();
                    try {
                        int delSId = MediaRepository.getInstance().findIdByTitle(delST);
                        adminService.deleteMedia(admin, delST);
                        if (delSId != -1) MediaRepository.getInstance().delete(delSId);
                        System.out.println("Serial sters.");
                    } catch (MediaNotFoundException | UnauthorizedAccessException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    } catch (SQLException e) { System.out.println("[DB] " + e.getMessage()); }
                    break;
                case 0: break;
                default: System.out.println("Optiune invalida.");
            }
        } while (opt != 0);
    }

    static void crudEpisod() {
        int opt;
        do {
            System.out.println("\n--- CRUD Episod ---");
            System.out.println("1. Adauga (Create)");
            System.out.println("2. Afiseaza toate din DB (Read)");
            System.out.println("3. Actualizeaza (Update)");
            System.out.println("4. Sterge (Delete)");
            System.out.println("0. Inapoi");
            System.out.print(">> ");
            opt = scanner.nextInt(); scanner.nextLine();

            switch (opt) {
                case 1:
                    audit.log("create_episod");
                    System.out.print("Titlu serial: "); String eSerTitle = scanner.nextLine();
                    System.out.print("Titlu episod: "); String eTitle = scanner.nextLine();
                    System.out.print("Data lansarii: "); String eDate = scanner.nextLine();
                    System.out.print("Nr. episod: "); int eNr = scanner.nextInt();
                    System.out.print("Nr. sezon: "); int eSez = scanner.nextInt();
                    System.out.print("Durata (minute): "); int eDur = scanner.nextInt(); scanner.nextLine();
                    Episode newEp = new Episode(eTitle, eDate, eNr, eSez, eDur);
                    try {
                        adminService.addEpisode(admin, eSerTitle, newEp);
                        int eSerId = MediaRepository.getInstance().findIdByTitle(eSerTitle);
                        if (eSerId != -1) EpisodeRepository.getInstance().create(newEp, eSerId);
                        System.out.println("Episod adaugat.");
                    } catch (MediaNotFoundException | UnauthorizedAccessException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    } catch (SQLException e) { System.out.println("[DB] " + e.getMessage()); }
                    break;
                case 2:
                    audit.log("read_episoade_db");
                    try {
                        List<Episode> dbEp = EpisodeRepository.getInstance().findAll();
                        System.out.println("Episoade in DB (" + dbEp.size() + "):");
                        dbEp.forEach(e -> System.out.println("  " + e));
                    } catch (SQLException e) { System.out.println("[DB] " + e.getMessage()); }
                    break;
                case 3:
                    audit.log("update_episod");
                    System.out.print("ID episod de actualizat: "); int eId = scanner.nextInt(); scanner.nextLine();
                    System.out.print("Titlu nou: "); String eTNew = scanner.nextLine();
                    System.out.print("Data noua: "); String eDNew = scanner.nextLine();
                    System.out.print("Nr. episod: "); int eNrNew = scanner.nextInt();
                    System.out.print("Nr. sezon: "); int eSezNew = scanner.nextInt();
                    System.out.print("Durata noua: "); int eDurNew = scanner.nextInt(); scanner.nextLine();
                    Episode updEp = new Episode(eTNew, eDNew, eNrNew, eSezNew, eDurNew);
                    try {
                        EpisodeRepository.getInstance().updateById(eId, updEp);
                        System.out.println("Episod actualizat.");
                    } catch (SQLException e) { System.out.println("[DB] " + e.getMessage()); }
                    break;
                case 4:
                    audit.log("delete_episod");
                    System.out.print("ID episod de sters: "); int delEId = scanner.nextInt(); scanner.nextLine();
                    try {
                        EpisodeRepository.getInstance().delete(delEId);
                        System.out.println("Episod sters.");
                    } catch (SQLException e) { System.out.println("[DB] " + e.getMessage()); }
                    break;
                case 0: break;
                default: System.out.println("Optiune invalida.");
            }
        } while (opt != 0);
    }

    static void adaugaWatchEntry() {
        audit.log("creare_watch_entry");
        System.out.print("Username: "); String wU = scanner.nextLine();
        System.out.print("Titlu media: "); String wM = scanner.nextLine();
        System.out.print("Titlu episod (gol daca film): "); String wE = scanner.nextLine();
        System.out.print("Rating (0-10): "); double wR = scanner.nextDouble(); scanner.nextLine();
        System.out.print("Comentariu (gol pentru niciunul): "); String wC = scanner.nextLine();

        User wUser = userService.findUserByUsername(wU);
        Media wMedia = userService.findMediaByExactTitle(wM);
        if (wUser == null) { System.out.println("Utilizatorul nu exista."); return; }
        if (wMedia == null) { System.out.println("Media nu exista."); return; }

        Episode wEp = null;
        if (wMedia instanceof Series && !wE.trim().isEmpty()) {
            wEp = userService.findEpisodeByTitle((Series) wMedia, wE);
            if (wEp == null) { System.out.println("Episodul nu exista."); return; }
        }

        WatchEntry we = new WatchEntry(wUser, wMedia, wEp, LocalDate.now());
        try {
            userService.addRating(we, wR);
            if (!wC.trim().isEmpty()) userService.addComment(we, new Comment(wUser.getUsername(), wC));
            userService.addWatchEntry(we);
            int mId = MediaRepository.getInstance().findIdByTitle(wMedia.getTitle());
            if (mId != -1) WatchEntryRepository.getInstance().create(we, mId, null, null);
            System.out.println("Watch entry adaugat cu succes.");
        } catch (InvalidRatingException e) {
            System.out.println("Eroare: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("[DB] " + e.getMessage());
        }
    }

    static void adaugaReview() {
        audit.log("adauga_review");
        System.out.print("Username: "); String rU = scanner.nextLine();
        System.out.print("Titlu media: "); String rM = scanner.nextLine();
        System.out.print("Titlu episod (gol daca film): "); String rE = scanner.nextLine();
        System.out.print("Rating (0-10): "); double rR = scanner.nextDouble(); scanner.nextLine();
        System.out.print("Autor comentariu: "); String rCA = scanner.nextLine();
        System.out.print("Text comentariu: "); String rCT = scanner.nextLine();

        User rUser = userService.findUserByUsername(rU);
        Media rMedia = userService.findMediaByExactTitle(rM);
        if (rUser == null) { System.out.println("Utilizatorul nu exista."); return; }
        if (rMedia == null) { System.out.println("Media nu exista."); return; }

        Episode rEp = null;
        if (rMedia instanceof Series && !rE.trim().isEmpty()) {
            rEp = userService.findEpisodeByTitle((Series) rMedia, rE);
        }

        WatchEntry re = new WatchEntry(rUser, rMedia, rEp, LocalDate.now());
        try {
            userService.addRating(re, rR);
            userService.addComment(re, new Comment(rCA, rCT));
            userService.addWatchEntry(re);
            int mId = MediaRepository.getInstance().findIdByTitle(rMedia.getTitle());
            if (mId != -1) WatchEntryRepository.getInstance().create(re, mId, null, null);
            System.out.println("Review adaugat:\n" + re);
        } catch (InvalidRatingException e) {
            System.out.println("Eroare: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("[DB] " + e.getMessage());
        }
    }
}
