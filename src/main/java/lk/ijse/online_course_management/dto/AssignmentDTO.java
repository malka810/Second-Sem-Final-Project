package lk.ijse.online_course_management.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class AssignmentDTO {
    private UUID id;
    private String title;
    private String description;
    private String filePath;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private UUID courseId;

    public AssignmentDTO() {}

    public AssignmentDTO(UUID id, String title, String description, String filePath, LocalDateTime dueDate, LocalDateTime createdAt, UUID courseId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.filePath = filePath;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.courseId = courseId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "AssignmentDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", filePath='" + filePath + '\'' +
                ", dueDate=" + dueDate +
                ", createdAt=" + createdAt +
                ", courseId=" + courseId +
                '}';
    }
}
