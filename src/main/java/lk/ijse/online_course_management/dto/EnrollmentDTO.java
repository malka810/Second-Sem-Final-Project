package lk.ijse.online_course_management.dto;

import java.util.Date;
import java.util.UUID;

public class EnrollmentDTO {
    private UUID enrollmentId;
    private UUID courseId;
    private String courseTitle;
    private UUID studentId;
    private String studentName;
    private Date enrollDate;

    public EnrollmentDTO() {
    }

    public EnrollmentDTO(UUID enrollmentId, UUID courseId, String courseTitle, UUID studentId, String studentName, Date enrollDate) {
        this.enrollmentId = enrollmentId;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.studentId = studentId;
        this.studentName = studentName;
        this.enrollDate = enrollDate;
    }

    public UUID getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(UUID enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
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

    @Override
    public String toString() {
        return "EnrollmentDTO{" +
                "enrollmentId=" + enrollmentId +
                ", courseId=" + courseId +
                ", courseTitle='" + courseTitle + '\'' +
                ", studentId=" + studentId +
                ", studentName='" + studentName + '\'' +
                ", enrollDate=" + enrollDate +
                '}';
    }
}