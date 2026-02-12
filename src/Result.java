public class Result {
    private String studentID, studentName, lecturerID, lecturerName, location, moduleName, grade;
    private int score;

    public Result(String[] parts) {
        this.studentID = parts[0].trim();
        this.studentName = parts[1].trim();
        this.lecturerName = parts[2].trim();
        this.moduleName = parts[3].trim();
        this.score = Integer.parseInt(parts[4].trim());
        // Only access index 5 if it exists, otherwise leave empty
        this.grade = (parts.length > 5) ? parts[5].trim() : "";
    }

    // Getters
    public String getStudentID() { return studentID; }
    public String getModuleName() { return moduleName; }
    public int getScore() { return score; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String toGradeFileString() {
        return String.join(",", studentID, studentName, lecturerName, moduleName, String.valueOf(score), grade);
    }
}