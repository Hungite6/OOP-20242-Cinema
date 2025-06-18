package manager.model; // Cập nhật package name

import java.time.LocalDateTime;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private String password;
    private String cityName;
    private boolean isSuperUser;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(int id, String firstName, String lastName, String emailAddress, String phoneNumber, String password, String cityName, boolean isSuperUser, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.cityName = cityName;
        this.isSuperUser = isSuperUser;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Constructor for new user (password, createdAt, updatedAt handled by DB or for display)
    public User(int id, String firstName, String lastName, String emailAddress, String phoneNumber, String cityName, boolean isSuperUser) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.cityName = cityName;
        this.isSuperUser = isSuperUser;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }
    public boolean getIsSuperUser() { return isSuperUser; }
    public void setIsSuperUser(boolean superUser) { isSuperUser = superUser; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getRole() {
        return isSuperUser ? "Admin" : "User";
    }
}