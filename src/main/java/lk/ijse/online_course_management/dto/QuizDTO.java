package lk.ijse.online_course_management.dto;

import lk.ijse.online_course_management.entity.Course;

import java.time.LocalDateTime;
import java.util.List;

public class QuizDTO {
    private int quizId;
    private Course course;
    private String title;
    private LocalDateTime createdAt;
    private List<QuizQuesDTO> questions;

    public QuizDTO() {}

    public QuizDTO(int quizId, Course course, String title, LocalDateTime createdAt, List<QuizQuesDTO> questions) {
        this.quizId = quizId;
        this.course = course;
        this.title = title;
        this.createdAt = createdAt;
        this.questions = questions;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<QuizQuesDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuizQuesDTO> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "QuizDTO{" +
                "quizId=" + quizId +
                ", course=" + course +
                ", title='" + title + '\'' +
                ", createdAt=" + createdAt +
                ", questions=" + questions +
                '}';
    }
}
