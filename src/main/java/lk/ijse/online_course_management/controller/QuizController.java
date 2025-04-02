package lk.ijse.online_course_management.controller;

import lk.ijse.online_course_management.dto.CourseDTO;
import lk.ijse.online_course_management.dto.QuizDTO;
import lk.ijse.online_course_management.dto.ResponseDTO;
import lk.ijse.online_course_management.service.QuizService;
import lk.ijse.online_course_management.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/quiz")
@CrossOrigin

public class QuizController {
    @Autowired
    QuizService quizService;
    @PostMapping("/add")
    public ResponseEntity<ResponseDTO> createQuiz(@RequestBody QuizDTO quizDTO) {
        try {
            int createdQuiz = quizService.saveQuiz(quizDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO(VarList.Success, "quiz created successfully", createdQuiz));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.RSP_ERROR, "Error: " + e.getMessage(), null));
        }
    }

}