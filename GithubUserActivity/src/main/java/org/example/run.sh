#!/bin/bash

# Path project
PROJECT_PATH="$HOME/IdeaProjects/backend_learning/GithubUserActivity"

# Path source java
SRC_PATH="$PROJECT_PATH/src/main/java"

# Compile Java file
javac "$SRC_PATH/org/example/GitHubActivity.java"

# Check fail
if [ $? -ne 0 ]; then
  echo "Compilation failed"
  exit 1
fi

# Run
java -cp "$SRC_PATH" org.example.GitHubActivity "$@" vinhhung053