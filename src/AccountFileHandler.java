import java.io.*;
import java.util.*;

public class AccountFileHandler {

    private static final String FILE_NAME = "accounts.txt";

    public static Student getStudent(String username) {

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data[0].equals(username) && data[2].equals("Student")) {
                    return new Student(data[0], data[1], data[2]);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void updateStudent(Student updatedStudent) {

        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data[0].equals(updatedStudent.getUsername())) {
                    lines.add(updatedStudent.getUsername() + "," +
                            updatedStudent.getPassword() + "," +
                            updatedStudent.getRole());
                } else {
                    lines.add(line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter fw = new FileWriter(FILE_NAME)) {
            for (String l : lines) {
                fw.write(l + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
