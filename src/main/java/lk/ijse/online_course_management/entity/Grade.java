package lk.ijse.online_course_management.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "grade")
public class Grade {
    @Id
    @Column(name = "grade_id")
    private String gradeId;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "user_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Course course;

    @OneToOne
    @JoinColumn(name = "submissionId", referencedColumnName = "submissionId")
    private AssignmentSub assignmentSub;

    @OneToOne
    @JoinColumn(name = "quizSub_id", referencedColumnName = "quizSub_id")
    private QuizSub quizSub;

    @Column(name = "final_grade")
    private double finalGrade;

    public Grade() {}


    public Grade(String gradeId, User student, Course course, AssignmentSub assignmentSub, QuizSub quizSub, double finalGrade) {
        this.gradeId = gradeId;
        this.student = student;
        this.course = course;
        this.assignmentSub = assignmentSub;
        this.quizSub = quizSub;
        this.finalGrade = finalGrade;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public AssignmentSub getAssignmentSub() {
        return assignmentSub;
    }

    public void setAssignmentSub(AssignmentSub assignmentSub) {
        this.assignmentSub = assignmentSub;
    }

    public QuizSub getQuizSub() {
        return quizSub;
    }

    public void setQuizSub(QuizSub quizSub) {
        this.quizSub = quizSub;
    }

    public double getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(double finalGrade) {
        this.finalGrade = finalGrade;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "gradeId='" + gradeId + '\'' +
                ", student=" + student +
                ", course=" + course +
                ", assignmentSub=" + assignmentSub +
                ", quizSub=" + quizSub +
                ", finalGrade=" + finalGrade +
                '}';
    }
}