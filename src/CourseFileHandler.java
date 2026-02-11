import java.io.*;
import java.util.*;

public class CourseFileHandler {

    private static final String COURSE_FILE = "academicLeaderModule.txt";
    private static final String REGISTRATION_FILE = "student_courses.txt";
    private static final String FEEDBACK_FILE = "student_feedback.txt";

    public static List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(COURSE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                // academicLeaderModule.txt uses commas
                courses.add(Course.fromFileString(line));
            }
        } catch (IOException e) {
            System.out.println("Course file not found: " + COURSE_FILE);
        }
        return courses;
    }

    public static List<Course> getRegisteredCoursesForStudent(String username) {
        List<String> registeredCodes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(REGISTRATION_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                // student_courses.txt still uses | based on your previous input
                String[] parts = line.split("\\|");
                if (parts.length >= 2 && parts[0].trim().equalsIgnoreCase(username)) {
                    registeredCodes.add(parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("No registration file found yet.");
        }

        List<Course> allCourses = getAllCourses();
        List<Course> filteredCourses = new ArrayList<>();
        for (Course c : allCourses) {
            if (registeredCodes.contains(c.getCode().trim())) {
                filteredCourses.add(c);
            }
        }
        return filteredCourses;
    }

    public static List<Feedback> getFeedbackForStudent(String targetStudentId) {
        List<Feedback> feedbackList = new ArrayList<>();
        File file = new File(FEEDBACK_FILE);

        if (!file.exists()) return feedbackList;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // student_feedback.txt using commas
                String[] parts = line.split(",");

                if (parts.length >= 4) {
                    String studentId = parts[0].trim();

                    if (studentId.equalsIgnoreCase(targetStudentId)) {
                        try {
                            String moduleName = parts[1].trim();
                            String lecturer = parts[2].trim();

                            // Check if the 4th column (index 3) is a number (Rating)
                            // or a comment (for those lines missing ratings)
                            int rating;
                            String comment;
                            String reply = "";

                            if (parts[3].trim().matches("\\d+")) {
                                // Format: Student,Module,Lecturer,Rating,Comment,Reply
                                rating = Integer.parseInt(parts[3].trim());
                                comment = (parts.length >= 5) ? parts[4].trim() : "";
                                reply = (parts.length >= 6) ? parts[5].trim() : "";
                            } else {
                                // Format: Student,Module,Lecturer,Comment (Missing Rating)
                                rating = 0; // Default if missing
                                comment = parts[3].trim();
                                reply = (parts.length >= 5) ? parts[4].trim() : "";
                            }

                            feedbackList.add(new Feedback(studentId, moduleName, lecturer, comment, rating, reply));
                        } catch (Exception e) {
                            System.err.println("Error parsing line: " + line);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return feedbackList;
    }

    public static void saveFeedback(Feedback fb) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FEEDBACK_FILE, true))) {
            // IMPORTANT: Ensure fb.toFileString() now uses COMMAS instead of PIPES
            pw.println(fb.toFileString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void registerStudent(String username, Course course) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(REGISTRATION_FILE, true))) {
            pw.println(username + "|" + course.getCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}