import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class academicLeaderModuleFileManager {
    private static final String FILE_PATH = "academicLeaderModule.txt";
    
    public static void saveModule(academicLeaderModule module) {
        // Use true for append mode. PrintWriter.println adds the newline automatically.
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("academicLeaderModule.txt", true)))) {
            out.println(module.toString()); 
        } catch (IOException e) {
            e.printStackTrace();
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
                if (parts.length >= 8) {
                    int yearValue = Integer.parseInt(parts[5].trim());
                    modules.add(new academicLeaderModule(parts[0], parts[1], parts[2], parts[3], parts[4], yearValue ,parts[6],parts[7]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return modules;
    }
    
    public static void updateModule(academicLeaderModule updatedModule){
        List<academicLeaderModule>modules = loadModules();
        for (int i=0;i<modules.size();i++){
            if (modules.get(i).getCode().equalsIgnoreCase(updatedModule.getCode())){
                modules.set(i,updatedModule);
                break;
            }
        }
        saveNewModule(modules);
    }
    
    public static void deleteModule(String moduleCode) {
        List<academicLeaderModule> modules = loadModules();
        modules.removeIf(m -> m.getCode().equalsIgnoreCase(moduleCode));
        saveNewModule(modules);
    }
    
    public static void saveNewModule(List<academicLeaderModule> modules) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH, false)))) {
            for (academicLeaderModule m : modules) {
                // Using m.toString() ensures the 7th field (Intake) is included!
                out.println(m.toString()); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    
    public int getModuleCount(){
        int count=0;
        File file=new File(FILE_PATH);
        if (!file.exists())return 0;
        
        try(BufferedReader br=new BufferedReader(new FileReader(file))){
            while(br.readLine() !=null){
                count++;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return count;
    }
    
}
