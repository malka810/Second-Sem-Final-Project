package lk.ijse.online_course_management.entity;

import jakarta.persistence.*;


import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "enrollment")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "enrollment_id")
    private UUID enrollmentId;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    private String courseTitle;
    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "user_id")
    private User student;
    private String studentName;
    private Date enrollDate;
    private int progress;
    private Date lastAccessed;


    public Enrollment() {
    }

    public Enrollment(UUID enrollmentId, Course course, String courseTitle, User student, String studentName, Date enrollDate, int progress, Date lastAccessed) {
        this.enrollmentId = enrollmentId;
        this.course = course;
        this.courseTitle = courseTitle;
        this.student = student;
        this.studentName = studentName;
        this.enrollDate = enrollDate;
        this.progress = progress;
        this.lastAccessed = lastAccessed;
    }

    public UUID getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(UUID enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Date getEnrollDate() {
        return enrollDate;
    }

    public void setEnrollDate(Date enrollDate) {
        this.enrollDate = enrollDate;
    }

    public int getProgress() {
        return progress;
    }
    public void setProgress(int progress) {
        this.progress = progress;
    }
    public Date getLastAccessed() {
        return lastAccessed;
    }
    public void setLastAccessed(Date lastAccessed) {}

    @Override
    public String toString() {
        return "Enrollment{" +
                "enrollmentId=" + enrollmentId +
                ", course=" + course +
                ", courseTitle='" + courseTitle + '\'' +
                ", student=" + student +
                ", studentName='" + studentName + '\'' +
                ", enrollDate=" + enrollDate +
                ", progress=" + progress +
                ", lastAccessed=" + lastAccessed +
                '}';
    }
}