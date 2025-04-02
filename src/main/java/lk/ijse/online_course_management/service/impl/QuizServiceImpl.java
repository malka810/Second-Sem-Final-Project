package lk.ijse.online_course_management.service.impl;

import lk.ijse.online_course_management.dto.QuizDTO;
import lk.ijse.online_course_management.service.QuizService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QuizServiceImpl implements QuizService {
    @Override
    public int saveQuiz(QuizDTO quizDTO) {
        return 0;
    }
}
