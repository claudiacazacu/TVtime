# TV Time

TV Time is a console-based Java application for managing users, movies, series, reviews, and watch history in a small personal watch-tracking system. The project focuses on object-oriented design and uses text files as seed data.

![Status](https://img.shields.io/badge/status-active-brightgreen)
![Language](https://img.shields.io/badge/language-Java-orange)
![OOP](https://img.shields.io/badge/OOP-project-blue)

## Overview

The application loads users, movies, and series from the `data/` folder, connects to a PostgreSQL database, and exposes a CLI menu for browsing and updating the library.

The current implementation includes:

- a `Media` hierarchy with `Movie` and `Series`
- episode support through the `Episode` class
- user management through the `User` model
- admin support through `Admin extends User`
- service separation through `UserService` and `AdminService`
- title search and genre filtering
- cast storage on each media item
- review data through `WatchEntry` and `Comment`
- watch history, profile display, and watchlist management
- admin post support through `Post`
- file-based bootstrap data via `FileService`
- PostgreSQL persistence via a `Repository<T>` interface and concrete implementations
- audit logging to `data/audit.csv` via `AuditService` (Singleton)
- 5 excepții custom în pachetul `exception/`

## Most recent updates

**Exception handling:** the project defines 5 custom checked exceptions in the `exception/` package: `MediaNotFoundException`, `UserNotFoundException`, `InvalidRatingException`, `UnauthorizedAccessException`, `DuplicateMediaException`. These are thrown by `AdminService` and `UserService` and caught with `try-catch` in `Main`.

**Database integration:** the project connects to a local PostgreSQL database (`TvTime`) at startup using a `DatabaseConnection` Singleton. CRUD operations for users, media, episodes, and watch entries are exposed through a generic `Repository<T>` interface with concrete implementations (`UserRepository`, `MediaRepository`, `EpisodeRepository`, `WatchEntryRepository`).

**Audit service:** every menu action is logged with a timestamp to `data/audit.csv` by `AuditService` (Singleton). The CSV is created automatically on first run with a `nume_actiune,timestamp` header.

**Watchlist:** users can add, view, and remove titles from a personal watchlist (menu options 19–21).

The service layer is role-separated:

- `UserService` contains functionality available to any user:
  - `addComment()`, `addRating()` *(throws `InvalidRatingException`)*
  - `showUserProfile()`, `showCommentsForMedia()` *(throws `UserNotFoundException` / `MediaNotFoundException`)*
  - `showWatchlist()`, `showRecommendationsForUser()` *(throws `UserNotFoundException`)*
  - watchlist operations: `addToWatchlist()`, `removeFromWatchlist()`
- `AdminService` extends `UserService` and contains admin-only operations:
  - `addMedia()` *(throws `UnauthorizedAccessException`, `DuplicateMediaException`)*
  - `deleteMedia()` *(throws `UnauthorizedAccessException`, `MediaNotFoundException`)*
  - `addEpisode()` *(throws `UnauthorizedAccessException`, `MediaNotFoundException`)*
  - `deleteUser()` *(throws `UnauthorizedAccessException`, `UserNotFoundException`)*
  - `createPost()` *(throws `UnauthorizedAccessException`)*

Both services work on the same in-memory state through `ServiceData`.

## Tipuri de obiecte

| Nr. | Tip | Descriere |
|-----|-----|-----------|
| 1 | `Media` | Clasă abstractă de bază pentru orice conținut media |
| 2 | `Movie` | Film, extinde `Media` |
| 3 | `Series` | Serial TV, extinde `Media`; conține o listă de `Episode` |
| 4 | `Episode` | Episod dintr-un serial, cu număr, sezon și durată |
| 5 | `User` | Utilizator al aplicației (username, vârstă, email, watchlist) |
| 6 | `Admin` | Utilizator cu drepturi de administrare, extinde `User` |
| 7 | `WatchEntry` | Înregistrare de vizionare: leagă un `User` de un titlu/episod, rating și comentariu |
| 8 | `Comment` | Comentariu asociat unui `WatchEntry` |
| 9 | `Character` | Personaj dintr-o producție (parte din cast) |
| 10 | `Post` | Postare publicată de un `Admin` |
| 11 | `ServiceData` | Container de stare în memorie partajat între servicii |
| 12 | `Repository<T>` | Interfață generică pentru operații CRUD în baza de date |
| 13 | `MediaNotFoundException` | Excepție aruncată când un titlu nu este găsit |
| 14 | `UserNotFoundException` | Excepție aruncată când un utilizator nu există |
| 15 | `InvalidRatingException` | Excepție aruncată când ratingul este în afara intervalului 0–10 |
| 16 | `UnauthorizedAccessException` | Excepție aruncată la tentativa de operație admin fără drepturi |
| 17 | `DuplicateMediaException` | Excepție aruncată la adăugarea unui titlu deja existent |

## Acțiuni & Interogări

| Număr | Acțiune | Serviciu | Descriere |
|-------|---------|----------|-----------|
| 1 | Afișare utilizatori | `UserService` | Listează toți utilizatorii înregistrați |
| 2 | Afișare media | `UserService` | Listează toate filmele și serialele |
| 3 | Afișare genuri | `UserService` | Afișează toate genurile disponibile |
| 4 | Adaugă utilizator | `UserService` | Creează un utilizator nou (username, vârstă, email) |
| 5 | Adaugă film | `AdminService` | Adaugă un film nou (doar admin) |
| 6 | Adaugă serial | `AdminService` | Adaugă un serial nou (doar admin) |
| 7 | Caută după titlu | `UserService` | Caută titluri după un șir de caractere |
| 8 | Afișare doar filme | `UserService` | Filtrează și afișează numai filmele |
| 9 | Afișare doar seriale | `UserService` | Filtrează și afișează numai serialele |
| 10 | Filtrare după gen | `UserService` | Returnează toate titlurile dintr-un gen specificat |
| 11 | Afișare cast | `UserService` | Afișează distribuția (cast) pentru o producție |
| 12 | Adaugă episod | `AdminService` | Adaugă un episod la un serial existent (doar admin) |
| 13 | Review cu rating și comentariu | `UserService` | Creează un `WatchEntry` complet cu rating și comentariu |
| 14 | Creare watch entry | `UserService` | Înregistrează o vizionare, opțional cu comentariu și personaj favorit |
| 15 | Afișare comentarii | `UserService` | Afișează toate comentariile pentru o producție |
| 16 | Afișare profil | `UserService` | Afișează profilul unui utilizator cu istoricul de vizionări |
| 17 | Top săptămână | `UserService` | Listează cele mai populare titluri pe baza vizionărilor |
| 18 | Recomandări personalizate | `UserService` | Sugerează titluri pe baza genurilor urmărite de utilizator |
| 19 | Adaugă în watchlist | `UserService` | Adaugă un titlu în watchlist-ul unui utilizator |
| 20 | Afișare watchlist | `UserService` | Afișează toate titlurile din watchlist-ul unui utilizator |
| 21 | Șterge din watchlist | `UserService` | Elimină un titlu din watchlist-ul unui utilizator |
| 22 | Ieșire | — | Închide aplicația |

## Implemented Menu Actions

When `Main` runs, the CLI offers these actions:

1. Show all users
2. Show all media
3. Show all genres
4. Add a user
5. Add a movie
6. Add a series
7. Search media by title
8. Show only movies
9. Show only series
10. Filter media by genre
11. Show cast for a production
12. Add an episode to a series
13. Create a watch entry review with rating and comment
14. Create a watch entry
15. Show comments for a production
16. Show user profile
17. Weekly top
18. Personalized recommendations
19. Add to watchlist
20. Show user watchlist
21. Remove from watchlist
22. Exit


## Project Structure

```text
TVtime/
|-- data/
|   |-- audit.csv
|   |-- movies.txt
|   |-- series.txt
|   `-- users.txt
|-- src/
|   `-- main/
|       `-- java/
|           |-- config/
|           |   `-- DatabaseConnection.java
|           |-- model/
|           |   |-- Admin.java
|           |   |-- Character.java
|           |   |-- Comment.java
|           |   |-- Episode.java
|           |   |-- Media.java
|           |   |-- Movie.java
|           |   |-- Post.java
|           |   |-- Series.java
|           |   |-- User.java
|           |   `-- WatchEntry.java
|           |-- repository/
|           |   |-- EpisodeRepository.java
|           |   |-- MediaRepository.java
|           |   |-- Repository.java
|           |   |-- UserRepository.java
|           |   `-- WatchEntryRepository.java
|           |-- service/
|           |   |-- AdminService.java
|           |   |-- AuditService.java
|           |   |-- FileService.java
|           |   |-- ServiceData.java
|           |   `-- UserService.java
|           |-- exception/
|           |   |-- DuplicateMediaException.java
|           |   |-- InvalidRatingException.java
|           |   |-- MediaNotFoundException.java
|           |   |-- UnauthorizedAccessException.java
|           |   `-- UserNotFoundException.java
|           `-- Main.java
`-- README.md
```

## Data Files

The application reads starter records from:

- `data/users.txt`
- `data/movies.txt`
- `data/series.txt`

Each file uses comma-separated values and is loaded at startup by `FileService`. All subsequent write operations go to the PostgreSQL database. Audit logs are written to `data/audit.csv`.


## Academic Context

PAO - FMI - UNIBUC
