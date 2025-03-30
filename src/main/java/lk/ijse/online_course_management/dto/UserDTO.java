package lk.ijse.online_course_management.dto;

import java.util.UUID;

public class UserDTO {
    private UUID userId;
    private String fullName;
    private String email;
    private String password;
    private String role;
    private String profileImage;

  public UserDTO() {}
  public UserDTO(UUID userId, String fullName, String email, String password, String role, String profileImage) {
      this.userId = userId;
      this.fullName = fullName;
      this.email = email;
      this.password = password;
      this.role = role;
      this.profileImage = profileImage;
  }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }
}
