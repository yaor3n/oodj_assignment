public class Result {
    private String studentID, studentName, lecturerID, lecturerName, location, moduleName, grade;
    private int score;

    public Result(String[] p) {
        this.studentID = p[0];
        this.studentName = p[1];
        this.lecturerID = p[2];
        this.lecturerName = p[3];
        this.location = p[4];
        this.moduleName = p[5];
        this.score = Integer.parseInt(p[6].trim());
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