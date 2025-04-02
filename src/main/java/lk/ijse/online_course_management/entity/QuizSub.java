package lk.ijse.online_course_management.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "quiz_submission")
public class QuizSub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quizSub_id")
    private Long submissionId;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "user_id")
    private User student;

    @Column(name = "submission_time", nullable = false)
    private LocalDateTime submissionTime;

    @Column(name = "score")
    private Double score;

    @Column(name = "total_marks")
    private Double totalMarks;

    @OneToMany(mappedBy = "quizSubmission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizAns> answers;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SubmissionStatus status;

    public enum SubmissionStatus {
        SUBMITTED,
        GRADED,
        PENDING_REVIEW
    }

    public QuizSub() {}

    public QuizSub(Long submissionId, Quiz quiz, User student, LocalDateTime submissionTime, Double score, Double totalMarks, List<QuizAns> answers, SubmissionStatus status) {
        this.submissionId = submissionId;
        this.quiz = quiz;
        this.student = student;
        this.submissionTime = submissionTime;
        this.score = score;
        this.totalMarks = totalMarks;
        this.answers = answers;
        this.status = status;
    }

    public Long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(LocalDateTime submissionTime) {
        this.submissionTime = submissionTime;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(Double totalMarks) {
        this.totalMarks = totalMarks;
    }

    public List<QuizAns> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuizAns> answers) {
        this.answers = answers;
    }

    public SubmissionStatus getStatus() {
        return status;
    }

    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "QuizSub{" +
                "submissionId=" + submissionId +
                ", quiz=" + quiz +
                ", student=" + student +
                ", submissionTime=" + submissionTime +
                ", score=" + score +
                ", totalMarks=" + totalMarks +
                ", answers=" + answers +
                ", status=" + status +
                '}';
    }
}