@echo off
rem Gradle wrapper delegate script
rem This script delegates Gradle commands to the backend gradlew

rem Change to the backend directory
cd "%~dp0backend"

rem Execute the backend gradlew with the same arguments
gradlew.bat %*
