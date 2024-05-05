import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentManagementSystem {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db";
    private static final String username="root";
    private static final String password="root";
    public static void main(String[] args) {
        createTableIfNotExists();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nStudent Management System Menu:");
            System.out.println("1. Add Student");
            System.out.println("2. Remove Student");
            System.out.println("3. Display Students");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addStudent(scanner);
                    break;
                case 2:
                    removeStudent(scanner);
                    break;
                case 3:
                    displayStudents();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        }
    }

    private static void createTableIfNotExists() {
        try (Connection conn = DriverManager.getConnection(DB_URL, username, password);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS students (" +
                    "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    "name TEXT NOT NULL," +
                    "age INTEGER NOT NULL," +
                    "grade TEXT NOT NULL)";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void addStudent(Scanner scanner) {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter student age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("Enter student grade: ");
        String grade = scanner.nextLine();

        try (Connection conn= DriverManager.getConnection(DB_URL,username,password);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO students (name, age, grade) VALUES (?, ?, ?)")) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, grade);
            pstmt.executeUpdate();
            System.out.println("Student added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void removeStudent(Scanner scanner) {
        System.out.print("Enter student name to remove: ");
        String name = scanner.nextLine();

        try (Connection conn= DriverManager.getConnection(DB_URL,username,password);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM students WHERE name = ?")) {
            pstmt.setString(1, name);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student removed successfully!");
            } else {
                System.out.println("Student not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayStudents() {
        try (Connection conn= DriverManager.getConnection(DB_URL,username,password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM students")) {
            if (!rs.isBeforeFirst()) {
                System.out.println("No students enrolled.");
                return;
            }
            System.out.println("List of Students:");
            while (rs.next()) {
                System.out.println("Name: " + rs.getString("name") +
                        ", Age: " + rs.getInt("age") +
                        ", Grade: " + rs.getString("grade"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
