package lk.ijse.online_course_management.repo;

import lk.ijse.online_course_management.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepo extends JpaRepository<Quiz, Integer> {
}
