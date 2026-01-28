import java.io.*;
import java.util.*;

public class CourseFileHandler {

    private static final String COURSE_FILE = "courses.txt";
    private static final String REGISTRATION_FILE = "student_courses.txt";
    private static final String FEEDBACK_FILE = "feedback.txt"; // New file for feedback

    public static List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(COURSE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                courses.add(Course.fromFileString(line));
            }
        } catch (IOException e) {
            System.out.println("Course file not found.");
        }
        return courses;
    }

    public static void registerStudent(String username, Course course) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(REGISTRATION_FILE, true))) {
            pw.println(username + "|" + course.getCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // get courses registered for student
    public static List<Course> getRegisteredCoursesForStudent(String username) {
        List<String> registeredCodes = new ArrayList<>();


        try (BufferedReader br = new BufferedReader(new FileReader(REGISTRATION_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 2 && parts[0].equals(username)) {
                    registeredCodes.add(parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("No registration file found yet.");
        }

        List<Course> allCourses = getAllCourses();
        List<Course> filteredCourses = new ArrayList<>();
        for (Course c : allCourses) {
            if (registeredCodes.contains(c.getCode())) {
                filteredCourses.add(c);
            }
        }
        return filteredCourses;
    }

    // get feedback to display
    public static List<Feedback> getFeedbackForStudent(String targetStudentId) {
        List<Feedback> feedbackList = new ArrayList<>();
        File file = new File("feedback.txt");

        if (!file.exists()) return feedbackList;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // \\| is required because | is a special regex character
                String[] parts = line.split("\\|");

                if (parts.length >= 5) {
                    String studentId = parts[0].trim();

                    if (studentId.equalsIgnoreCase(targetStudentId)) {
                        try {
                            String module = parts[1].trim();
                            String lecturer = parts[2].trim();
                            int rating = Integer.parseInt(parts[3].trim());
                            String comment = parts[4].trim();


                            String reply = (parts.length >= 6) ? parts[5].trim() : "";

                            feedbackList.add(new Feedback(studentId, module, lecturer, comment, rating, reply));
                        } catch (NumberFormatException e) {
                            System.err.println("Skipping line with invalid rating format: " + line);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return feedbackList;
    }

    // save feedback
    public static void saveFeedback(Feedback fb) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FEEDBACK_FILE, true))) {
            // Uses the Feedback object's own method to format the string (Encapsulation)
            pw.println(fb.toFileString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
