public class academicLeaderModule {
    private String code, name, qualification, lecturerID, lecturer, intakeMonth, description, imageFilePath, course;
    private int year;

    public academicLeaderModule(String code, String name, String qualification, String lecturerID, String lecturer, String intakeMonth, int year, String description, String imageFilePath, String course){
        this.code = code;
        this.name = name;
        this.qualification = qualification;
        this.lecturerID = lecturerID;
        this.lecturer = lecturer;
        this.intakeMonth = intakeMonth;
        this.year = year;
        this.description = description;
        this.imageFilePath = imageFilePath;
        this.course = course;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getQualification() { return qualification; }
    public String getLecturerID() { return lecturerID; }
    public String getLecturerName() { return lecturer; }
    public String getIntakeMonth() { return intakeMonth; }
    public int getYear() { return year; }
    public String getDescription() { return description; }
    public String getImageFilePath() { return imageFilePath; }
    public String getCourse() { return course; }

    @Override
    public String toString() {
        return code + "," + name + "," + qualification + "," + lecturerID + "," + lecturer + "," + intakeMonth + "," + year + "," + description + "," + imageFilePath + "," + course;
    }
}
