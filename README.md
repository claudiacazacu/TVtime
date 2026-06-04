# TV Time

Aplicație Java de tip consolă pentru urmărirea filmelor și serialelor vizionate, cu suport pentru utilizatori, recenzii, recomandări și administrare completă prin CRUD.

![Status](https://img.shields.io/badge/status-active-brightgreen)
![Language](https://img.shields.io/badge/language-Java-orange)
![OOP](https://img.shields.io/badge/OOP-PAO-blue)

---

## Descriere

TV Time permite utilizatorilor să înregistreze vizionări, să adauge recenzii cu rating și comentariu, să vadă topul săptămânii și să primească recomandări personalizate pe baza preferințelor lor. Adminul gestionează biblioteca de conținut prin operații CRUD complete, persistate în PostgreSQL.

---

## Concepte POO utilizate

| Concept | Implementare |
|---------|-------------|
| **Clasă abstractă** | `Media` — bază pentru `Movie` și `Series` |
| **Moștenire** | `Movie extends Media`, `Series extends Media`, `Admin extends User` |
| **Polimorfism** | Colecții de tip `List<Media>` conțin atât `Movie` cât și `Series` |
| **Encapsulare** | Atribute `private` cu getteri/setteri în toate modelele |
| **Interfață generică** | `Repository<T>` cu metode CRUD abstracte |
| **Design Pattern Singleton** | `DatabaseConnection`, `AuditService` |
| **Design Pattern Service Layer** | `UserService`, `AdminService` separă logica de UI |
| **Colecții** | `List`, `Set`, `TreeSet`, `Map`, `HashMap`, `HashSet` |
| **Stream API** | Filtrări, sortări și agregări în `UserService` |
| **Excepții custom** | 5 excepții checked în pachetul `exception/` |
| **Fișiere** | Citire date inițiale din CSV, scriere audit în CSV |
| **Baze de date** | JDBC + PostgreSQL, CRUD complet pe 4 entități |

---

## Tipuri de obiecte

| Nr. | Tip | Descriere |
|-----|-----|-----------|
| 1 | `Media` | Clasă abstractă de bază pentru orice conținut media |
| 2 | `Movie` | Film, extinde `Media` |
| 3 | `Series` | Serial TV, extinde `Media`; conține o listă de `Episode` |
| 4 | `Episode` | Episod dintr-un serial, cu număr, sezon și durată |
| 5 | `User` | Utilizator al aplicației (username, vârstă, email) |
| 6 | `Admin` | Utilizator cu drepturi de administrare, extinde `User` |
| 7 | `WatchEntry` | Înregistrare de vizionare: leagă un `User` de un titlu, rating și comentariu |
| 8 | `Comment` | Comentariu asociat unui `WatchEntry` |
| 9 | `Character` | Personaj dintr-o producție (parte din cast) |
| 10 | `Post` | Postare publicată de un `Admin` |
| 11 | `ServiceData` | Container de stare în memorie partajat între servicii |
| 12 | `Repository<T>` | Interfață generică pentru operații CRUD în baza de date |
| 13 | `MediaNotFoundException` | Excepție aruncată când un titlu nu este găsit |
| 14 | `UserNotFoundException` | Excepție aruncată când un utilizator nu există |
| 15 | `InvalidRatingException` | Excepție aruncată când ratingul este în afara intervalului 0–10 |
| 16 | `UnauthorizedAccessException` | Excepție aruncată la operație admin fără drepturi |
| 17 | `DuplicateMediaException` | Excepție aruncată la adăugarea unui titlu deja existent |

---

## Acțiuni & Interogări

| Nr. | Acțiune | Serviciu | Descriere |
|-----|---------|----------|-----------|
| 1 | Top săptămână | `UserService` | Calculează scorul de popularitate pentru fiecare titlu |
| 2 | Recomandări personalizate | `UserService` | Sugerează titluri pe baza genurilor preferate și ratingului comunității |
| 3 | Adaugă watch entry | `UserService` | Înregistrează o vizionare cu rating și comentariu opțional |
| 4 | Adaugă review | `UserService` | Creează un `WatchEntry` complet cu rating și comentariu |
| 5 | Afișează profil | `UserService` | Afișează istoricul de vizionări al unui utilizator |
| 6 | CRUD Utilizator | `UserRepository` | Create / Read / Update / Delete utilizatori în PostgreSQL |
| 7 | CRUD Film | `MediaRepository` | Create / Read / Update / Delete filme în PostgreSQL |
| 8 | CRUD Serial | `MediaRepository` | Create / Read / Update / Delete seriale în PostgreSQL |
| 9 | CRUD Episod | `EpisodeRepository` | Create / Read / Update / Delete episoade în PostgreSQL |
| 10 | Adaugă episod | `AdminService` | Adaugă un episod la un serial existent |
| 11 | Creează post | `AdminService` | Publică o postare de admin |

---

## Meniu

```
TV TIME
├── 1. Top săptămânii
├── 2. Recomandări personalizate
├── 3. Adaugă watch entry
├── 4. Adaugă review (rating + comentariu)
├── 5. Afișează profil utilizator
└── 99. ADMIN
      ├── 1. CRUD
      │     ├── 1. Utilizator  →  Create / Read / Update / Delete
      │     ├── 2. Film        →  Create / Read / Update / Delete
      │     ├── 3. Serial      →  Create / Read / Update / Delete
      │     └── 4. Episod      →  Create / Read / Update / Delete
      ├── 2. Adaugă episod la serial
      └── 3. Creează post
```

---

## Structura proiectului

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

---

## Date inițiale

La pornire, aplicația se conectează la PostgreSQL și încarcă automat toți utilizatorii și toate titlurile din baza de date în memorie. Dacă conexiunea eșuează, aplicația nu pornește.

Fiecare acțiune din meniu este logată cu timestamp în `data/audit.csv` de către `AuditService`.

---

## Baza de date

Conexiunea la PostgreSQL se face prin `DatabaseConnection` (Singleton) la adresa `localhost:5432/TvTime`. Tabelele utilizate: `users`, `media`, `episodes`, `watch_entries`.

---

*PAO — FMI — UNIBUC*
