# Architecture Diagram Description

## System Flow

```
USER INTERACTION
      │
      ▼
┌─────────────────┐
│  Web Browser    │
│  (index.html)   │
└────────┬────────┘
         │ HTTP
         │
         ▼
┌─────────────────────────────────────────┐
│     SPRING BOOT APPLICATION             │
│     (Port 8080)                         │
│                                         │
│  ┌────────────────────────────────┐    │
│  │   REST Controller               │    │
│  │   - POST /api/shorten          │    │
│  │   - GET /{shortUrl}            │    │
│  │   - GET /api/health            │    │
│  └──────────┬─────────────────────┘    │
│             │                           │
│  ┌──────────▼──────────────────────┐   │
│  │   URL Shortener Service         │   │
│  │   - Validate URL                │   │
│  │   - Get counter from ZooKeeper  │   │
│  │   - Encode with Base62          │   │
│  │   - Store in DB & Cache         │   │
│  └──────────┬──────────────────────┘   │
│             │                           │
│   ┌─────────┴───────────┐              │
│   │                     │               │
│   ▼                     ▼               │
│ ┌─────────────┐   ┌──────────────┐     │
│ │   Redis     │   │  H2 Database │     │
│ │   Cache     │   │  (JPA/Hibernate) │ │
│ │ - Fast read │   │  - Persistent │    │
│ │ - 24h TTL   │   │  - Reliable   │    │
│ └─────────────┘   └──────────────┘     │
└──────────┬──────────────────────────────┘
           │
           ▼
    ┌──────────────────┐
    │   ZooKeeper      │
    │   (Port 2181)    │
    │                  │
    │  Counter Manager │
    │  - Allocate      │
    │    ranges (1000) │
    │  - Ensure        │
    │    uniqueness    │
    └──────────────────┘
```

## Data Flow for Shortening URL

1. User enters long URL → Browser
2. Browser sends POST → Spring Boot Controller
3. Controller validates → URL Shortener Service
4. Service requests counter → ZooKeeper
5. ZooKeeper allocates range → Service
6. Service encodes counter → Base62 (e.g., 1234 → "Ty")
7. Service saves to H2 Database
8. Service caches in Redis
9. Service returns short URL → Controller
10. Controller returns JSON → Browser
11. Browser displays result

## Data Flow for Accessing Short URL

1. User clicks short URL → Browser
2. Browser sends GET → Spring Boot Controller
3. Controller extracts shortUrl → URL Shortener Service
4. Service checks Redis cache
   - IF FOUND: Return immediately (fast path)
   - IF NOT: Query H2 Database
5. Service validates expiration
6. Service caches in Redis (for next time)
7. Service returns long URL → Controller
8. Controller sends HTTP 302 Redirect → Browser
9. Browser redirects to original URL

## Component Responsibilities

### Spring Boot Application
- REST API endpoints
- Business logic
- Validation
- Error handling
- Session management

### ZooKeeper
- Distributed counter coordination
- Ensures uniqueness across instances
- High availability
- Fault tolerance

### Redis
- High-speed caching
- Reduces database load
- TTL-based expiration
- Sub-millisecond response times

### H2 Database
- Persistent storage
- ACID compliance
- Backup and recovery
- Historical data

### Base62 Encoder
- Converts numbers to short strings
- Uses: 0-9, A-Z, a-z (62 characters)
- URL-safe
- Compact representation

## Scaling Considerations

### Horizontal Scaling
- Multiple app instances can run simultaneously
- ZooKeeper coordinates counter ranges
- Redis acts as shared cache
- Load balancer distributes requests

### Performance Optimization
- Redis cache hit rate: ~95%
- Database queries: Only on cache miss
- Counter range allocation: Batch (1000)
- Connection pooling: HikariCP

## Security Features
- Input validation
- SQL injection prevention (JPA)
- XSS protection
- CORS configuration
- Error message sanitization

## Monitoring Points
- API response times
- Cache hit/miss ratio
- Database query performance
- ZooKeeper connection health
- Redis memory usage
- Counter range exhaustion

