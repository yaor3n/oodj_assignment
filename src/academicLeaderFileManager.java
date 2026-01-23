import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class academicLeaderFileManager {
    private static final String MODULE_FILE = "academicLeaderModule.txt";
    private static final String ACCOUNT_FILE = "accounts.txt";

    // Authenticate user by username and password
    public static String[] authenticate(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(ACCOUNT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String fUser = parts[0].trim();
                    String fPass = parts[1].trim();
                    if (username.equals(fUser) && password.equals(fPass)) {
                        return parts;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // No match found
    }

    // Save a single module to the file (append mode)
    public static void saveModule(academicLeaderModule module) {
        try (PrintWriter out = new PrintWriter(new FileWriter(MODULE_FILE, true))) {
            out.println(module.toString());
            System.out.println("Successfully saved to: " + MODULE_FILE);
        } catch (IOException e) {
            System.err.println("Error saving module: " + e.getMessage());
        }
    }

    // Load all modules from file
    public static List<academicLeaderModule> loadModules() {
        List<academicLeaderModule> modules = new ArrayList<>();
        File file = new File(MODULE_FILE);
        if (!file.exists()) return modules;

        try (BufferedReader br = new BufferedReader(new FileReader(MODULE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    modules.add(new academicLeaderModule(parts[0], parts[1], parts[2], parts[3], parts[4]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return modules;
    }

    // Update a module
    public static void updateModule(academicLeaderModule updatedModule) {
        List<academicLeaderModule> modules = loadModules();
        for (int i = 0; i < modules.size(); i++) {
            if (modules.get(i).getCode().equalsIgnoreCase(updatedModule.getCode())) {
                modules.set(i, updatedModule);
                break;
            }
        }
        saveNewModule(modules);
    }

    // Delete a module
    public static void deleteModule(String moduleCode) {
        List<academicLeaderModule> modules = loadModules();
        modules.removeIf(m -> m.getCode().equalsIgnoreCase(moduleCode));
        saveNewModule(modules);
    }

    // Save a list of modules (overwrite file)
    public static void saveNewModule(List<academicLeaderModule> modules) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(MODULE_FILE, false)))) {
            for (academicLeaderModule m : modules) {
                out.println(m.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get all lecturers from accounts.txt
    public static List<String> checkLecturerNames() {
        List<String> lecturers = new ArrayList<>();
        File file = new File(ACCOUNT_FILE);

        if (!file.exists()) return lecturers;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[2].trim().equalsIgnoreCase("Lecturer")) {
                    lecturers.add(parts[0].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lecturers;
    }

    // Get total module count
    public static int getModuleCount() {
        int count = 0;
        File file = new File(MODULE_FILE);
        if (!file.exists()) return 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return count;
    }
}
