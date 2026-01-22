public class Feedback {
    private String studentUsername;
    private String courseCode;
    private String lecturerName;
    private String comment;
    private int rating; // New field: 1 to 5

    public Feedback(String studentUsername, String courseCode, String lecturerName, String comment, int rating) {
        this.studentUsername = studentUsername;
        this.courseCode = courseCode;
        this.lecturerName = lecturerName;
        this.comment = comment;
        this.rating = rating;
    }

    public String getCourseCode() { return courseCode; }
    public String getLecturerName() { return lecturerName; }
    public String getComment() { return comment; }
    public int getRating() { return rating; }

    public String toFileString() {
        // Updated to include rating
        return studentUsername + "|" + courseCode + "|" + lecturerName + "|" + rating + "|" + comment.replace("\n", " ");
    }
}