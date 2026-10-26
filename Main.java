
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final ArrayList<Student> students = new ArrayList<>();
    private static final String FILE_NAME = "students.csv";

    public static void main(String[] args) {
        loadStudents();

        while (true) {
            System.out.println("\n================================");
            System.out.println("     STUDENT MANAGEMENT SYSTEM");
            System.out.println("================================");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student by ID");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Save Students");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": addStudent(); break;
                case "2": viewStudents(); break;
                case "3": searchStudent(); break;
                case "4": updateStudent(); break;
                case "5": deleteStudent(); break;
                case "6": saveStudents(); break;
                case "7":
                    saveStudents();
                    System.out.println("Thank you for using Student Management System!");
                    return;
                default:
                    System.out.println("Invalid option. Please choose 1-7.");
            }
        }
    }

    private static void addStudent() {
        try {
            System.out.print("Enter Student ID: ");
            int id = Integer.parseInt(sc.nextLine().trim());

            if (findStudent(id) != null) {
                System.out.println("That ID already exists.");
                return;
            }

            System.out.print("Enter Name: ");
            String name = sc.nextLine().trim();

            System.out.print("Enter Age: ");
            int age = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Enter Course: ");
            String course = sc.nextLine().trim();

            System.out.print("Enter Marks (0-100): ");
            double marks = Double.parseDouble(sc.nextLine().trim());

            if (id <= 0 || name.isEmpty() || age <= 0
                    || course.isEmpty() || marks < 0 || marks > 100) {
                System.out.println("Invalid details. Please check your entries.");
                return;
            }

            students.add(new Student(id, name, age, course, marks));
            saveStudents();
            System.out.println("Student added successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numbers for ID, age, and marks.");
        }
    }

    private static void viewStudents() {
        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }

        System.out.println("\n--- All Students ---");
        for (Student student : students) {
            student.display();
        }
    }

    private static void searchStudent() {
        try {
            System.out.print("Enter Student ID to search: ");
            int id = Integer.parseInt(sc.nextLine().trim());
            Student student = findStudent(id);

            if (student == null) {
                System.out.println("Student not found.");
            } else {
                student.display();
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid student ID.");
        }
    }

    private static void updateStudent() {
        try {
            System.out.print("Enter Student ID to update: ");
            int id = Integer.parseInt(sc.nextLine().trim());
            Student old = findStudent(id);

            if (old == null) {
                System.out.println("Student not found.");
                return;
            }

            System.out.println("Enter the new details:");
            System.out.print("Name: ");
            String name = sc.nextLine().trim();

            System.out.print("Age: ");
            int age = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Course: ");
            String course = sc.nextLine().trim();

            System.out.print("Marks (0-100): ");
            double marks = Double.parseDouble(sc.nextLine().trim());

            if (name.isEmpty() || age <= 0 || course.isEmpty()
                    || marks < 0 || marks > 100) {
                System.out.println("Invalid details. Update cancelled.");
                return;
            }

            students.remove(old);
            students.add(new Student(id, name, age, course, marks));
            saveStudents();
            System.out.println("Student updated successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input. Update cancelled.");
        }
    }

    private static void deleteStudent() {
        try {
            System.out.print("Enter Student ID to delete: ");
            int id = Integer.parseInt(sc.nextLine().trim());
            Student student = findStudent(id);

            if (student == null) {
                System.out.println("Student not found.");
                return;
            }

            students.remove(student);
            saveStudents();
            System.out.println("Student deleted successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid student ID.");
        }
    }

    private static Student findStudent(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }

    private static void saveStudents() {
        try (PrintWriter writer = new PrintWriter(
                Files.newBufferedWriter(Paths.get(FILE_NAME)))) {
            writer.println("ID,Name,Age,Course,Marks");

            for (Student student : students) {
                writer.printf("%d,%s,%d,%s,%.2f%n",
                        student.getId(),
                        student.getName().replace(",", " "),
                        student.getAge(),
                        student.getCourse().replace(",", " "),
                        student.getMarks());
            }

            System.out.println("Student records saved.");
        } catch (IOException e) {
            System.out.println("Error saving records: " + e.getMessage());
        }
    }

    
private static void loadStudents() {
    Path path = Paths.get(FILE_NAME);

    if (!Files.exists(path)) {
        return;
    }

    try (BufferedReader reader = Files.newBufferedReader(path)) {
        String line;
        boolean firstLine = true;

        while ((line = reader.readLine()) != null) {
            if (firstLine) {
                firstLine = false;
                continue; // Skip the CSV header
            }

            try {
                String[] data = line.split(",", -1);

                if (data.length == 5) {
                    int id = Integer.parseInt(data[0]);
                    String name = data[1];
                    int age = Integer.parseInt(data[2]);
                    String course = data[3];
                    double marks = Double.parseDouble(data[4]);

                    if (id > 0 && age > 0 && marks >= 0 && marks <= 100
                            && findStudent(id) == null) {
                        students.add(
                            new Student(id, name, age, course, marks)
                        );
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Skipping an invalid student record.");
            }
        }
    } catch (IOException e) {
        System.out.println("Error loading records: " + e.getMessage());
    }
}

}
