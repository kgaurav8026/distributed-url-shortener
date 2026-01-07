@echo off
echo ============================================
echo   Starting URL Shortener on Port 7777
echo ============================================
echo.
echo Killing any existing Java processes...
taskkill /F /IM java.exe 2>nul
timeout /t 2 /nobreak >nul

echo.
echo Starting application...
echo This will take 30-40 seconds on first run...
echo.

cd /d "%~dp0"
mvn clean spring-boot:run -Dspring-boot.run.arguments="--server.port=7777"

pause

