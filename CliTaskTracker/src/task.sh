#!/bin/bash

# Định nghĩa đường dẫn tuyệt đối tới dự án
PROJECT_ROOT="/home/vinhhung053/IdeaProjects/backend_learning/CliTaskTracker"
SRC_DIR="$PROJECT_ROOT/src/main/java"
CLASS_PATH="$PROJECT_ROOT/src/main/java"

# 1. Biên dịch: sử dụng đường dẫn tuyệt đối tới file .java
javac "$SRC_DIR/org/example/TaskCLI.java"

# 2. Chạy: sử dụng đường dẫn tuyệt đối cho classpath
java -cp "$CLASS_PATH" org.example.TaskCLI "$@"