public class Student extends User {
    private String displayName;

    public Student(String id, String fullName, String email, String gender,
                   String dob, String username, String password,
                   String role, String course) {

        // super must be the first line - it calls the User constructor
        super(id, fullName, email, gender, dob, username, password, role, course);

        // Initialize Student-specific field
        this.displayName = (fullName != null && !fullName.isBlank()) ? fullName : username;
    }

    // Getters for specific User fields (inherited)
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getCourse() { return course; }

    // Student specific logic
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}