@echo off
echo ========================================
echo   URL Shortener - Starting Services
echo ========================================
echo.

echo [1/3] Starting Docker services (ZooKeeper and Redis)...
docker-compose up -d
if %errorlevel% neq 0 (
    echo ERROR: Failed to start Docker services
    echo Please make sure Docker Desktop is running
    pause
    exit /b 1
)

echo.
echo [2/3] Waiting for services to be ready...
timeout /t 10 /nobreak

echo.
echo [3/3] Starting URL Shortener application...
start cmd /k "mvn spring-boot:run"

echo.
echo ========================================
echo Services started successfully!
echo.
echo ZooKeeper: localhost:2181
echo Redis: localhost:6379
echo Application: http://localhost:8080
echo.
echo Press any key to open the application in browser...
pause
start http://localhost:8080

