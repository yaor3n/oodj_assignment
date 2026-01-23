public class User {
    String id;
    String fullName;
    String email;
    String gender;
    String dob;
    String username;
    String password;
    String role;
    String course;

    public User(String id, String fullName, String email, String gender,
                String dob, String username, String password,
                String role, String course) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.gender = gender;
        this.dob = dob;
        this.username = username;
        this.password = password;
        this.role = role;
        this.course = course;
    }

    public String toFileString() {
        return id + "," + fullName + "," + email + "," + gender + "," + dob + ","
                + username + "," + password + "," + role + "," + course;
    }

    public String getUsername() {
        return this.username;
    }
    public String getRole() {
        return this.role;
    }
    public String getPassword() {
        return this.password;
    }
    public String getfullName() {
        return this.fullName;
    }
}
