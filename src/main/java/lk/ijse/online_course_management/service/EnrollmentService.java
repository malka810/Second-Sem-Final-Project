package lk.ijse.online_course_management.service;


import lk.ijse.online_course_management.dto.EnrollmentDTO;

import java.util.List;
import java.util.UUID;

public interface EnrollmentService {

    int saveEnroll(EnrollmentDTO enrollmentDTO);

    List<EnrollmentDTO> getAllEnrollments();

    void deleteEnrollment(UUID enrollmentId);
}
