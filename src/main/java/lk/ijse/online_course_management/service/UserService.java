package lk.ijse.online_course_management.service;

import lk.ijse.online_course_management.dto.UserDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface UserService {
    int saveUser(UserDTO userDTO);

    List<UserDTO> getAllUsers();

    int updateUser(UserDTO userDTO);

    int deleteUser(String userId);

    int resetPassword(String email, String newPassword);

    UserDTO getUserById(String userId);

    List<UserDTO> getUsersByRole(String role);
}
