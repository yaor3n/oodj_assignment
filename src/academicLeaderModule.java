public class academicLeaderModule {
    private String code, name, qualification, lecturer, intakeMonth, description, imageFilePath;
    private int year;
    
    public academicLeaderModule(String code, String name, String qualification, String lecturer, String intakeMonth, int year, String description, String imageFilePath){
        this.code = code;
        this.name = name;
        this.qualification = qualification;
        this.lecturer = lecturer;
        this.intakeMonth = intakeMonth;
        this.year = year;
        this.description = description;
        this.imageFilePath = imageFilePath;
    }
    
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getQualification() { return qualification; }
    public String getLecturerName() { return lecturer; }
    public String getIntakeMonth() { return intakeMonth; } 
    public int getYear() { return year; }
    public String getDescription() { return description; }
    public String getImageFilePath() { return imageFilePath; }
    
    @Override
    public String toString() {
        return code + "," + name + "," + qualification + "," + lecturer + "," + intakeMonth + "," + year + "," + description + "," + imageFilePath;
    }
}