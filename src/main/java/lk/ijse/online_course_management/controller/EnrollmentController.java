package lk.ijse.online_course_management.controller;

import lk.ijse.online_course_management.dto.CourseDTO;
import lk.ijse.online_course_management.dto.EnrollmentDTO;
import lk.ijse.online_course_management.dto.ResponseDTO;
import lk.ijse.online_course_management.service.EnrollmentService;
import lk.ijse.online_course_management.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/enrollments")
@CrossOrigin(origins = "*")
public class EnrollmentController {
@Autowired
    private EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/enroll")
    public ResponseEntity<ResponseDTO> enrollCourse(@RequestBody EnrollmentDTO enrollmentDTO) {
        try {
            int createdCourse = enrollmentService.saveEnroll(enrollmentDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO(VarList.Success, " Student enroll successfully", createdCourse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.RSP_ERROR, "Error: " + e.getMessage(), null));
        }
    }
    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> getAllEnrollments() {
        try {
            List<EnrollmentDTO> enrollments = enrollmentService.getAllEnrollments();
            return ResponseEntity.ok(new ResponseDTO(VarList.Success, "Success", enrollments));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.RSP_ERROR, "Error: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<ResponseDTO> deleteEnrollment(@PathVariable UUID enrollmentId) {
        try {
            enrollmentService.deleteEnrollment(enrollmentId);
            return ResponseEntity.ok(new ResponseDTO(VarList.Success, "Enrollment deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.RSP_ERROR, "Error: " + e.getMessage(), null));
        }
    }
}