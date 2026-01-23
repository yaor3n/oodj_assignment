public class Module {
    private String id, name, level, lecturerId, lecturer, month, year, imagePath, courseName,
            classId, className, location, date, startTime, endTime;

    public Module(String[] p) {
        this.id = p[0];
        this.name = p[1];
        this.level = p[2];
        this.lecturerId = p[3];
        this.lecturer = p[4];
        this.month = p[5];
        this.year = p[6];
        this.imagePath = p[7];
        this.courseName = p[8];
        this.classId = p[9];
        this.className = p[10];
        this.location = p[11];
        this.date = p[12];
        this.startTime = p[13];
        this.endTime = p[14];
    }

    // Getters
    public String getName() { return name; }
    public String getCourseName() { return courseName; }
    public String getLecturer() { return lecturer; }
    public String getThisLecturer() {return this.lecturer;}
    public String getDetails() {
        return String.format("Date: %s<br>Time: %s - %s<br>Location: %s (%s)",
                date, startTime, endTime, location, className);
    }
    public String getImagePath() { return imagePath; }
    public String getclassName() { return className; }
    public String getLecturerId() {return lecturerId; }
}