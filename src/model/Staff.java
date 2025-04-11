// Staff.java
package model;

public class Staff {
    private int staffId;
    private String fullName;
    private String email;
    private String phone;
    private String position;
    private String status;
    private String username;
    private String password;

    public Staff(int staffId, String fullName, String email, String phone, 
                String position, String status, String username, String password) {
        this.staffId = staffId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.position = position;
        this.status = status;
        this.username = username;
        this.password = password;
    }

    // Getters and setters
    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}