package lk.ijse.online_course_management.controller;

import jakarta.persistence.EntityNotFoundException;
import lk.ijse.online_course_management.dto.EnrollmentDTO;
import lk.ijse.online_course_management.dto.ResponseDTO;
import lk.ijse.online_course_management.repo.UserRepo;
import lk.ijse.online_course_management.service.EnrollmentService;
import lk.ijse.online_course_management.util.JwtUtil;
import lk.ijse.online_course_management.util.VarList;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/enrollments")
@CrossOrigin(origins = "*")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepo userRepo;

    private static final Logger log = LoggerFactory.getLogger(EnrollmentController.class);


    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> getAllEnrollments() {
        try {
            List<EnrollmentDTO> enrollments = enrollmentService.getAllEnrollments();
            return ResponseEntity.ok(new ResponseDTO(VarList.Retrieved, "All enrollments retrieved", enrollments));
        } catch (Exception e) {
            log.error("Error fetching enrollments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.RSP_ERROR, "Error fetching enrollments", null));
        }
    }

    @GetMapping("/student")
    public ResponseEntity<ResponseDTO> getStudentEnrollments(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            UUID studentId = jwtUtil.getUserIdFromToken(token);

            List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
            return ResponseEntity.ok(new ResponseDTO(VarList.Retrieved, "Student enrollments retrieved", enrollments));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(VarList.Not_Found, e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error fetching student enrollments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.RSP_ERROR, "Error fetching enrollments", null));
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ResponseDTO> getCourseEnrollments(@PathVariable UUID courseId) {
        try {
            List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByCourse(courseId);
            return ResponseEntity.ok(new ResponseDTO(VarList.Retrieved, "Course enrollments retrieved", enrollments));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(VarList.Not_Found, e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error fetching course enrollments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.RSP_ERROR, "Error fetching enrollments", null));
        }
    }

    @GetMapping("/my-courses")
    public ResponseEntity<ResponseDTO> getMyCoursesWithProgress(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            UUID studentId = jwtUtil.getUserIdFromTokenOrEmail(token, userRepo);

            List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsWithProgress(studentId);
            return ResponseEntity.ok(new ResponseDTO(VarList.Retrieved, "Courses with progress retrieved", enrollments));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(VarList.Not_Found, e.getMessage(), null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(VarList.Bad_Request, "Invalid token format", null));
        } catch (Exception e) {
            log.error("Error fetching courses with progress", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.RSP_ERROR, "Error fetching courses", null));
        }
    }
    @PatchMapping("/progress")
    public ResponseEntity<ResponseDTO> updateCourseProgress(
            @RequestBody EnrollmentDTO enrollmentDTO,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            UUID studentId = jwtUtil.getUserIdFromToken(token);

            if (!enrollmentService.verifyEnrollmentOwnership(enrollmentDTO.getEnrollmentId(), studentId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ResponseDTO(VarList.Not_Acceptable, "Not authorized to update this enrollment", null));
            }

            EnrollmentDTO updatedEnrollment = enrollmentService.updateCourseProgress(enrollmentDTO);
            return ResponseEntity.ok(new ResponseDTO(VarList.Updated, "Progress updated", updatedEnrollment));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(VarList.Not_Found, e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error updating progress", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.RSP_ERROR, "Error updating progress", null));
        }
    }

    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<ResponseDTO> cancelEnrollment(
            @PathVariable UUID enrollmentId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            UUID studentId = jwtUtil.getUserIdFromToken(token);

            if (!enrollmentService.verifyEnrollmentOwnership(enrollmentId, studentId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ResponseDTO(VarList.Not_Acceptable, "Not authorized to cancel this enrollment", null));
            }

            enrollmentService.deleteEnrollment(enrollmentId);
            return ResponseEntity.ok(new ResponseDTO(VarList.Deleted, "Enrollment cancelled", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(VarList.Not_Found, e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error cancelling enrollment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.RSP_ERROR, "Error cancelling enrollment", null));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<ResponseDTO> verifyEnrollment(
            @RequestParam @NotNull UUID courseId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ResponseDTO(VarList.Unauthorized, "Invalid authorization header", null));
            }

            String token = authHeader.substring(7);

            UUID userId = jwtUtil.getUserIdFromToken(token);

            boolean isEnrolled = enrollmentService.isUserEnrolled(userId, courseId);

            Map<String, Object> response = Map.of(
                    "isEnrolled", isEnrolled,
                    "courseId", courseId
            );

            return ResponseEntity.ok(new ResponseDTO(VarList.Retrieved, "Verification complete", response));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(VarList.Not_Found, "User not found", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseDTO(VarList.Unauthorized, "Invalid token", null));
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Error verifying enrollment", null));
        }
    }

    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    public static class EnrollmentException extends RuntimeException {
        public EnrollmentException(String message) {
            super(message);
        }
    }

    @PostMapping("/enroll")
    public ResponseEntity<ResponseDTO> enrollInCourse(
            @RequestBody EnrollmentDTO enrollmentDTO,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Validate authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ResponseDTO(VarList.Unauthorized, "Invalid authorization header", null));
            }

            String token = authHeader.substring(7);
            UUID studentId = jwtUtil.getUserIdFromToken(token);

            // Set student ID from token (not from request body for security)
            enrollmentDTO.setStudentId(studentId);

            EnrollmentDTO createdEnrollment = enrollmentService.createEnrollment(enrollmentDTO);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO(VarList.Created, "Enrollment successful", createdEnrollment));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(VarList.Not_Found, e.getMessage(), null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(VarList.Bad_Request, "Invalid request data", null));
        } catch (Exception e) {
            log.error("Error creating enrollment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.RSP_ERROR, "Error creating enrollment", null));
        }
    }
}