// Manager.java
package model;

public class Manager {
    private int managerId;
    private String fullName;
    private String email;
    private String phone;
    private String department;
    private String username;
    private String password;

    public Manager(int managerId, String fullName, String email, String phone,
                  String department, String username, String password) {
        this.managerId = managerId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.username = username;
        this.password = password;
    }

    // Getters and setters
    public int getManagerId() { return managerId; }
    public void setManagerId(int managerId) { this.managerId = managerId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}