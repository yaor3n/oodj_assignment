import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class academicLeaderFileManager {
    private static final String MODULE_FILE = "academicLeaderModule.txt";
    private static final String ACCOUNT_FILE = "accounts.txt";
    
    public static String[] authenticate(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(ACCOUNT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 10) {
                    String authenticateUser = parts[7].trim();
                    String authenticatePassword = parts[8].trim();
                    
                    if (username.equals(authenticateUser) && password.equals(authenticatePassword)) {
                        return parts;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; 
    }
    
    public static String userFullName() {
        String currentID = userSession.loggedInUserId;
        try (BufferedReader br = new BufferedReader(new FileReader("accounts.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 1 && parts[0].trim().equals(currentID)) {
                    return parts[1].trim(); 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "User"; 
    }
    
    public static void saveModule(academicLeaderModule module) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("academicLeaderModule.txt", true)))) {
            out.println(module.toString()); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<academicLeaderModule> loadModules() {
        List<academicLeaderModule> modules = new ArrayList<>();
        File file = new File(MODULE_FILE);
        if (!file.exists()) return modules;

        try (BufferedReader br = new BufferedReader(new FileReader(MODULE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 10) {
                    int yearValue = Integer.parseInt(parts[6].trim());
                    modules.add(new academicLeaderModule(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], yearValue ,parts[7], parts[8], parts[9]));
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
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(MODULE_FILE, false)))) {
            for (academicLeaderModule m : modules) {
                out.println(m.toString()); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static List<String> checkLecturerNames(){
        List<String> lecturers = new ArrayList<>();
        File file = new File(ACCOUNT_FILE);
        
        if (!file.exists())return lecturers;
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = br.readLine()) !=null){
                String[]parts = line.split(",");
                if (parts.length>=10 && parts[9].trim().equals("Lecturer")){
                    lecturers.add(parts[0].trim() + " - " + parts[1].trim());
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return lecturers;
    }
    
    public int getModuleCount(){
        int count=0;
        File file=new File(MODULE_FILE);
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
    
    public static void saveLog(String action, String moduleCode, String moduleName) {
        String timestamp = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(java.time.LocalDateTime.now());
        String userId = userSession.loggedInUserId; 
        String userName = userFullName(); 

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("academicLeaderLog.txt", true)))) {
            out.println(timestamp + "," + userId + "," + userName + "," + action + "," + moduleCode + "," + moduleName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}