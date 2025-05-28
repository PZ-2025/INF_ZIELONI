@echo off
setlocal EnableDelayedExpansion
cd /d "%~dp0"

echo =========================================
echo     MySQL Lokalny - Setup i Import
echo =========================================
echo.

REM Konfiguracja MySQL
set DB_NAME=obiwanshop
set MYSQL_USER=root	
set MYSQL_PASSWORD=admin
set MYSQL_HOST=localhost
set MYSQL_PORT=3306
set CHECK_TABLE=users

REM Sprawdzenie czy pliki SQL istnieją
if not exist "Db.sql" (
    echo BLAD: Nie znaleziono pliku Db.sql
    pause
    exit /b 1
)
if not exist "data.sql" (
    echo BLAD: Nie znaleziono pliku data.sql
    pause
    exit /b 1
)

echo Sprawdzanie dostepnosci MySQL...

REM Uruchamianie uslugi MySQL jesli nie dziala
echo Sprawdzanie czy usluga MySQL jest uruchomiona...
sc query MySQL80 | find "RUNNING" >nul
if errorlevel 1 (
    echo MySQL nie jest uruchomiony – uruchamiam usluge...
    net start MySQL80
    timeout /t 5 >nul
) else (
    echo MySQL juz dziala jako usluga.
)

REM Szukanie mysql.exe
set MYSQL_CMD=mysql

where mysql.exe >nul 2>&1
if %errorlevel% neq 0 (
    if exist "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" (
        set MYSQL_CMD="C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
    ) else if exist "C:\Program Files\MySQL\MySQL Server 5.7\bin\mysql.exe" (
        set MYSQL_CMD="C:\Program Files\MySQL\MySQL Server 5.7\bin\mysql.exe"
    ) else if exist "C:\xampp\mysql\bin\mysql.exe" (
        set MYSQL_CMD="C:\xampp\mysql\bin\mysql.exe"
    ) else if exist "C:\wamp64\bin\mysql\mysql8.0.31\bin\mysql.exe" (
        set MYSQL_CMD="C:\wamp64\bin\mysql\mysql8.0.31\bin\mysql.exe"
    ) else if exist "C:\wamp\bin\mysql\mysql5.7.36\bin\mysql.exe" (
        set MYSQL_CMD="C:\wamp\bin\mysql\mysql5.7.36\bin\mysql.exe"
    ) else (
        echo BLAD: Nie znaleziono MySQL
        pause
        exit /b 1
    )
)

echo Sprawdzanie polaczenia z MySQL...
%MYSQL_CMD% -h %MYSQL_HOST% -P %MYSQL_PORT% -u %MYSQL_USER% -p%MYSQL_PASSWORD% -e "SELECT 1;" >nul 2>&1
if %errorlevel% neq 0 (
    echo BLAD: Nie mozna polaczyc sie z MySQL
    pause
    exit /b 1
)
echo Polaczenie OK

REM Sprawdzenie czy baza danych istnieje
echo Sprawdzanie czy baza danych '%DB_NAME%' istnieje...
%MYSQL_CMD% -h %MYSQL_HOST% -P %MYSQL_PORT% -u %MYSQL_USER% -p%MYSQL_PASSWORD% -e "SHOW DATABASES LIKE '%DB_NAME%';" | findstr /i "%DB_NAME%" >nul
if %errorlevel% == 0 (
    echo Baza danych '%DB_NAME%' juz istnieje – pominieto tworzenie.
    goto :end
)

REM Tworzenie bazy danych i import
echo Baza nie istnieje – tworzenie i import...
%MYSQL_CMD% -h %MYSQL_HOST% -P %MYSQL_PORT% -u %MYSQL_USER% -p%MYSQL_PASSWORD% < Db.sql
if %errorlevel% neq 0 (
    echo BLAD przy tworzeniu struktury bazy danych!
    pause
    exit /b 1
)
echo Struktura utworzona, import danych...
%MYSQL_CMD% -h %MYSQL_HOST% -P %MYSQL_PORT% -u %MYSQL_USER% -p%MYSQL_PASSWORD% %DB_NAME% < data.sql
if %errorlevel% neq 0 (
    echo BLAD przy imporcie danych!
    pause
    exit /b 1
)
echo Baza danych utworzona i dane zaimportowane.

:end
echo.
echo =========================================
echo           SETUP ZAKONCZONY
echo =========================================
echo.
echo Baza danych: %DB_NAME%
echo Uzytkownik: %MYSQL_USER%
echo.
pause
