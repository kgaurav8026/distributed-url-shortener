@echo off
echo ========================================
echo   URL Shortener - Stopping Services
echo ========================================
echo.

echo Stopping Docker services...
docker-compose down

echo.
echo Services stopped successfully!
pause

