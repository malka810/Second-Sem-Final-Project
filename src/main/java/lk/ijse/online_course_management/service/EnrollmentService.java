package lk.ijse.online_course_management.service;


import lk.ijse.online_course_management.dto.EnrollmentDTO;

import java.util.List;
import java.util.UUID;

public interface EnrollmentService {

    String saveEnroll(EnrollmentDTO enrollmentDTO);

    List<EnrollmentDTO> getAllEnrollments();

    void deleteEnrollment(UUID enrollmentId);

    List<EnrollmentDTO> getEnrollmentsByStudent(UUID studentId);

    UUID getStudentIdByEmail(String email);


    boolean isUserEnrolled(String token, UUID courseId);

    List<EnrollmentDTO> getEnrollmentsWithProgress(UUID studentId);

    boolean verifyEnrollmentOwnership(UUID enrollmentId, UUID studentId);

    EnrollmentDTO updateCourseProgress(EnrollmentDTO enrollmentDTO);

    List<EnrollmentDTO> getEnrollmentsByCourse(UUID courseId);

    boolean isUserEnrolled(UUID studentId, UUID courseId);

    EnrollmentDTO createEnrollment(EnrollmentDTO enrollmentDTO);
}
