package lk.ijse.online_course_management.entity;

import jakarta.persistence.*;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "course_id")
    private UUID courseId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "instructor_name", nullable = false)
    private String instructorName;
    @ManyToOne
    @JoinColumn(name = "instructor_id", referencedColumnName = "user_id")
    private User instructor;
    @Setter
    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active = true;
    public Course() {}

    public Course(UUID courseId, String title, String description, String instructorName, User instructor, Boolean active) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.instructorName = instructorName;
        this.instructor = instructor;
        this.active = active;
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

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public User getInstructor() {
        return instructor;
    }

    public Course(String title, String description, String instructorName, User instructor) {
        this.title = title;
        this.description = description;
        this.instructorName = instructorName;
        this.instructor = instructor;
    }
    public void setInstructor(User instructor) {
        this.instructor = instructor;
        if (instructor != null) {
            this.instructorName = instructor.getFullName();
        }
    }

    public Boolean getActive() {
        return active;
    }

    @PrePersist
    @PreUpdate
    private void syncInstructorName() {
        if (instructor != null && instructorName == null) {
            instructorName = instructor.getFullName();
        }
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", instructorName='" + instructorName + '\'' +
                ", instructor=" + instructor +
                ", active=" + active +
                '}';
    }
}