package lk.ijse.online_course_management.controller;

import lk.ijse.online_course_management.dto.AuthDTO;
import lk.ijse.online_course_management.dto.ResponseDTO;
import lk.ijse.online_course_management.dto.UserDTO;
import lk.ijse.online_course_management.entity.Authentication;
import lk.ijse.online_course_management.entity.User;
import lk.ijse.online_course_management.repo.AuthenticationRepo;
import lk.ijse.online_course_management.service.impl.UserServiceImpl;
import lk.ijse.online_course_management.util.JwtUtil;
import lk.ijse.online_course_management.util.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@CrossOrigin("*")
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userService;
    private final ResponseDTO responseDTO;
    private final AuthenticationRepo authenticationRepo;

    public AuthController(JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserServiceImpl userService, ResponseDTO responseDTO, AuthenticationRepo authenticationRepo) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.responseDTO = responseDTO;
        this.authenticationRepo = authenticationRepo;

    }

    @PostMapping("/authenticate")
    public ResponseEntity<ResponseDTO> authenticate(@RequestBody UserDTO userDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseDTO(VarList.Unauthorized, "Invalid Credentials", e.getMessage()));
        }

        UserDTO loadedUser = userService.loadUserDetailsByUsername(userDTO.getEmail());
        if (loadedUser == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDTO(VarList.Conflict, "Authorization Failure! Please Try Again", null));
        }
        String token = jwtUtil.generateToken(loadedUser);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Token generation failed", null));
        }
//        if (token == null || token.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body(new ResponseDTO(VarList.Conflict, "Authorization Failure! Please Try Again", null));
//        }
        Authentication authLog = new Authentication();
//        AuthDTO authDTO = new AuthDTO();
//        authDTO.setEmail(loadedUser.getEmail());
//        authDTO.setToken(token);
        authLog.setEmail(loadedUser.getEmail());
        authLog.setToken(token);
        authLog.setRole(loadedUser.getRole());
        authLog.setLoginTime(LocalDateTime.now());

        User userEntity = userService.getUserEntityByEmail(loadedUser.getEmail());
        if (userEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(VarList.Not_Found, "User entity not found", null));
        }
        authLog.setUser(userEntity);

        authenticationRepo.save(authLog);
        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail(loadedUser.getEmail());
        authDTO.setToken(token);
        authDTO.setRole(loadedUser.getRole());
        return ResponseEntity.ok()
                .body(new ResponseDTO(VarList.OK, "Login successful", authDTO));
    }
}

