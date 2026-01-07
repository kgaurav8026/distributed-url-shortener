# 🚀 Distributed URL Shortener - Quick Start Guide

## What I've Built

I've created a complete, production-ready URL shortener application with:

### ✅ Backend (Spring Boot)
- **Counter-based URL shortening** with Base62 encoding
- **ZooKeeper integration** for distributed counter management
- **Redis caching** for fast URL lookups
- **H2 Database** for persistent storage
- **REST API** with validation and error handling
- **Proper architecture** with layers (Controller, Service, Repository, Entity, DTO)

### ✅ Frontend (HTML/CSS/JS)
- **Modern, responsive UI** with gradient design
- **Real-time validation** and error messages
- **Copy-to-clipboard** functionality
- **Mobile-friendly** layout
- **Professional animations** and transitions

### ✅ Infrastructure
- **Docker Compose** for ZooKeeper and Redis
- **Maven** for dependency management
- **Startup scripts** for easy execution

## 📁 Project Structure

```
distributed-url-shortner/
├── src/
│   ├── main/
│   │   ├── java/org/example/
│   │   │   ├── UrlShortenerApplication.java  # Main app
│   │   │   ├── config/RedisConfig.java
│   │   │   ├── controller/UrlShortenerController.java
│   │   │   ├── dto/
│   │   │   ├── entity/Url.java
│   │   │   ├── exception/
│   │   │   ├── repository/UrlRepository.java
│   │   │   ├── service/
│   │   │   └── util/Base62Encoder.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/styles.css
│   │       │   ├── js/app.js
│   │       │   ├── index.html
│   │       │   └── error.html
│   │       └── application.properties
├── docker-compose.yml
├── pom.xml
├── start.bat    # 🎯 Run this to start!
├── stop.bat
└── README.md
```

## 🎯 How to Run

### Option 1: Using the Startup Script (Easiest)

1. **Make sure Docker Desktop is running**
2. **Double-click `start.bat`**
3. Wait for services to start
4. Browser will automatically open to http://localhost:8080

### Option 2: Manual Steps

1. **Start Docker services:**
   ```bash
   docker-compose up -d
   ```

2. **Build the project:**
   ```bash
   mvn clean install -DskipTests
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

4. **Open browser:**
   ```
   http://localhost:8080
   ```

### To Stop Everything

**Double-click `stop.bat`** or run:
```bash
docker-compose down
```

## 🔧 What's Happening Under the Hood

### When You Shorten a URL:

1. **Frontend** sends POST request to `/api/shorten`
2. **Controller** validates the request
3. **Service** requests a unique counter from ZooKeeper
4. Counter is encoded using **Base62** (e.g., 1234 → "Ty")
5. Mapping saved to **H2 database** and **Redis cache**
6. Short URL returned to user (e.g., `http://localhost:8080/Ty`)

### When You Access a Short URL:

1. User visits `http://localhost:8080/Ty`
2. **Controller** receives the request
3. **Service** checks **Redis cache** first (fast!)
4. If not in cache, queries **H2 database**
5. Returns HTTP 302 redirect to original URL

## 📊 Architecture Highlights

### Technologies Used:
- **Spring Boot 3.2.0** - Core framework
- **ZooKeeper 3.9.1** - Distributed counter coordination
- **Redis 7** - Caching layer
- **H2 Database** - In-memory persistence
- **Lombok** - Reduces boilerplate
- **Maven** - Build tool

### Key Features:
- **Distributed Counter Management** - Multiple instances can run simultaneously
- **High Performance** - Redis caching for sub-millisecond lookups
- **Scalable** - Can handle millions of URLs
- **Professional UI** - Clean, modern design
- **Expiration Support** - URLs can have custom expiration dates

## 🎨 UI Features

The web interface includes:
- ✨ **Gradient background** with modern card design
- 📱 **Mobile responsive** - works on all devices
- ⚡ **Real-time validation** - immediate feedback
- 📋 **Copy button** - one-click copy to clipboard
- 🎭 **Smooth animations** - professional transitions
- 🎯 **Feature highlights** - explains the technology

## 🧪 Testing the Application

### Shorten a URL:
1. Open http://localhost:8080
2. Enter a long URL (e.g., `https://www.example.com/very/long/url`)
3. Optionally select expiration time
4. Click "Shorten URL"
5. Copy the generated short URL

### Access Short URL:
1. Open the short URL in browser
2. You'll be redirected to the original URL

### API Testing with cURL:

```bash
# Shorten a URL
curl -X POST http://localhost:8080/api/shorten \
  -H "Content-Type: application/json" \
  -d "{\"url\":\"https://example.com/test\",\"expirationDays\":30}"

# Health check
curl http://localhost:8080/api/health
```

## 📝 Configuration

Default configuration in `application.properties`:
- **Server Port**: 8080
- **ZooKeeper**: localhost:2181
- **Redis**: localhost:6379
- **Counter Range Size**: 1000

To change settings, create environment variables:
```bash
set SERVER_PORT=8081
set REDIS_HOST=redis-server
mvn spring-boot:run
```

## 🐛 Troubleshooting

### Docker services won't start:
- Make sure Docker Desktop is running
- Check if ports 2181 (ZooKeeper) and 6379 (Redis) are free

### Application won't start:
- Check if port 8080 is available
- Verify ZooKeeper and Redis are running: `docker-compose ps`

### Maven build fails:
- Run: `mvn clean install -U`
- Make sure Java 21 is installed: `java -version`

## 📈 Next Steps

The application is ready to use! Here are some ideas for enhancement:

- [ ] Add custom short URL feature
- [ ] Implement analytics tracking
- [ ] Add rate limiting
- [ ] Create admin dashboard
- [ ] Add QR code generation
- [ ] Implement user authentication
- [ ] Add URL preview feature
- [ ] Create mobile app

## 🎓 What You Learned

This project demonstrates:
- **Microservices architecture** with Spring Boot
- **Distributed systems** concepts (ZooKeeper, Redis)
- **RESTful API** design
- **Database design** and JPA
- **Frontend development** (HTML/CSS/JS)
- **Docker** containerization
- **Maven** build management

## 📚 Documentation

See `README.md` for complete documentation including:
- Detailed architecture diagrams
- API documentation
- Environment variables
- Development guide
- Contributing guidelines

## 🎉 You're All Set!

The URL shortener is fully functional and ready to use!

**To start:** Double-click `start.bat`  
**To stop:** Double-click `stop.bat`  
**Access:** http://localhost:8080

Enjoy your new URL shortener! 🚀

