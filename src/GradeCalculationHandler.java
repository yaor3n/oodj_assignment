import java.io.*;
import java.util.*;

public class GradeCalculationHandler {

    // Reads GradingSystem.txt and returns the Grade based on score
    public static String calculateGrade(int score) {
        try (BufferedReader br = new BufferedReader(new FileReader("GradingSystem.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                String gradeName = p[0];
                int min = Integer.parseInt(p[1]);
                int max = Integer.parseInt(p[2]);
                if (score >= min && score <= max) return gradeName;
            }
        } catch (IOException e) { e.printStackTrace(); }
        return "F"; // Default if not found
    }

    // Processes Results.txt and writes to Grades.txt
    public static void syncGrades() {
        List<Result> processedResults = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("Grades.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Result res = new Result(line.split(","));
                res.setGrade(calculateGrade(res.getScore()));
                processedResults.add(res);
            }
        } catch (IOException e) { e.printStackTrace(); }

        try (PrintWriter pw = new PrintWriter(new FileWriter("Grades.txt"))) {
            for (Result r : processedResults) {
                pw.println(r.toGradeFileString());
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}