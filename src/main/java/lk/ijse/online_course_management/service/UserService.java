package lk.ijse.online_course_management.service;

import lk.ijse.online_course_management.dto.UserDTO;

import java.util.List;
import java.util.UUID;


public interface UserService {
    int saveUser(UserDTO userDTO);

    List<UserDTO> getAllUsers();

    int updateUser(UserDTO userDTO);

    int deleteUser(UUID userId);

    int resetPassword(UUID userId, String newPassword);

    UserDTO getUserById(UUID userId);

    List<UserDTO> getUsersByRole(String role);

    int updateUserProfileImage(String email, String newFilename);

    int deleteUserByEmail(String email);
}
