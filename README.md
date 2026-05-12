# DrawDuel

A real-time multiplayer drawing and guessing game where two players compete head-to-head — one draws a secret word while the other races to guess it, swapping roles each round.

---

## Tech Stack

- **Frontend**: React 19, TypeScript, Vite, Konva (canvas), Tailwind CSS, Framer Motion
- **Backend**: Java 21, Spring Boot 3.5, Spring WebSocket, Spring Security + JWT
- **Database**: PostgreSQL 16 with Flyway migrations
- **Infrastructure**: Docker, Docker Compose, Nginx (reverse proxy + WebSocket)

---

## Key Features

- **Real-time WebSocket multiplayer** with automatic matchmaking — join the queue and get paired instantly
- **Synchronized drawing canvas** — strokes are transmitted live to the opponent via WebSocket using Konva
- **Dynamic scoring** — points scale with remaining time and penalize excess guesses, rewarding both speed and accuracy
- **Multi-device session management** — track and revoke active sessions per device with user-agent parsing
- **Player dashboard** — match history, win/loss stats, games-per-day chart, and leaderboard

---

## How to Run

### Docker (recommended)

**Prerequisites:** Docker and Docker Compose installed.

1. Create the external network:
   ```bash
   docker network create drawduel-net
   ```

2. Create `.env.db` in the project root:
   ```env
   POSTGRES_USER=drawduel
   POSTGRES_PASSWORD=yourpassword
   POSTGRES_DB=drawduel
   ```

3. Create `backend/infrastructure/.env.docker`:
   ```env
   DB_HOST=db
   DB_PORT=5432
   DB_NAME=drawduel
   DB_USER=drawduel
   DB_PASSWORD=yourpassword
   JWT_SECRET=your_long_random_secret_here
   JWT_EXPIRATION_TIME=900000
   CLOUDINARY_URL=cloudinary://<api_key>:<api_secret>@<cloud_name>
   SPRING_PROFILES_ACTIVE=docker
   ```

4. Start all services:
   ```bash
   docker-compose up -d
   ```

The app will be available at **http://localhost:3000**.

| Service   | URL                              |
|-----------|----------------------------------|
| Frontend  | http://localhost:3000            |
| Backend   | http://localhost:8080            |
| WebSocket | ws://localhost:3000/ws/drawduel  |
| Database  | localhost:5432                   |

### Local Development

**Backend:**
```bash
cd backend
# Create backend/infrastructure/.env.local with local DB credentials
./gradlew bootRun
```

**Frontend:**
```bash
cd frontend
npm install
npm run dev   # http://localhost:5173
```

---

## Known Limitations

- **Static word pool** — only 10 hardcoded words; no difficulty levels or custom word lists
- **In-memory game state** — active game sessions are not persisted; a server restart ends ongoing matches and the app cannot be horizontally scaled as-is
- **Location tracking unimplemented** — session location defaults to "unknown"
- **No rate limiting** — WebSocket connections and registration endpoints have no abuse protection
- **Fixed match format** — always 4 rounds, no configurable length
