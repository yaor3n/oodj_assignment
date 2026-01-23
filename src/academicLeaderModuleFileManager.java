//Module&txt file
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class academicLeaderModuleFileManager {
    private static final String FILE_PATH = "academicLeaderModule.txt";
    
    public static void saveModule(academicLeaderModule module) {
    // try-with-resources automatically closes the writer, ensuring data is saved
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            out.println(module.toString()); 
            System.out.println("Successfully saved to: " + FILE_PATH);
        } catch (IOException e) {
            System.err.println("Error saving module: " + e.getMessage());
        }
    }

    // Read all modules from the file and turn them into Objects
    public static List<academicLeaderModule> loadModules() {
        List<academicLeaderModule> modules = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return modules;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
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
    
    public static List<String> checkLecturerNames(){
        List<String> lecturers = new ArrayList<>();
        File file = new File("accounts.txt");
        
        if (!file.exists())return lecturers;
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = br.readLine()) !=null){
                String[]parts = line.split(",");
                if (parts.length>=3 && parts[2].trim().equals("Lecturer")){
                    lecturers.add(parts[0].trim());
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return lecturers;
    }
    
}
