package lk.ijse.online_course_management.entity;

import jakarta.persistence.*;

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
    public Course() {}

    public Course(UUID courseId, String title, String description, String instructorName, User instructor) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.instructorName = instructorName;
        this.instructor = instructor;
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

    @PrePersist
    @PreUpdate
    private void syncInstructorName() {
        if (instructor != null && instructorName == null) {
            instructorName = instructor.getFullName();
        }
    }

}