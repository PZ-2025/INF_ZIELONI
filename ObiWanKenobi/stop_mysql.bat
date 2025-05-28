@echo off
echo =========================================
echo     Zatrzymywanie serwera MySQL
echo =========================================

REM Nazwa usługi MySQL (dla domyślnej instalacji MySQL 8 to zazwyczaj MySQL80)
set SERVICE_NAME=MySQL80

REM Sprawdzenie, czy usługa działa
sc query %SERVICE_NAME% | find "RUNNING" >nul
if %errorlevel%==0 (
    echo MySQL jest uruchomiony – zatrzymywanie...
    net stop %SERVICE_NAME%
    if %errorlevel%==0 (
        echo MySQL zatrzymany pomyslnie.
    ) else (
        echo BLAD: Nie udalo sie zatrzymac uslugi MySQL.
    )
) else (
    echo MySQL nie jest uruchomiony – nic do zatrzymania.
)

echo.
pause
