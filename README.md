# 🚀 Distributed URL Shortener

> A production-ready, scalable URL shortener built with Spring Boot, featuring distributed counter management with ZooKeeper and Redis caching for high performance.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/)
[![Redis](https://img.shields.io/badge/Redis-7-red.svg)](https://redis.io/)
[![ZooKeeper](https://img.shields.io/badge/ZooKeeper-3.9-yellow.svg)](https://zookeeper.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

---

## 📖 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Architecture](#️-architecture)
- [Tech Stack](#️-tech-stack)
- [Quick Start](#-quick-start)
- [API Documentation](#-api-documentation)
- [Project Structure](#-project-structure)
- [Configuration](#️-configuration)
- [Contributing](#-contributing)
- [License](#-license)

---

## 🎯 Overview

This URL Shortener provides a **scalable and efficient** way to shorten long URLs into compact, manageable links. Built with enterprise-grade technologies, it uses a **counter-based mechanism** with **Base62 encoding** to generate unique short URLs.

### Key Highlights

- **Distributed Architecture**: Multiple instances can run simultaneously with ZooKeeper coordination
- **High Performance**: Redis caching provides sub-millisecond response times
- **Scalable Design**: Handles millions of URLs efficiently with distributed counter management
- **Production Ready**: Includes comprehensive error handling, validation, and logging
- **Modern UI**: Beautiful, responsive frontend with smooth animations

---

## ✨ Features

### Core Functionality
- ⚡ **Instant URL Shortening** - Generate short URLs in milliseconds using Base62 encoding
- 🔗 **Smart Caching** - Redis caching with 24-hour TTL for optimal performance
- ⏰ **Custom Expiration** - Set URLs to expire after 1, 7, 30, or 90 days
- 🔄 **Duplicate Detection** - Efficiently reuses short URLs for identical long URLs
- 📋 **One-Click Copy** - Copy short URLs to clipboard instantly

### Technical Features
- 🎯 **Distributed Counters** - ZooKeeper ensures unique IDs across multiple instances
- 💾 **Dual Storage** - H2 database for persistence, Redis for speed
- 🔒 **Input Validation** - Comprehensive URL and parameter validation
- 📊 **Health Monitoring** - Built-in health check endpoints
- 🎨 **Responsive UI** - Mobile-first design that works on all devices

---

## 🏗️ Architecture

### System Design

```
┌──────────┐
│  Client  │
└────┬─────┘
     │ HTTP
     ▼
┌─────────────────────────┐
│  Spring Boot App        │
│  ┌──────────────────┐   │
│  │   Controller     │   │
│  └────────┬─────────┘   │
│           │              │
│  ┌────────▼─────────┐   │
│  │    Service       │   │
│  └────────┬─────────┘   │
│           │              │
│     ┌─────┴─────┐       │
│     ▼           ▼        │
│  ┌─────┐   ┌────────┐   │
│  │Redis│   │   H2   │   │
│  └─────┘   └────────┘   │
└──────┬──────────────────┘
       │
       ▼
┌──────────────┐
│  ZooKeeper   │
│ (Distributed │
│   Counter)   │
└──────────────┘
```

### How It Works

**URL Shortening Process:**
1. User submits a URL through the web interface or API
2. System validates the URL and checks for existing entries
3. Requests a unique counter from ZooKeeper (manages distributed counter ranges)
4. Encodes the counter using Base62 algorithm
5. Stores the mapping in H2 database and caches it in Redis
6. Returns the shortened URL to the user

**URL Resolution Process:**
1. User accesses the short URL
2. System checks Redis cache first (fast path - sub-millisecond)
3. If not cached, queries H2 database
4. Validates expiration date
5. Caches the result in Redis for future requests
6. Redirects user to the original URL (HTTP 302)

---

## 🛠️ Tech Stack

### Backend
- **Java 21** - Modern Java features and performance optimizations
- **Spring Boot 3.2.0** - Application framework with auto-configuration
- **Spring Data JPA** - Database access and ORM
- **Spring Data Redis** - Redis integration and caching
- **Maven** - Build and dependency management

### Storage & Infrastructure
- **H2 Database** - In-memory SQL database for persistence
- **Redis 7** - High-performance distributed cache
- **ZooKeeper 3.9** - Distributed coordination and counter management

### Frontend
- **HTML5** - Semantic markup
- **CSS3** - Modern styling with gradients and animations
- **Vanilla JavaScript** - No framework dependencies for simplicity

### DevOps
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration

---

