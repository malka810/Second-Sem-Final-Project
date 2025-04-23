package lk.ijse.online_course_management.controller;

import lk.ijse.online_course_management.dto.MaterialDTO;
import lk.ijse.online_course_management.dto.ResponseDTO;
import lk.ijse.online_course_management.service.EnrollmentService;
import lk.ijse.online_course_management.service.MaterialService;
import lk.ijse.online_course_management.util.VarList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/v1/materials")
public class MaterialController {

    private static final Logger log = LoggerFactory.getLogger(MaterialController.class);

    private final MaterialService materialService;
    private final EnrollmentService enrollmentService;

    public static String uploadDirectory = System.getProperty("user.dir") + "/FrontEnd/webApp/videos";

    private static final long MAX_FILE_SIZE = 500 * 1024 * 1024;

    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList(
            "video/mp4",
            "video/webm",
            "video/ogg"
    );

    @Autowired
    public MaterialController(MaterialService materialService, EnrollmentService enrollmentService) {
        this.materialService = materialService;
        this.enrollmentService = enrollmentService;

    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> uploadMaterial(
            @RequestParam("title") String title,
            @RequestParam("courseId") UUID courseId,
            @RequestParam("file") MultipartFile file) {

        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ResponseDTO(VarList.Bad_Request, "File cannot be empty", null));
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                return ResponseEntity.badRequest()
                        .body(new ResponseDTO(VarList.Bad_Request, "File size exceeds maximum limit (500MB)", null));
            }
            if (!ALLOWED_MIME_TYPES.contains(file.getContentType())) {
                return ResponseEntity.badRequest()
                        .body(new ResponseDTO(VarList.Bad_Request, "Only video files (MP4, WebM, Ogg) are allowed", null));
            }

            Path uploadPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueName = UUID.randomUUID() + extension;
            Path filePath = uploadPath.resolve(uniqueName);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
            MaterialDTO dto = new MaterialDTO();
            dto.setTitle(title);
            dto.setCourseId(courseId);
            dto.setUploadAt(new Date());
            dto.setFileUrl(uniqueName);

            int result = materialService.uploadMaterial(dto);
            if (result == VarList.Created) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created, "Material uploaded successfully", dto));
            } else {
                Files.deleteIfExists(filePath);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseDTO(result, "Failed to save material information", null));
            }
        } catch (IOException e) {
            log.error("File upload error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "File upload error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> getAllMaterials() {
        try {
            List<MaterialDTO> materials = materialService.getAllMaterials();
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Success", materials));
        } catch (Exception e) {
            log.error("Error loading all materials: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ResponseDTO> getMaterialsByCourse(
            @PathVariable UUID courseId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");

            if (!enrollmentService.isUserEnrolled(token, courseId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ResponseDTO(VarList.Forbidden, "Not enrolled in this course", null));
            }
            List<MaterialDTO> materials = materialService.getAllMaterialsByCourse(courseId);
            return ResponseEntity.ok(new ResponseDTO(VarList.Retrieved, "Success", materials));

        } catch (Exception e) {
            log.error("Error loading materials for course {}: {}", courseId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Error loading materials", null));
        }
    }


    @GetMapping("/{materialId}")
    public ResponseEntity<ResponseDTO> getMaterialById(@PathVariable UUID materialId) {
        try {
            MaterialDTO material = materialService.getMaterialById(materialId);
            if (material != null) {
                return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Success", material));
            } else {
                log.warn("Material not found with ID: {}", materialId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "Material not found", null));
            }
        } catch (Exception e) {
            log.error("Error getting material by ID {}: {}", materialId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Error: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{materialId}")
    public ResponseEntity<ResponseDTO> updateMaterial(
            @PathVariable UUID materialId,
            @RequestBody MaterialDTO materialDTO) {
        try {
            materialDTO.setMaterialId(materialId);
            int result = materialService.updateMaterial(materialDTO);

            if (result == VarList.OK) {
                return ResponseEntity.ok()
                        .body(new ResponseDTO(VarList.OK, "Material updated successfully", null));
            } else if (result == VarList.Not_Found) {
                log.warn("Material or Course not found for update with ID: {}", materialId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "Material or Course not found", null));
            } else {
                log.error("Error updating material with ID: {}", materialId);
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                        .body(new ResponseDTO(VarList.Bad_Gateway, "Error updating material", null));
            }
        } catch (Exception e) {
            log.error("Error updating material {}: {}", materialId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{materialId}")
    public ResponseEntity<ResponseDTO> deleteMaterial(@PathVariable UUID materialId) {
        try {
            int result = materialService.deleteMaterial(materialId);
            if (result == VarList.OK) {
                return ResponseEntity.ok()
                        .body(new ResponseDTO(VarList.OK, "Material deleted successfully", null));
            } else {
                log.warn("Material not found for deletion with ID: {}", materialId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "Material not found", null));
            }
        } catch (Exception e) {
            log.error("Error deleting material {}: {}", materialId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/videos/{filename:.+}")
    public ResponseEntity<Resource> serveVideo(@PathVariable String filename) {
        try {
            if (filename.contains("..")) {
                log.warn("Invalid filename with path traversal: {}", filename);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Path filePath = Paths.get(uploadDirectory).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            log.info("Attempting to access file at: {}", filePath);

            if (!resource.exists()) {
                log.error("File not found at: {}", filePath);
                return ResponseEntity.notFound().build();
            }

            if (!resource.isReadable()) {
                log.error("File exists but not readable at: {}", filePath);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            String contentType = determineContentType(filename);
            log.debug("Serving file {} with content type {}", filename, contentType);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=3600")
                    .body(resource);
        } catch (Exception e) {
            log.error("Error serving video file {}: {}", filename, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    private String determineContentType(String filename) {
        String ext = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        return switch (ext) {
            case "mp4" -> "video/mp4";
            case "webm" -> "video/webm";
            case "ogg" -> "video/ogg";
            default -> "application/octet-stream";
        };
    }
}