package lk.ijse.online_course_management.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class AssignmentSub {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID submissionId;
    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Assignment assignment;
    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "user_id")
    private User student;
    private String submissionFile;
    private LocalDateTime submittedAt;
    private Double marks;
    private String feedback;
    private boolean isGraded;

    public AssignmentSub() {}

    public AssignmentSub(UUID submissionId, Assignment assignment, User student, String submissionFile, LocalDateTime submittedAt, Double marks, String feedback, boolean isGraded) {
        this.submissionId = submissionId;
        this.assignment = assignment;
        this.student = student;
        this.submissionFile = submissionFile;
        this.submittedAt = submittedAt;
        this.marks = marks;
        this.feedback = feedback;
        this.isGraded = isGraded;
    }

    public UUID getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(UUID submissionId) {
        this.submissionId = submissionId;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public String getSubmissionFile() {
        return submissionFile;
    }

    public void setSubmissionFile(String submissionFile) {
        this.submissionFile = submissionFile;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Double getMarks() {
        return marks;
    }

    public void setMarks(Double marks) {
        this.marks = marks;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public boolean isGraded() {
        return isGraded;
    }

    public void setGraded(boolean graded) {
        isGraded = graded;
    }

    @Override
    public String toString() {
        return "AssignmentSub{" +
                "submissionId=" + submissionId +
                ", assignment=" + assignment +
                ", student=" + student +
                ", submissionFile='" + submissionFile + '\'' +
                ", submittedAt=" + submittedAt +
                ", marks=" + marks +
                ", feedback='" + feedback + '\'' +
                ", isGraded=" + isGraded +
                '}';
    }
}
