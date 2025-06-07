@echo off
echo ================================================
echo    Tworzenie instalatora ObiWanKenobi
echo ================================================

REM Sprawdź czy istnieje target directory
if not exist "target" (
    echo BŁĄD: Brak folderu target! Uruchom najpierw: mvn clean package
    pause
    exit /b 1
)

REM Sprawdź czy istnieje JAR
if not exist "target\ObiWanKenobi-1.0-SNAPSHOT-shaded.jar" (
    echo BŁĄD: Brak pliku JAR! Uruchom najpierw: mvn clean package
    pause
    exit /b 1
)

REM Usuń stary runtime
if exist "myruntime" (
    echo Usuwanie starego runtime...
    rmdir /s /q myruntime
)

echo.
echo Krok 1: Tworzenie custom runtime...
echo Używane moduły: java.base,java.desktop,java.logging,java.prefs,java.xml,java.sql,java.naming,java.transaction.xa,java.management,java.security.jgss,javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web

REM Sprawdź czy istnieje JDK
if not exist "C:\Program Files\Java\jdk-23\jmods" (
    echo UWAGA: Nie znaleziono JDK w standardowej lokalizacji
    echo Sprawdź ścieżkę do JDK lub zaktualizuj skrypt
)

REM Sprawdź czy istnieje JavaFX
if not exist "C:\javafx\javafx-jmods-23.0.1" (
    echo UWAGA: Nie znaleziono JavaFX jmods
    echo Pobierz z https://openjfx.io/ i rozpakuj do C:\javafx\javafx-jmods-23.0.1
    echo Lub zaktualizuj ścieżkę w skrypcie
)

jlink --module-path "C:\Program Files\Java\jdk-23\jmods;C:\javafx\javafx-jmods-23.0.1" ^
      --add-modules java.base,java.desktop,java.logging,java.prefs,java.xml,java.sql,java.naming,java.transaction.xa,java.management,java.security.jgss,javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web ^
      --output myruntime ^
      --compress=2 ^
      --no-header-files ^
      --no-man-pages

if not exist "myruntime" (
    echo BŁĄD: Runtime nie został utworzony!
    echo Sprawdź ścieżki do JDK i JavaFX
    pause
    exit /b 1
)

echo Runtime utworzony pomyślnie!

echo.
echo Krok 2: Tworzenie instalatora EXE...

jpackage --input target ^
         --name ObiWanKenobi ^
         --main-jar ObiWanKenobi-1.0-SNAPSHOT-shaded.jar ^
         --main-class com.example.obiwankenobi.Launcher ^
         --type exe ^
         --app-version 1.0 ^
         --vendor "ObiWanShop" ^
         --icon src\main\resources\com\example\obiwankenobi\img\logo2.ico ^
         --runtime-image myruntime ^
         --win-console ^
         --win-dir-chooser ^
         --win-menu ^
         --win-shortcut

if %errorlevel% equ 0 (
    echo.
    echo ================================================
    echo    SUKCES! Installer został utworzony!
    echo ================================================
    echo Sprawdź folder z plikiem .exe
) else (
    echo.
    echo BŁĄD podczas tworzenia instalatora!
    echo Sprawdź powyższe komunikaty błędów
)

pause