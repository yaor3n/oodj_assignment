import java.io.*;
import java.util.*;

public class ClassFileHandler {
    private static final String FILE_PATH = "Classes.txt";

    public static List<Module> getModulesForStudent(String studentCourse) {
        List<Module> filteredModules = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 15) {
                    Module m = new Module(parts);
                    // Filter: Only add if the module belongs to the student's course
                    if (parts[8].trim().equalsIgnoreCase(studentCourse.trim())) {
                        filteredModules.add(new Module(parts));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filteredModules;
    }

    public static boolean registerStudentToModule(String id, String name, String moduleName) {
        String filePath = "student_courses.txt";
        // Format: studentId,studentname,moduleName
        String record = id + "," + name + "," + moduleName;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(record);
            bw.newLine(); // Ensure the next entry starts on a new line
            return true;
        } catch (IOException e) {
            System.err.println("File Write Error: " + e.getMessage());
            return false;
        }
    }
}
