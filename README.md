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

## Most recent updates

**Database integration:** the project now connects to a local PostgreSQL database (`TvTime`) at startup using a `DatabaseConnection` Singleton. CRUD operations for users, media, episodes, and watch entries are exposed through a generic `Repository<T>` interface with concrete implementations (`UserRepository`, `MediaRepository`, `EpisodeRepository`, `WatchEntryRepository`), each using the Singleton connection.

**Audit service:** every menu action is logged with a timestamp to `data/audit.csv` by `AuditService` (Singleton). The CSV is created automatically on first run with a `nume_actiune,timestamp` header.

**Watchlist:** users can now add, view, and remove titles from a personal watchlist (menu options 19–21).

The service layer remains role-separated:

- `UserService` contains functionality available to any user:
  - `addComment()`, `addRating()`, `showWatchHistory()`, `filterCommentsByRating()`
  - watchlist operations: `addToWatchlist()`, `showWatchlist()`, `removeFromWatchlist()`
  - user/profile/media browsing helpers
- `AdminService` extends `UserService` and contains admin-only operations:
  - `addMedia()`, `deleteMedia()`, `addEpisode()`, `deleteUser()`, `createPost()`

Both services work on the same in-memory state through `ServiceData`.

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
|   |-- movies.txt
|   |-- series.txt
|   `-- users.txt
|-- src/
|   `-- main/
|       `-- java/
|           |-- Admin.java
|           |-- AdminService.java
|           |-- AuditService.java
|           |-- Character.java
|           |-- Comment.java
|           |-- DatabaseConnection.java
|           |-- Episode.java
|           |-- EpisodeRepository.java
|           |-- FileService.java
|           |-- Main.java
|           |-- Media.java
|           |-- MediaRepository.java
|           |-- Movie.java
|           |-- Post.java
|           |-- Repository.java
|           |-- Series.java
|           |-- ServiceData.java
|           |-- User.java
|           |-- UserRepository.java
|           |-- UserService.java
|           |-- WatchEntry.java
|           `-- WatchEntryRepository.java
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
