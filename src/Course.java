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
        return code + "," + name + "," + qualification + "," + lecturer + ","
                + intakeMonth + "," + year + "," + description + "," + imageFilePath;
    }

    public static Course fromFileString(String line) {
        // Split by comma
        String[] parts = line.split(",");

        // The new file format has 10 parts:
        // 0:Code, 1:Name, 2:Level, 3:LecID, 4:LecName, 5:Month, 6:Year, 7:Desc, 8:Image, 9:Field
        if (parts.length < 10) {
            throw new IllegalArgumentException("Invalid course line: " + line);
        }

        return new Course(
                parts[0].trim(),                   // code (M001)
                parts[1].trim(),                   // name (Book Keeping 101)
                parts[2].trim(),                   // qualification (Foundation)
                parts[4].trim(),                   // lecturer (Ms. Nurul - Index 4, not 3!)
                parts[5].trim(),                   // intake month (March)
                Integer.parseInt(parts[6].trim()), // year (2026)
                parts[7].trim(),                   // description
                parts[8].trim()                    // image path
        );
    }

}

