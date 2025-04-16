package lk.ijse.online_course_management.service.impl;

import lk.ijse.online_course_management.dto.UserDTO;
import lk.ijse.online_course_management.entity.User;
import lk.ijse.online_course_management.repo.UserRepo;
import lk.ijse.online_course_management.service.UserService;
import lk.ijse.online_course_management.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserServiceImpl(UserRepo userRepo, ModelMapper modelMapper) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public int saveUser(UserDTO userDTO) {
        if (userRepo.existsByEmail(userDTO.getEmail())) {
            return VarList.Not_Acceptable;
        }

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        if (userDTO.getRole() == null || userDTO.getRole().isEmpty()) {
            userDTO.setRole("STUDENT");
        } else if (!userDTO.getRole().equalsIgnoreCase("ADMIN")
                && !userDTO.getRole().equalsIgnoreCase("INSTRUCTOR")
                && !userDTO.getRole().equalsIgnoreCase("STUDENT")) {
            return VarList.Not_Acceptable;
        }

        User user = modelMapper.map(userDTO, User.class);
        userRepo.save(user);
        return VarList.Created;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepo.findAll();
        return users.stream()
                .map(user -> {
                    UserDTO dto = modelMapper.map(user, UserDTO.class);
                    dto.setPassword(null);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public int updateUser(UserDTO userDTO) {
        Optional<User> optionalUser = userRepo.findById((userDTO.getUserId()));
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            existingUser.setFullName(userDTO.getFullName());
            existingUser.setEmail(userDTO.getEmail());
            if (isValidRole(userDTO.getRole())) {
                existingUser.setRole(userDTO.getRole().toUpperCase());
            }
            if (userDTO.getProfileImage() != null) {
                existingUser.setProfileImage(userDTO.getProfileImage());
            }

            userRepo.save(existingUser);
            return VarList.OK;
        }
        return VarList.Not_Found;
    }

    @Override
    public int deleteUser(UUID userId) {
        if (userRepo.existsById(userId)) {
            userRepo.deleteById(userId);
            return VarList.OK;
        }
        return VarList.Not_Found;
    }

    @Override
    public int resetPassword(UUID userId, String newPassword) {
        Optional<User> optionalUser = userRepo.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Encode the default password
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);

            userRepo.save(user);
            return VarList.OK;
        } else {
            return VarList.Not_Found;
        }
    }

    private boolean isValidRole(String role) {
        if (role == null) return false;
        String upperRole = role.toUpperCase();
        return upperRole.equals("ADMIN") ||
                upperRole.equals("INSTRUCTOR") ||
                upperRole.equals("STUDENT");

    }


    @Override
    public UserDTO getUserById(UUID userId) {
        return userRepo.findById(userId)
                .map(user -> {
                    UserDTO dto = modelMapper.map(user, UserDTO.class);
                    dto.setPassword(null);
                    return dto;
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
    }

    @Override
    public List<UserDTO> getUsersByRole(String role) {
        if (!isValidRole(role)) {
            throw new IllegalArgumentException("Invalid role specified: " + role);
        }

        return userRepo.findByRole().stream()
                .map(user -> {
                    UserDTO dto = modelMapper.map(user, UserDTO.class);
                    dto.setPassword(null);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public int updateUserProfileImage(String email, String newFilename) {
        User user = userRepo.findByEmail(email);
        if (user != null) {
            user.setProfileImage(newFilename);
            userRepo.save(user);
            return VarList.OK;
        }
        return VarList.Not_Found;

    }

    @Override
    public int deleteUserByEmail(String email) {
        User user = userRepo.findByEmail(email);
        if (user != null) {
            userRepo.delete(user);
            return VarList.OK;
        }
        return VarList.Not_Found;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthorities(user));
    }

    private Set<SimpleGrantedAuthority> getAuthorities(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()));
        return authorities;
    }

    public UserDTO loadUserDetailsByUsername(String email) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    public User getUserEntityByEmail(String email) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return user;
    }
    }
