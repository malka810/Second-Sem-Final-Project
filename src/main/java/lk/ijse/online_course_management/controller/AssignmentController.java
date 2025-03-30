package lk.ijse.online_course_management.controller;

import lk.ijse.online_course_management.dto.AssignmentDTO;
import lk.ijse.online_course_management.dto.ResponseDTO;
import lk.ijse.online_course_management.service.AssignmentService;
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
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/assignments")
public class AssignmentController {

    @Autowired
    private final AssignmentService assignmentService;

    public static String uploadDirectory = System.getProperty("user.dir") + "/FrontEnd/webApp/file";

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDTO> uploadAssignment(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("dueDate") String dueDate,
            @RequestParam("courseId") UUID courseId,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            String fileUrl = null;
            if (file != null && !file.isEmpty()) {
                Path uploadPath = Paths.get(uploadDirectory);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                String originalFilename = file.getOriginalFilename();
                Path filePath = Paths.get(uploadDirectory, originalFilename);
                Files.write(filePath, file.getBytes());
                fileUrl = "/FrontEnd/webApp/file/" + originalFilename;
            }

            AssignmentDTO assignmentDTO = new AssignmentDTO();
            assignmentDTO.setTitle(title);
            assignmentDTO.setDescription(description);
            assignmentDTO.setDueDate(LocalDateTime.parse(dueDate));
            assignmentDTO.setFilePath(fileUrl);
            assignmentDTO.setCourseId(courseId);
            assignmentDTO.setCreatedAt(LocalDateTime.parse(LocalDateTime.now().toString()));

            int result = assignmentService.saveAssignment(assignmentDTO);

            switch (result) {
                case VarList.Created -> {
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Assignment created successfully", assignmentDTO));
                }
                case VarList.Not_Found -> {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(VarList.Not_Found, "Course not found", null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error creating assignment", null));
                }
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "File upload error: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> getAllAssignments() {
        try {
            List<AssignmentDTO> assignments = assignmentService.getAllAssignments();
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Success", assignments));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ResponseDTO> getAssignmentsByCourse(@PathVariable UUID courseId) {
        try {
            List<AssignmentDTO> assignments = assignmentService.getAssignmentsByCourse(courseId);
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Success", assignments));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{assignmentId}")
    public ResponseEntity<ResponseDTO> getAssignmentById(@PathVariable UUID assignmentId) {
        try {
            AssignmentDTO assignment = assignmentService.getAssignmentById(assignmentId);
            if (assignment != null) {
                return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Success", assignment));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "Assignment not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Error: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{assignmentId}")
    public ResponseEntity<ResponseDTO> updateAssignment(
            @PathVariable UUID assignmentId,
            @RequestBody AssignmentDTO assignmentDTO) {
        try {
            assignmentDTO.setId(assignmentId);
            int result = assignmentService.updateAssignment(assignmentDTO);

            if (result == VarList.OK) {
                return ResponseEntity.ok()
                        .body(new ResponseDTO(VarList.OK, "Assignment updated successfully", assignmentDTO));
            } else if (result == VarList.Not_Found) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "Assignment or Course not found", null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                        .body(new ResponseDTO(VarList.Bad_Gateway, "Error updating assignment", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<ResponseDTO> deleteAssignment(@PathVariable UUID assignmentId) {
        try {
            int result = assignmentService.deleteAssignment(assignmentId);
            if (result == VarList.OK) {
                return ResponseEntity.ok()
                        .body(new ResponseDTO(VarList.OK, "Assignment deleted successfully", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "Assignment not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
}