import java.io.*;
import java.util.*;


public class ClassFileHandler {
    private static final String MODULE_FILE = "Classes.txt";
    private static final String REGISTRATION_FILE = "student_courses.txt";

    public static boolean isAlreadyRegistered(String studentId, String moduleName) {
        File file = new File(REGISTRATION_FILE);
        if (!file.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split(",");

                if (parts.length >= 3) {
                    String existingId = parts[0].trim();
                    String existingModule = parts[2].trim();
                    
                    if (existingId.equalsIgnoreCase(studentId.trim()) && 
                        existingModule.equalsIgnoreCase(moduleName.trim())) {
                        return true; // Match found
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading registration file: " + e.getMessage());
        }
        return false;
    }

    public static List<Module> getModulesForStudent(String studentCourse) {
        List<Module> filteredModules = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(MODULE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split(",");
                if (parts.length >= 15) {
                    // Filter: Only add if the module belongs to the student's course (Index 8)
                    if (parts[8].trim().equalsIgnoreCase(studentCourse.trim())) {
                        filteredModules.add(new Module(parts));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading Modules file: " + e.getMessage());
        }
        return filteredModules;
    }

    public static boolean registerStudentToModule(String id, String name, String moduleName) {
        String record = id.trim() + "," + name.trim() + "," + moduleName.trim();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(REGISTRATION_FILE, true))) {
            bw.write(record);
            bw.newLine(); 
            return true;
        } catch (IOException e) {
            System.err.println("File Write Error: " + e.getMessage());
            return false;
        }
    }
}