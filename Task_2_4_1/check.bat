@echo off
setlocal

set GRADLE_USER_HOME=C:\gradle-home
set CHECKER=C:\gradle-build\Task_2_4_1\install\oop-checker\bin\oop-checker.bat

if not exist "%CHECKER%" (
    echo [check.bat] %CHECKER% not found.
    echo [check.bat] Running: gradlew installDist -x test
    call "%~dp0gradlew.bat" installDist -x test
    if errorlevel 1 (
        echo [check.bat] installDist failed.
        exit /b 1
    )
)

if not exist "%~dp0checker.groovy" (
    echo [check.bat] checker.groovy not found in %~dp0
    exit /b 1
)

pushd "%~dp0"
echo [check.bat] Running checker, output -^> report.html, errors -^> err.log
"%CHECKER%" test > report.html 2> err.log
set RC=%ERRORLEVEL%
popd

if %RC% NEQ 0 (
    echo [check.bat] checker exited with code %RC%. See err.log
) else (
    echo [check.bat] Done. report.html generated.
)
exit /b %RC%
