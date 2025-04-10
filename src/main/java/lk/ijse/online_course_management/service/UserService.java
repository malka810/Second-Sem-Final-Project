package lk.ijse.online_course_management.service;

import lk.ijse.online_course_management.dto.UserDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


public interface UserService {
    int saveUser(UserDTO userDTO);

    List<UserDTO> getAllUsers();

    int updateUser(UserDTO userDTO);

    int deleteUser(UUID userId);

    int resetPassword(String email, String newPassword);

    UserDTO getUserById(UUID userId);

    List<UserDTO> getUsersByRole(String role);
}
