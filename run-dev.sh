#!/bin/bash
echo "Arrancando Autotrack API con GRADLE en modo DESARROLLO..."
./gradlew clean bootRun --args='--spring.profiles.active=dev'
