package lk.ijse.online_course_management.controller;

import lk.ijse.online_course_management.dto.MaterialDTO;
import lk.ijse.online_course_management.dto.ResponseDTO;
import lk.ijse.online_course_management.service.MaterialService;
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
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/materials")
public class MaterialController {

    @Autowired
    private final MaterialService materialService;

    public static String uploadDirectory = System.getProperty("user.dir") + "/FrontEnd/webApp/videos";

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDTO> uploadMaterial(
            @RequestParam("title") String title,
            @RequestParam("courseId") UUID courseId,
            @RequestParam("file") MultipartFile file) {

        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save file
            String originalFilename = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDirectory, originalFilename);
            Files.write(filePath, file.getBytes());

            // Create material DTO
            MaterialDTO materialDTO = new MaterialDTO();
            materialDTO.setTitle(title);
            materialDTO.setFileUrl("/FrontEnd/webApp/videos" + originalFilename);
            materialDTO.setCourseId(courseId);

            int result = materialService.uploadMaterial(materialDTO);

            switch (result) {
                case VarList.Created -> {
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Material uploaded successfully", materialDTO));
                }
                case VarList.Not_Found -> {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(VarList.Not_Found, "Course not found", null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error uploading material", null));
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
    public ResponseEntity<ResponseDTO> getAllMaterials() {
        try {
            List<MaterialDTO> materials = materialService.getAllMaterials();
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Success", materials));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ResponseDTO> getMaterialsByCourse(@PathVariable UUID courseId) {
        try {
            List<MaterialDTO> materials = materialService.getAllMaterialsByCourse(courseId);
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Success", materials));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{materialId}")
    public ResponseEntity<ResponseDTO> getMaterialById(@PathVariable UUID materialId) {
        try {
            MaterialDTO material = materialService.getMaterialById(materialId);
            if (material != null) {
                return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Success", material));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "Material not found", null));
            }
        } catch (Exception e) {
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
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "Material or Course not found", null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                        .body(new ResponseDTO(VarList.Bad_Gateway, "Error updating material", null));
            }
        } catch (Exception e) {
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
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "Material not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
}