package lk.ijse.online_course_management.controller;

import lk.ijse.online_course_management.dto.AuthDTO;
import lk.ijse.online_course_management.dto.ResponseDTO;
import lk.ijse.online_course_management.dto.UserDTO;
import lk.ijse.online_course_management.service.UserService;
import lk.ijse.online_course_management.util.JwtUtil;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("api/v1/user")
public class UserController {
    @Autowired
    private final UserService userService;
    private final JwtUtil jwtUtil;
    public static String uploadDirectory = System.getProperty("user.dir") + "/FrontEnd/webApp/images";
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> registerUser(
            @RequestPart("user") @Validated UserDTO userDTO,
            @RequestPart(value = "profileImage", required = false) MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body(new ResponseDTO(VarList.Payload_Too_Large, "File size exceeds maximum limit of 10MB", null));
                }
                String uniqueFilename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDirectory);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(uniqueFilename).normalize();
                Files.write(filePath, file.getBytes());
                userDTO.setProfileImage(uniqueFilename);
            }

            int res = userService.saveUser(userDTO);

            switch (res) {
                case VarList.Created -> {
                    String token = jwtUtil.generateToken(userDTO);
                    AuthDTO authDTO = new AuthDTO();
                    authDTO.setEmail(userDTO.getEmail());
                    authDTO.setToken(token);
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Success", authDTO));
                }
                case VarList.Not_Acceptable -> {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(new ResponseDTO(VarList.Not_Acceptable, "Email Already Used", null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> getAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers();
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Success", users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDTO> getUserById(@PathVariable UUID userId) {
        try {
            UserDTO user = userService.getUserById(userId);
            if (user != null) {
                return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Success", user));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "User not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<ResponseDTO> updateUser(
            @PathVariable UUID userId,
            @RequestBody UserDTO userDTO) {
        try {
            userDTO.setUserId(userId);
            int result = userService.updateUser(userDTO);
            if (result == VarList.OK) {
                return ResponseEntity.ok()
                        .body(new ResponseDTO(VarList.OK, "User updated successfully", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "User not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ResponseDTO> deleteUser(@PathVariable("userId") UUID userId) {
        try {
            int result = userService.deleteUser(userId);
            if (result == VarList.OK) {
                return ResponseEntity.ok()
                        .body(new ResponseDTO(VarList.OK, "User deleted successfully", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "User not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<ResponseDTO> getUsersByRole(@PathVariable String role) {
        try {
            List<UserDTO> users = userService.getUsersByRole(role);
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Success", users));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(VarList.Bad_Request, "Invalid role specified", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @PostMapping("/reset-password/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> resetPassword(@PathVariable UUID userId) {
        try {
            String defaultPassword = "password123";
            int result = userService.resetPassword(userId, defaultPassword);

            if (result == VarList.OK) {
                return ResponseEntity.ok()
                        .body(new ResponseDTO(VarList.OK, "Password reset to default successfully", null));
            } else if (result == VarList.Not_Found) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "User not found", null));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseDTO(VarList.Internal_Server_Error, "Error resetting password", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
        try {
            log.info("Request received for image: {}", filename);

            if (filename.contains("..")) {
                log.warn("Path traversal attempt detected: {}", filename);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Path uploadPath = Paths.get(uploadDirectory).normalize();
            Path filePath = uploadPath.resolve(filename).normalize();

            log.info("Looking for image at: {}", filePath.toString());

            if (!filePath.startsWith(uploadPath)) {
                log.warn("Path traversal attempt detected: {}", filename);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                log.warn("Image not found: {}", filename);
                Path defaultPath = uploadPath.resolve("default-user.png");
                Resource defaultResource = new UrlResource(defaultPath.toUri());

                if (defaultResource.exists()) {
                    return ResponseEntity.ok()
                            .contentType(MediaType.IMAGE_PNG)
                            .body(defaultResource);
                }
                return ResponseEntity.notFound().build();
            }

            String contentType = determineImageContentType(filename);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (Exception e) {
            log.error("Error serving image: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    private String determineImageContentType(String filename) {
        String lowerFilename = filename.toLowerCase();
        if (lowerFilename.endsWith(".png")) {
            return "image/png";
        } else if (lowerFilename.endsWith(".jpg") || lowerFilename.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (lowerFilename.endsWith(".gif")) {
            return "image/gif";
        } else if (lowerFilename.endsWith(".svg")) {
            return "image/svg+xml";
        } else if (lowerFilename.endsWith(".webp")) {
            return "image/webp";
        } else if (lowerFilename.endsWith(".bmp")) {
            return "image/bmp";
        } else if (lowerFilename.endsWith(".ico")) {
            return "image/x-icon";
        }
        return MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }
}