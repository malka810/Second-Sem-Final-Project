package lk.ijse.online_course_management.controller;

import lk.ijse.online_course_management.dto.AssignmentSubDTO;
import lk.ijse.online_course_management.dto.ResponseDTO;
import lk.ijse.online_course_management.service.AssignmentSubService;
import lk.ijse.online_course_management.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/assignment-submissions")
public class AssignmentSubController {

    @Autowired
    private AssignmentSubService assignmentSubService;

    public static String uploadDirectory = System.getProperty("user.dir") + "/FrontEnd/webApp/subFile";

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDTO> submitAssignment(
            @RequestParam("assignmentId") UUID assignmentId,
            @RequestParam("studentId") UUID studentId,
            @RequestParam("file") MultipartFile file) {

        try {
            Path uploadPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID() + fileExtension;

            Path filePath = Paths.get(uploadDirectory, uniqueFilename);
            Files.write(filePath, file.getBytes());

            AssignmentSubDTO submissionDTO = new AssignmentSubDTO();
            submissionDTO.setAssignmentId(assignmentId);
            submissionDTO.setStudentId(studentId);
            submissionDTO.setSubmissionFile("/uploads/assignments/" + uniqueFilename);
            submissionDTO.setSubmittedAt(LocalDateTime.now());
            submissionDTO.setGraded(false);

            String response = String.valueOf(assignmentSubService.saveSubmission(submissionDTO));

            if (response.equals("00")) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created, "Assignment submitted successfully", submissionDTO));
            } else if (response.equals("06")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDTO(VarList.RSP_DUPLICATED, "Already submitted this assignment", null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDTO(VarList.RSP_ERROR, "Error submitting assignment", null));
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.RSP_ERROR, "File upload error: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.RSP_ERROR, e.getMessage(), null));
        }
    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<ResponseDTO> getSubmissionById(@PathVariable UUID submissionId) {
        try {
            AssignmentSubDTO submission = assignmentSubService.getSubmissionById(submissionId);
            if (submission != null) {
                return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Success", submission));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "Submission not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.RSP_ERROR, e.getMessage(), null));
        }
    }

    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<ResponseDTO> getSubmissionsByAssignment(@PathVariable UUID assignmentId) {
        try {
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Success",
                    assignmentSubService.getSubmissionsByAssignment(assignmentId)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.RSP_ERROR, e.getMessage(), null));
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ResponseDTO> getSubmissionsByStudent(@PathVariable UUID studentId) {
        try {
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Success",
                    assignmentSubService.getSubmissionsByStudent(studentId)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.RSP_ERROR, e.getMessage(), null));
        }
    }

    @PutMapping("/grade/{submissionId}")
    public ResponseEntity<ResponseDTO> gradeSubmission(
            @PathVariable UUID submissionId,
            @RequestBody AssignmentSubDTO submissionDTO) {
        try {
            submissionDTO.setSubmissionId(submissionId);
            String response = String.valueOf(assignmentSubService.gradeSubmission(submissionDTO));

            if (response.equals("00")) {
                return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Submission graded successfully", null));
            } else if (response.equals("01")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "Submission not found", null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDTO(VarList.RSP_ERROR, "Error grading submission", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.RSP_ERROR, e.getMessage(), null));
        }
    }
}