# Dota 2 Match Harvester (Micronaut + PostgreSQL)

This project fetches public Dota 2 matches from the [OpenDota API](https://docs.opendota.com/), stores them in a PostgreSQL database, and processes their statistics for further analysis and machine learning pipelines.

---

## 🚀 Features

- **Micronaut Framework** for a lightweight and fast JVM application.
- **Reactive Scheduling** using `@Scheduled` to periodically fetch matches.
- **OpenDota API Integration** via a custom HTTP client.
- **Hibernate / JPA** for database persistence.
- **PostgreSQL** with array and numeric fields to store match advantage timelines.
- **Error Handling & Logging** with `Slf4j` for clear monitoring of fetch/save steps.

---

## 📂 Project Structure

```
/src/main/java/com/senai/ml/t1/dota
│
├── clients/opendota/          # OpenDota API client & request beans
│   └── OpenDotaClient.java
│
├── models/
│   ├── entities/              # Database entities (JPA + Micronaut Data)
│   │   └── MatchEntity.java
│   └── opendota/              # Models mapped to OpenDota API responses
│       └── OpenDotaMatch.java
│
├── schedulers/
│   └── FetchMatchesScheduler.java  # Periodic match fetch & save logic
│
├── services/
│   └── match/                 # Business logic for saving & validating matches
│       └── MatchService.java
│
└── helper/
    └── MathHelper.java        # Utility for standard deviation & math operations
```

---

## 🗄 Database Schema

Main table: `match`

| Column                         | Type        | Description                                   |
|--------------------------------|-------------|-----------------------------------------------|
| `id`                           | BIGINT      | Match ID (primary key)                        |
| `first_blood_time`             | INT         | Time of first blood (seconds)                 |
| `radiant_win`                  | BOOLEAN     | Did Radiant win?                              |
| `radiant_gold_advantage`       | INT[]       | Timeline of Radiant gold advantage            |
| `radiant_experience_advantage` | INT[]       | Timeline of Radiant XP advantage              |
| `min_radiant_gold_advantage`   | INT         | Minimum gold advantage                        |
| `max_radiant_gold_advantage`   | INT         | Maximum gold advantage                        |
| `mean_radiant_gold_advantage`  | DOUBLE      | Average gold advantage                        |
| `std_dev_radiant_gold_advantage` | DOUBLE    | Standard deviation of gold advantage          |
| `final_radiant_gold_advantage` | INT         | Final gold advantage at end of match          |

---

## ⚙️ How It Works

1. Every **60 seconds**, the scheduler requests 100 public matches from OpenDota.
2. For each match:
   - It checks if the match is already stored.
   - Fetches detailed match data from OpenDota.
   - Calculates aggregated statistics:
     - min / max / mean / std-dev gold advantage
     - final gold advantage
   - Saves everything into PostgreSQL.
3. Errors are logged, but the scheduler continues running.

---

## 🧰 Technologies Used

- **Java 17**
- **Micronaut 4**
- **Reactor (Flux/Mono)**
- **Hibernate / JPA**
- **PostgreSQL**
- **Lombok**

---

## 🔧 Setup

### Prerequisites
- Java 17+
- PostgreSQL running locally or in Docker
- Maven

### Environment Variables
Set in `application.yml` or as environment variables:

```yaml
datasources:
  default:
    url: jdbc:postgresql://localhost:5432/dota
    username: postgres
    password: postgres
    driverClassName: org.postgresql.Driver
```

### Run the Project

```bash
./mvnw clean install
./mvnw mn:run
```

---

## 📈 Future Improvements

- Add **rate-limiting** to comply with OpenDota request limits.
- Store **hero-level data** (players, picks, bans).
- Expose REST endpoints to query stored matches.
- Add **machine learning pipeline** integration for prediction tasks.

---

## 📜 License

MIT License. Free to use and adapt.
