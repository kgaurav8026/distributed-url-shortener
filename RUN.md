# 🚀 How to Run the URL Shortener Project

This guide provides step-by-step instructions to run the Distributed URL Shortener application.

---

## 📋 Prerequisites

Before running the project, ensure you have the following installed:

- ✅ **Java 17 or higher** - [Download JDK](https://www.oracle.com/java/technologies/downloads/)
- ✅ **Maven 3.6+** - [Download Maven](https://maven.apache.org/download.cgi)
- ✅ **Docker Desktop** - [Download Docker](https://www.docker.com/products/docker-desktop/)
- ✅ **Git** - [Download Git](https://git-scm.com/downloads)

### Verify Installation

```bash
# Check Java version
java -version

# Check Maven version
mvn -version

# Check Docker version
docker --version
docker-compose --version
```

---

## 🏃 Quick Start (3 Steps)

### Step 1: Start Docker Services

Open a terminal in the project directory and run:

```bash
docker-compose up -d
```

This will start:
- **ZooKeeper** on port `2181`
- **Redis** on port `6379`

**Verify services are running:**

```bash
docker ps
```

You should see two containers:
- `url-shortener-zookeeper`
- `url-shortener-redis`

---

### Step 2: Build the Application

```bash
mvn clean package
```

This will:
- Compile the source code
- Run tests
- Create a JAR file in the `target` directory

---

### Step 3: Run the Application

```bash
java -jar target/distributed-url-shortner-1.0-SNAPSHOT.jar
```

Or use the provided batch file (Windows):

```bash
start.bat
```

---

## 🌐 Access the Application

Once the application starts successfully, open your browser:

### Main Application
```
http://localhost:8080
```

### Health Check (Optional)
```
http://localhost:8080/actuator/health
```

---

## 🎯 Using the Application

### Shorten a URL

1. **Open** `http://localhost:8080` in your browser
2. **Enter** a long URL in the input field (e.g., `https://www.example.com/very/long/url`)
3. **Click** the "Shorten URL" button
4. **Copy** the shortened URL displayed
5. **Click** "Open Short URL" to test it

### API Usage

**Shorten URL (POST):**

```bash
curl -X POST http://localhost:8080/api/shorten \
  -H "Content-Type: application/json" \
  -d "{\"originalUrl\": \"https://www.example.com\"}"
```

**Response:**
```json
{
  "shortUrl": "http://localhost:8080/abc123",
  "originalUrl": "https://www.example.com"
}
```

**Redirect (GET):**

```bash
curl -L http://localhost:8080/abc123
```

This will redirect you to the original URL.

---

## 🛑 Stopping the Application

### Stop Spring Boot Application

Press `Ctrl + C` in the terminal where the application is running.

### Stop Docker Services

```bash
docker-compose down
```

**To stop and remove volumes:**

```bash
docker-compose down -v
```

---

## 🔧 Configuration Options

### Change Server Port

Edit `src/main/resources/application.properties`:

```properties
server.port=8080
```

Or set environment variable:

```bash
export SERVER_PORT=9090
java -jar target/distributed-url-shortner-1.0-SNAPSHOT.jar
```

### Custom ZooKeeper/Redis Hosts

If running ZooKeeper or Redis on different hosts:

```bash
export ZOOKEEPER_HOST=your-zookeeper-host:2181
export REDIS_HOST=your-redis-host
export REDIS_PORT=6379
java -jar target/distributed-url-shortner-1.0-SNAPSHOT.jar
```

---

## 📦 Running Different Instances

To run multiple instances (for distributed testing):

### Instance 1 (Port 8080):
```bash
java -jar target/distributed-url-shortner-1.0-SNAPSHOT.jar
```

### Instance 2 (Port 8081):
```bash
java -Dserver.port=8081 -jar target/distributed-url-shortner-1.0-SNAPSHOT.jar
```

### Instance 3 (Port 8082):
```bash
java -Dserver.port=8082 -jar target/distributed-url-shortner-1.0-SNAPSHOT.jar
```

All instances will share the same ZooKeeper and Redis backend.

---

## 🔍 Verify Everything is Working

### Check Docker Services

```bash
# Check if containers are running
docker ps

# Check ZooKeeper logs
docker logs url-shortener-zookeeper

# Check Redis logs
docker logs url-shortener-redis
```

### Check Application Logs

Watch the console output for:
- ✅ `Started UrlShortenerApplication`
- ✅ `Tomcat started on port(s): 8080`
- ✅ ZooKeeper connection messages
- ✅ Redis connection messages

---

## 🎓 Development Mode

For development with auto-reload:

```bash
mvn spring-boot:run
```

This allows you to make code changes and restart quickly.

---

## 🐳 Using Only Docker (Future Enhancement)

Currently, the Spring Boot application runs outside Docker. To run everything in Docker:

1. Build Docker image:
```bash
docker build -t url-shortener-app .
```

2. Update `docker-compose.yml` to include the app service

3. Run:
```bash
docker-compose up -d
```

---

## 📝 Next Steps

- ✅ Application is running
- 📖 See [TROUBLESHOOTING.md](TROUBLESHOOTING.md) if you encounter issues
- 🎨 Customize the UI in `src/main/resources/static/`
- 🔧 Modify configuration in `application.properties`

---

## 💡 Tips

- **First Run**: May take a few minutes to download Docker images
- **Port Conflicts**: If ports 2181, 6379, or 8080 are in use, change them in `docker-compose.yml` or `application.properties`
- **Performance**: For production, adjust Redis and ZooKeeper memory settings
- **Data Persistence**: Named volumes ensure data persists across container restarts

---

## ✅ Success Indicators

You'll know everything is working when:

1. ✅ Docker containers are running: `docker ps` shows 2 containers
2. ✅ Application starts without errors
3. ✅ You can access `http://localhost:8080`
4. ✅ You can shorten a URL and access it

---

**Happy URL Shortening! 🎉**

For issues, check [TROUBLESHOOTING.md](TROUBLESHOOTING.md)

