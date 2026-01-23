@Override
public void actionPerformed(ActionEvent e) {
    if (e.getSource() == loginButton) {
        String username = usernameInput.getText().trim();
        String password = new String(passwordInput.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both Username and Password!",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Authenticate using file manager
        String[] userData = academicLeaderFileManager.authenticate(username, password);

        if (userData != null) {
            String role = userData[2].trim();

            switch (role) {
                case "Admin":
                    new adminDashboard();
                    break;
                case "Lecturer":
                    new lecturerDashboard(username);
                    break;
                case "Student":
                    new studentDashboard();
                    break;
                case "AcademicLeader":
                    new academicLeaderDashboard();
                    break;
                default:
                    JOptionPane.showMessageDialog(this,
                            "Role not recognized: " + role,
                            "Login Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
            }

            // Close login window after successful login
            this.dispose();

        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid Username or Password!",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
