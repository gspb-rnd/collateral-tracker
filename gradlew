#!/bin/sh
# Gradle wrapper delegate script
# This script delegates Gradle commands to the backend gradlew

# Change to the backend directory
cd "$(dirname "$0")/backend"

# Execute the backend gradlew with the same arguments
exec ./gradlew "$@"
