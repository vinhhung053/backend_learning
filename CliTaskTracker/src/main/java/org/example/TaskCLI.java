package org.example;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

public class TaskCLI {

    static String FILE_NAME = "tasks.json";

    static class Task {
        int id;
        String description;
        String status;
        String createdAt;
        String updatedAt;

        Task(int id, String description) {
            this.id = id;
            this.description = description;
            this.status = "todo";
            this.createdAt = LocalDateTime.now().toString();
            this.updatedAt = this.createdAt;
        }
    }

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            System.out.println("No command provided.");
            return;
        }

        List<Task> tasks = loadTasks();
        String command = args[0];

        switch (command) {

            case "add":
                addTask(tasks, args);
                break;

            case "update":
                updateTask(tasks, args);
                break;

            case "delete":
                deleteTask(tasks, args);
                break;

            case "mark-in-progress":
                markStatus(tasks, args, "in-progress");
                break;

            case "mark-done":
                markStatus(tasks, args, "done");
                break;

            case "list":
                listTasks(tasks, args);
                break;

            default:
                System.out.println("Unknown command");
        }

        saveTasks(tasks);
    }

    static List<Task> loadTasks() throws Exception {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            file.createNewFile();
            Files.write(Paths.get(FILE_NAME), "[]".getBytes());
        }

        String content = new String(Files.readAllBytes(Paths.get(FILE_NAME)));

        if (content.trim().isEmpty() || content.equals("[]")) {
            return new ArrayList<>();
        }

        // simple parser
        List<Task> tasks = new ArrayList<>();
        String[] items = content.replace("[", "").replace("]", "").split("},");

        for (String item : items) {

            item = item.replace("{", "").replace("}", "");

            String[] fields = item.split(",");

            int id = 0;
            String description = "";
            String status = "";
            String createdAt = "";
            String updatedAt = "";

            for (String f : fields) {

                String[] pair = f.split(":");

                String key = pair[0].replace("\"", "").trim();
                String value = pair[1].replace("\"", "").trim();

                switch (key) {
                    case "id":
                        id = Integer.parseInt(value);
                        break;
                    case "description":
                        description = value;
                        break;
                    case "status":
                        status = value;
                        break;
                    case "createdAt":
                        createdAt = value;
                        break;
                    case "updatedAt":
                        updatedAt = value;
                        break;
                }
            }

            Task t = new Task(id, description);
            t.status = status;
            t.createdAt = createdAt;
            t.updatedAt = updatedAt;

            tasks.add(t);
        }

        return tasks;
    }

    static void saveTasks(List<Task> tasks) throws Exception {

        StringBuilder json = new StringBuilder();
        json.append("[\n");

        for (int i = 0; i < tasks.size(); i++) {

            Task t = tasks.get(i);

            json.append("  {");
            json.append("\"id\":").append(t.id).append(",");
            json.append("\"description\":\"").append(t.description).append("\",");
            json.append("\"status\":\"").append(t.status).append("\",");
            json.append("\"createdAt\":\"").append(t.createdAt).append("\",");
            json.append("\"updatedAt\":\"").append(t.updatedAt).append("\"");
            json.append("}");

            if (i < tasks.size() - 1) json.append(",");

            json.append("\n");
        }

        json.append("]");

        Files.write(Paths.get(FILE_NAME), json.toString().getBytes());
    }

    static void addTask(List<Task> tasks, String[] args) {

        if (args.length < 2) {
            System.out.println("Description required");
            return;
        }

        int id = tasks.size() == 0 ? 1 : tasks.get(tasks.size() - 1).id + 1;

        Task task = new Task(id, args[1]);

        tasks.add(task);

        System.out.println("Task added successfully (ID: " + id + ")");
    }

    static void updateTask(List<Task> tasks, String[] args) {

        if (args.length < 3) {
            System.out.println("Usage: update <id> <description>");
            return;
        }

        int id = Integer.parseInt(args[1]);

        for (Task t : tasks) {

            if (t.id == id) {

                t.description = args[2];
                t.updatedAt = LocalDateTime.now().toString();

                System.out.println("Task updated.");
                return;
            }
        }

        System.out.println("Task not found.");
    }

    static void deleteTask(List<Task> tasks, String[] args) {

        int id = Integer.parseInt(args[1]);

        tasks.removeIf(t -> t.id == id);

        System.out.println("Task deleted.");
    }

    static void markStatus(List<Task> tasks, String[] args, String status) {

        int id = Integer.parseInt(args[1]);

        for (Task t : tasks) {

            if (t.id == id) {

                t.status = status;
                t.updatedAt = LocalDateTime.now().toString();

                System.out.println("Task updated.");
                return;
            }
        }

        System.out.println("Task not found.");
    }

    static void listTasks(List<Task> tasks, String[] args) {

        if (tasks.isEmpty()) {
            System.out.println("No tasks.");
            return;
        }

        if (args.length == 1) {

            for (Task t : tasks) {
                printTask(t);
            }

        } else {

            String filter = args[1];

            for (Task t : tasks) {

                if (t.status.equals(filter)) {
                    printTask(t);
                }
            }
        }
    }

    static void printTask(Task t) {

        System.out.println(
                t.id + " | " +
                        t.description + " | " +
                        t.status
        );
    }
}