package lk.ijse.online_course_management.dto;

import lk.ijse.online_course_management.entity.Course;

import java.util.UUID;

public class CourseDTO {
    private UUID courseId;
    private String title;
    private String description;
    private UUID instructorId;
    private String instructorName;
    private String imagePath;

    public CourseDTO(){}

    public CourseDTO(UUID courseId, String title, String description, UUID instructorId, String instructorName, String imagePath) {
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
        this.instructorName = instructorName;
        this.imagePath = imagePath;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
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

    public UUID getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(UUID instructorId) {
        this.instructorId = instructorId;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "CourseDTO{" +
                "courseId=" + courseId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", instructorId=" + instructorId +
                ", instructorName='" + instructorName + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}