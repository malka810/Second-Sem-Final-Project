package lk.ijse.online_course_management.service.impl;

import jakarta.persistence.Cacheable;
import jakarta.persistence.EntityNotFoundException;
import lk.ijse.online_course_management.controller.EnrollmentController;
import lk.ijse.online_course_management.controller.MaterialController;
import lk.ijse.online_course_management.dto.EnrollmentDTO;
import lk.ijse.online_course_management.entity.Course;
import lk.ijse.online_course_management.entity.Enrollment;
import lk.ijse.online_course_management.entity.User;
import lk.ijse.online_course_management.repo.CourseRepo;
import lk.ijse.online_course_management.repo.EnrollmentRepo;
import lk.ijse.online_course_management.repo.UserRepo;
import lk.ijse.online_course_management.service.EnrollmentService;
import lk.ijse.online_course_management.util.JwtUtil;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.service.spi.ServiceException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepo enrollmentRepo;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger log = LoggerFactory.getLogger(EnrollmentServiceImpl.class);

    @Override
    public String saveEnroll(EnrollmentDTO enrollmentDTO) {
        try {
            User student = userRepo.findById(enrollmentDTO.getStudentId())
                    .orElseThrow(() -> new EnrollmentController.ResourceNotFoundException("Student not found"));

            if (!"STUDENT".equals(student.getRole())) {
                throw new EnrollmentController.EnrollmentException("Only students can enroll in courses");
            }
            Course course = courseRepo.findById(enrollmentDTO.getCourseId())
                    .orElseThrow(() -> new EnrollmentController.ResourceNotFoundException("Course not found"));
            if (enrollmentRepo.existsByStudentAndCourse(student, course)) {
                return "ALREADY_ENROLLED";
            }

            Enrollment enrollment = new Enrollment();
            enrollment.setStudent(student);
            enrollment.setCourse(course);
            enrollment.setStudentName(student.getFullName());
            enrollment.setCourseTitle(course.getTitle());
            enrollment.setEnrollDate(new Date());

            enrollmentRepo.save(enrollment);
            return "SUCCESS";

        } catch (EnrollmentController.ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new EnrollmentController.EnrollmentException("Enrollment failed: " + e.getMessage());
        }
    }

    @Override
    public List<EnrollmentDTO> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentRepo.findAll();
        return enrollments.stream()
                .map(enrollment -> modelMapper.map(enrollment, EnrollmentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentDTO> getEnrollmentsByStudent(UUID studentId) {
        User student = userRepo.findById(studentId)
                .orElseThrow(() -> new EnrollmentController.ResourceNotFoundException("Student not found"));

        List<Enrollment> enrollments = enrollmentRepo.findByStudent(student);
        return enrollments.stream()
                .map(enrollment -> modelMapper.map(enrollment, EnrollmentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UUID getStudentIdByEmail(String email) {
        User student = userRepo.findByEmail(email);

        if (student == null) {
            throw new EnrollmentController.ResourceNotFoundException("Student not found with email: " + email);
        }

        if (!"STUDENT".equals(student.getRole())) {
            throw new EnrollmentController.EnrollmentException("Only students can enroll in courses");
        }

        return student.getUserId();
    }



    @Override
    public List<EnrollmentDTO> getEnrollmentsWithProgress(UUID studentId) {
        User student = userRepo.findById(studentId)
                .orElseThrow(() -> new EnrollmentController.ResourceNotFoundException("Student not found"));

        List<Enrollment> enrollments = enrollmentRepo.findByStudent(student);

        return enrollments.stream().map(enrollment -> {
            EnrollmentDTO dto = new EnrollmentDTO();

            // Basic fields
            dto.setEnrollmentId(enrollment.getEnrollmentId());
            dto.setEnrollDate(enrollment.getEnrollDate());
            dto.setProgress(enrollment.getProgress());
            dto.setLastAccessed(enrollment.getLastAccessed());
            if (enrollment.getCourse() != null) {
                dto.setCourseId(enrollment.getCourse().getCourseId());
                dto.setCourseTitle(enrollment.getCourse().getTitle());

                if (enrollment.getCourse().getInstructor() != null) {
                    dto.setInstructorName(enrollment.getCourse().getInstructor().getFullName());
                }
            }
            if (enrollment.getStudent() != null) {
                dto.setStudentId(enrollment.getStudent().getUserId());
                dto.setStudentName(enrollment.getStudent().getFullName());
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean verifyEnrollmentOwnership(UUID enrollmentId, UUID studentId) {
        try {
            Optional<Enrollment> enrollment = enrollmentRepo.findById(enrollmentId);
            return enrollment.isPresent() && enrollment.get().getStudent().getUserId().equals(studentId);
        } catch (Exception e) {
            log.error("Error verifying enrollment ownership", e);
            return false;
        }
    }

    @Override
    public EnrollmentDTO updateCourseProgress(EnrollmentDTO enrollmentDTO) {
        try {
            Enrollment enrollment = enrollmentRepo.findById(enrollmentDTO.getEnrollmentId())
                    .orElseThrow(() -> new EnrollmentController.ResourceNotFoundException("Enrollment not found"));

            if (enrollmentDTO.getProgress() < 0 || enrollmentDTO.getProgress() > 100) {
                throw new EnrollmentController.EnrollmentException("Progress must be between 0 and 100");
            }

            enrollment.setProgress(enrollmentDTO.getProgress());
            enrollment.setLastAccessed(new Date());

            Enrollment updatedEnrollment = enrollmentRepo.save(enrollment);
            EnrollmentDTO updatedDTO = modelMapper.map(updatedEnrollment, EnrollmentDTO.class);
            updatedDTO.setCourseTitle(updatedEnrollment.getCourse().getTitle());
            updatedDTO.setInstructorName(updatedEnrollment.getCourse().getInstructor().getFullName());

            return updatedDTO;
        } catch (EnrollmentController.ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating course progress", e);
            throw new EnrollmentController.EnrollmentException("Error updating progress");
        }

    }

    @Override
    public List<EnrollmentDTO> getEnrollmentsByCourse(UUID courseId) {
        try {
            Course course = courseRepo.findById(courseId)
                    .orElseThrow(() -> new EnrollmentController.ResourceNotFoundException("Course not found"));

            List<Enrollment> enrollments = enrollmentRepo.findByCourse(course);
            return enrollments.stream()
                    .map(enrollment -> modelMapper.map(enrollment, EnrollmentDTO.class))
                    .collect(Collectors.toList());
        } catch (EnrollmentController.ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error fetching course enrollments", e);
            throw new EnrollmentController.EnrollmentException("Error fetching course enrollments");
        }
    }


    @Override
    public void deleteEnrollment(UUID enrollmentId) {
        if (!enrollmentRepo.existsById(enrollmentId)) {
            throw new EnrollmentController.ResourceNotFoundException("Enrollment not found");
        }
        enrollmentRepo.deleteById(enrollmentId);
    }

    @Override
    public boolean isUserEnrolled(UUID studentId, UUID courseId) {
        try {
            User user = userRepo.findById(studentId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            if (!"STUDENT".equals(user.getRole())) {
                throw new ServiceException("Only students can be enrolled in courses");
            }

            Course course = courseRepo.findById(courseId)
                    .orElseThrow(() -> new EnrollmentController.ResourceNotFoundException("Course not found"));

            return enrollmentRepo.existsByStudentAndCourse(user, course);
        } catch (Exception e) {
            log.error("Error checking enrollment for student {} in course {}", studentId, courseId, e);
            throw new ServiceException("Error verifying enrollment", e);
        }
    }

    @Override
    public EnrollmentDTO createEnrollment(EnrollmentDTO enrollmentDTO) {
        try {
            User student = userRepo.findById(enrollmentDTO.getStudentId())
                    .orElseThrow(() -> new EnrollmentController.ResourceNotFoundException("Student not found"));

            if (!"STUDENT".equals(student.getRole())) {
                throw new EnrollmentController.EnrollmentException("Only students can enroll in courses");
            }

            Course course = courseRepo.findById(enrollmentDTO.getCourseId())
                    .orElseThrow(() -> new EnrollmentController.ResourceNotFoundException("Course not found"));

            if (enrollmentRepo.existsByStudentAndCourse(student, course)) {
                throw new EnrollmentController.EnrollmentException("Student is already enrolled in this course");
            }

            Enrollment enrollment = new Enrollment();
            enrollment.setStudent(student);
            enrollment.setCourse(course);
            enrollment.setStudentName(student.getFullName());
            enrollment.setCourseTitle(course.getTitle());
            enrollment.setEnrollDate(new Date());
            enrollment.setProgress(0); // Initialize progress to 0

            Enrollment savedEnrollment = enrollmentRepo.save(enrollment);
            return modelMapper.map(savedEnrollment, EnrollmentDTO.class);

        } catch (EnrollmentController.ResourceNotFoundException | EnrollmentController.EnrollmentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error creating enrollment", e);
            throw new EnrollmentController.EnrollmentException("Failed to create enrollment: " + e.getMessage());
        }
    }
    @Override
    public boolean isUserEnrolled(String token, UUID courseId) {
        try {
            String email = jwtUtil.getUsernameFromToken(token);
            return enrollmentRepo.existsByStudentEmailAndCourseId(email, courseId);
        } catch (Exception e) {
            log.error("Error checking enrollment by token", e);
            throw new ServiceException("Error verifying enrollment by token", e);
        }
    }
}