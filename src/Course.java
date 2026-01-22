public class Course {

    private String code;
    private String name;
    private String qualification;
    private String lecturer;
    private String intakeMonth;
    private int year;
    private String description;
    private String imageFilePath;

    public Course(String code, String name, String qualification,
                  String lecturer, String intakeMonth, int year,
                  String description, String imageFilePath) {

        this.code = code;
        this.name = name;
        this.qualification = qualification;
        this.lecturer = lecturer;
        this.intakeMonth = intakeMonth;
        this.year = year;
        this.description = description;
        this.imageFilePath = imageFilePath;
    }

    // ===== Getters =====
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getQualification() { return qualification; }
    public String getLecturer() { return lecturer; }
    public String getIntakeMonth() { return intakeMonth; }
    public int getYear() { return year; }
    public String getDescription() { return description; }
    public String getImageFilePath() { return imageFilePath; }

    // ===== File Format =====
    public String toFileString() {
        return code + "|" + name + "|" + qualification + "|" + lecturer + "|"
                + intakeMonth + "|" + year + "|" + description + "|" + imageFilePath;
    }

    public static Course fromFileString(String line) {

        String[] parts = line.split("\\|");

        if (parts.length != 8) {
            throw new IllegalArgumentException("Invalid course line: " + line);
        }

        return new Course(
                parts[0].trim(),                 // code
                parts[1].trim(),                 // name
                parts[2].trim(),                 // qualification
                parts[3].trim(),                 // lecturer
                parts[4].trim(),                 // intake month
                Integer.parseInt(parts[5].trim()), // year
                parts[6].trim(),                 // description
                parts[7].trim()                  // image path
        );
    }

}

