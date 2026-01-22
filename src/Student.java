public class Student {

    private String username;
    private String password;
    private String role;
    private String displayName;

    public Student(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.displayName = (displayName == null || displayName.isBlank())
                ? username
                : displayName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getDisplayName(){
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

