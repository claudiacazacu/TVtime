# TV Time

TV Time is a console-based Java application for managing users, movies, and TV series in a small personal watch-tracking system. The project focuses on object-oriented design and uses text files as seed data.

![Status](https://img.shields.io/badge/status-active-brightgreen)
![Language](https://img.shields.io/badge/language-Java-orange)
![OOP](https://img.shields.io/badge/OOP-project-blue)

## Overview

The application loads users, movies, and series from the `data/` folder, stores them in memory, and exposes a CLI menu for browsing and updating the library.

The current implementation includes:

- a `Media` hierarchy with `Movie` and `Series`
- episode support through the `Episode` class
- user management through the `User` model
- title search and genre filtering
- cast storage on each media item
- review data through `WatchEntry` and `Comment`
- file-based bootstrap data via `FileService`

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
14. Exit

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
|           |-- Main.java
|           |-- Service.java
|           |-- FileService.java
|           |-- Media.java
|           |-- Movie.java
|           |-- Series.java
|           |-- Episode.java
|           |-- User.java
|           |-- WatchEntry.java
|           |-- Comment.java
|           `-- Character.java
`-- README.md
```

## Data Files

The application reads starter records from:

- `data/users.txt`
- `data/movies.txt`
- `data/series.txt`

Each file uses comma-separated values and is loaded at startup by `FileService`.

## Academic Context

PAO - FMI - UNIBUC
