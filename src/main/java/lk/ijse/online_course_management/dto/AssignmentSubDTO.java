package lk.ijse.online_course_management.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class AssignmentSubDTO {
    private UUID submissionId;
    private UUID assignmentId;
    private String assignmentTitle;
    private UUID studentId;
    private String studentName;
    private String submissionFile;
    private LocalDateTime submittedAt;
    private Double marks;
    private String feedback;
    private boolean isGraded;

    public AssignmentSubDTO() {}

    public AssignmentSubDTO(UUID submissionId, UUID assignmentId, String assignmentTitle, UUID studentId, String studentName, String submissionFile, LocalDateTime submittedAt, Double marks, String feedback, boolean isGraded) {
        this.submissionId = submissionId;
        this.assignmentId = assignmentId;
        this.assignmentTitle = assignmentTitle;
        this.studentId = studentId;
        this.studentName = studentName;
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

    public UUID getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(UUID assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
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
        return "AssignmentSubDTO{" +
                "submissionId=" + submissionId +
                ", assignmentId=" + assignmentId +
                ", assignmentTitle='" + assignmentTitle + '\'' +
                ", studentId=" + studentId +
                ", studentName='" + studentName + '\'' +
                ", submissionFile='" + submissionFile + '\'' +
                ", submittedAt=" + submittedAt +
                ", marks=" + marks +
                ", feedback='" + feedback + '\'' +
                ", isGraded=" + isGraded +
                '}';
    }
}
