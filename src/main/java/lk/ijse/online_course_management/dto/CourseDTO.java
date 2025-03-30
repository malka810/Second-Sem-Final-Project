package lk.ijse.online_course_management.dto;

import lk.ijse.online_course_management.entity.Course;

import java.util.UUID;

public class CourseDTO {
    private UUID courseId;
    private String title;
    private String description;
    private UUID instructorId;
    private String instructorName;

    public CourseDTO(){}

    public CourseDTO(UUID courseId, String title, String description, UUID instructorId, String instructorName) {
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
        this.instructorName = instructorName;
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

    public static CourseDTO fromEntity(Course course) {
        return new CourseDTO(
                course.getCourseId(),
                course.getTitle(),
                course.getDescription(),
                course.getInstructor().getUserId(),
                course.getInstructorName()
        );
    }

    public Course toEntity() {
        return new Course(
                this.title,
                this.description,
                this.instructorName,
                null  // Instructor will be set separately
        );
    }
}