package lk.ijse.online_course_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuthDTO {

    private Long logId;
    private String email;
    private String token;
    private String role;
    private LocalDateTime loginTime;

    public AuthDTO() {}
    public AuthDTO(Long logId, String email, String token, LocalDateTime loginTime, LocalDateTime logoutTime) {
        this.logId = logId;
        this.email = email;
        this.token = token;
        this.loginTime = loginTime;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "AuthDTO{" +
                "logId=" + logId +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", role='" + role + '\'' +
                ", loginTime=" + loginTime +
                '}';
    }
}
