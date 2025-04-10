package lk.ijse.online_course_management.service.impl;

import lk.ijse.online_course_management.dto.EnrollmentDTO;
import lk.ijse.online_course_management.entity.Course;
import lk.ijse.online_course_management.entity.Enrollment;
import lk.ijse.online_course_management.entity.User;
import lk.ijse.online_course_management.repo.CourseRepo;
import lk.ijse.online_course_management.repo.EnrollmentRepo;
import lk.ijse.online_course_management.repo.UserRepo;
import lk.ijse.online_course_management.service.EnrollmentService;
import lk.ijse.online_course_management.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public int saveEnroll(EnrollmentDTO enrollmentDTO) {
        try {
            // Find user and verify they are a student
            User user = userRepo.findById((enrollmentDTO.getStudentId()))
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!"STUDENT".equals(user.getRole())) {
                return VarList.Not_Acceptable; // Or your custom code for invalid role
            }

            Course course = courseRepo.findById(enrollmentDTO.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            if (enrollmentRepo.existsByStudentAndCourse(user, course)) {
                return VarList.RSP_DUPLICATED;
            }

            Enrollment enrollment = modelMapper.map(enrollmentDTO, Enrollment.class);
            enrollment.setStudent(user);
            enrollment.setCourse(course);
            enrollment.setEnrollDate(new java.util.Date());

            enrollmentRepo.save(enrollment);
            return VarList.Success;

        } catch (Exception e) {
            e.printStackTrace();
            return VarList.RSP_ERROR;
        }
    }
    }

