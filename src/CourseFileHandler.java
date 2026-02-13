import java.io.*;
import java.util.*;

public class CourseFileHandler {

    private static final String COURSE_FILE = "academicLeaderModule.txt";
    private static final String REGISTRATION_FILE = "student_courses.txt";
    private static final String FEEDBACK_FILE = "student_feedback.txt";

    /**
     * Reads all courses from the master file (academicLeaderModule.txt).
     * Uses comma-separated values (10 columns).
     */
    public static List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(COURSE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                // Course.fromFileString is now updated to handle the 10-column format
                courses.add(Course.fromFileString(line));
            }
        } catch (IOException e) {
            System.out.println("Course file not found: " + COURSE_FILE);
        }
        return courses;
    }

    /**
     * Finds courses specifically registered to a student.
     * Checks student_courses.txt (raul|CP001 format) and matches against master list.
     */
    public static List<Course> getRegisteredCoursesForStudent(String username) {
        List<String> registeredModuleNames = new ArrayList<>();

        // 1. Read student_courses.txt
        try (BufferedReader br = new BufferedReader(new FileReader(REGISTRATION_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");

                // parts[0] is raul, parts[2] is the Module Name
                if (parts.length >= 3 && parts[0].trim().equalsIgnoreCase(username.trim())) {
                    registeredModuleNames.add(parts[2].trim().toLowerCase());
                }
            }
        } catch (IOException e) {
            System.out.println("Registration file missing.");
        }

        // 2. Cross-reference with academicLeaderModule.txt
        List<Course> allCourses = getAllCourses();
        List<Course> filteredCourses = new ArrayList<>();

        for (Course c : allCourses) {
            // Compare the Name from the master file to the names we found in registrations
            if (registeredModuleNames.contains(c.getName().trim().toLowerCase())) {
                filteredCourses.add(c);
            }
        }
        return filteredCourses;
    }

    /**
     * Reads feedback for a specific student, including lecturer replies.
     * Uses the new comma-separated format.
     */
    public static List<Feedback> getFeedbackForStudent(String targetStudentId) {
        List<Feedback> feedbackList = new ArrayList<>();
        File file = new File(FEEDBACK_FILE);

        if (!file.exists()) return feedbackList;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Now splitting by comma to match the new feedback file
                String[] parts = line.split(",");

                if (parts.length >= 4) {
                    String studentId = parts[0].trim();

                    if (studentId.equalsIgnoreCase(targetStudentId)) {
                        try {
                            String moduleName = parts[1].trim();
                            String lecturer = parts[2].trim();

                            // Handling varied feedback column lengths
                            int rating = 0;
                            String comment = "";
                            String reply = "";

                            if (parts.length >= 4 && parts[3].trim().matches("\\d+")) {
                                rating = Integer.parseInt(parts[3].trim());
                                comment = (parts.length >= 5) ? parts[4].trim() : "";
                                reply = (parts.length >= 6) ? parts[5].trim() : "";
                            } else {
                                // Fallback for lines without a numeric rating
                                comment = parts[3].trim();
                                reply = (parts.length >= 5) ? parts[4].trim() : "";
                            }

                            feedbackList.add(new Feedback(studentId, moduleName, lecturer, comment, rating, reply));
                        } catch (Exception e) {
                            System.err.println("Error parsing feedback line: " + line);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return feedbackList;
    }

    public static void registerStudent(String username, Course course) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(REGISTRATION_FILE, true))) {
            // Maintains the pipe format for consistency with your existing file
            pw.println(username.trim() + "," + course.getCode().trim() + "," + course.getName().trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveFeedback(Feedback fb) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FEEDBACK_FILE, true))) {
            // Important: ensure Feedback.toFileString() uses COMMAS
            pw.println(fb.toFileString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}