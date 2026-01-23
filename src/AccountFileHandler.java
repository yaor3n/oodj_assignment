import java.io.*;
import java.util.*;

public class AccountFileHandler {
    private static final String ACCOUNT_FILE = "accounts.txt";

    /**
     * Authenticates a user by checking credentials against the file.
     * @return The User object if successful, null otherwise.
     */
    public static User authenticate(String inputUsername, String inputPassword) {
        List<User> allUsers = getAllUsers();
        for (User u : allUsers) {
            // Check if both username and password match (case-sensitive)
            if (u.getUsername().equals(inputUsername) && u.getPassword().equals(inputPassword)) {
                return u;
            }
        }
        return null;
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(ACCOUNT_FILE);

        if (!file.exists()) return users;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] p = line.split(",");

                // Must have exactly 9 fields to be a valid user in your current system
                if (p.length >= 9) {
                    String role = p[7].trim();

                    // Using .trim() on all parameters to prevent "invisible space" errors
                    if (role.equalsIgnoreCase("Student")) {
                        users.add(new Student(
                                p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(),
                                p[4].trim(), p[5].trim(), p[6].trim(), p[7].trim(), p[8].trim()
                        ));
                    } else {
                        users.add(new User(
                                p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(),
                                p[4].trim(), p[5].trim(), p[6].trim(), p[7].trim(), p[8].trim()
                        ));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Read Error: " + e.getMessage());
        }
        return users;
    }

    public static void updateStudent(Student updatedStudent) {
        List<User> allUsers = getAllUsers();
        try (PrintWriter pw = new PrintWriter(new FileWriter(ACCOUNT_FILE))) {
            for (User u : allUsers) {
                // If this is the user we want to update
                if (u.getUsername().equals(updatedStudent.getUsername())) {
                    pw.println(updatedStudent.toFileString());
                } else {
                    pw.println(u.toFileString());
                }
            }
        } catch (IOException e) {
            System.err.println("Update Error: " + e.getMessage());
        }
    }

    public static Student getStudent(String username) {
        for (User u : getAllUsers()) {
            if (u.getUsername().equals(username) && u instanceof Student) {
                return (Student) u;
            }
        }
        return null;
    }
}