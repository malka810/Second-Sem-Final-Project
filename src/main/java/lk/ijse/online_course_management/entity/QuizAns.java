package lk.ijse.online_course_management.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "quiz_answer")
public class QuizAns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long answerId;

    @ManyToOne
    @JoinColumn(name = "quizSub_id", nullable = false)
    private QuizSub quizSubmission;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuizQues question;

    @Column(name = "selected_answer", nullable = false)
    private String selectedAnswer;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "marks_awarded")
    private Double marksAwarded;

    public QuizAns() {}


    public QuizAns(Long answerId, QuizSub quizSubmission, QuizQues question, String selectedAnswer, Boolean isCorrect, Double marksAwarded) {
        this.answerId = answerId;
        this.quizSubmission = quizSubmission;
        this.question = question;
        this.selectedAnswer = selectedAnswer;
        this.isCorrect = isCorrect;
        this.marksAwarded = marksAwarded;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public QuizSub getQuizSubmission() {
        return quizSubmission;
    }

    public void setQuizSubmission(QuizSub quizSubmission) {
        this.quizSubmission = quizSubmission;
    }

    public QuizQues getQuestion() {
        return question;
    }

    public void setQuestion(QuizQues question) {
        this.question = question;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public Double getMarksAwarded() {
        return marksAwarded;
    }

    public void setMarksAwarded(Double marksAwarded) {
        this.marksAwarded = marksAwarded;
    }

    @Override
    public String toString() {
        return "QuizAns{" +
                "answerId=" + answerId +
                ", quizSubmission=" + quizSubmission +
                ", question=" + question +
                ", selectedAnswer='" + selectedAnswer + '\'' +
                ", isCorrect=" + isCorrect +
                ", marksAwarded=" + marksAwarded +
                '}';
    }
}