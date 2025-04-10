package lk.ijse.online_course_management.service.impl;

import lk.ijse.online_course_management.dto.CourseDTO;
import lk.ijse.online_course_management.entity.Course;
import lk.ijse.online_course_management.entity.User;
import lk.ijse.online_course_management.repo.CourseRepo;
import lk.ijse.online_course_management.repo.UserRepo;
import lk.ijse.online_course_management.service.CourseService;
import lk.ijse.online_course_management.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepo courseRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public int saveCourse(CourseDTO courseDTO) {
        try {
            User instructor = userRepository.findById((courseDTO.getInstructorId()))
                    .orElseThrow(() -> new RuntimeException("Instructor not found"));

            if (!"INSTRUCTOR".equals(instructor.getRole())) {
                return VarList.Not_Acceptable;
            }
            if (courseRepository.existsByTitle(courseDTO.getTitle())) {
                return VarList.RSP_DUPLICATED;
            }

            Course course = modelMapper.map(courseDTO, Course.class);
            course.setInstructor(instructor);
            course.setInstructorName(instructor.getFullName());

            courseRepository.save(course);
            return VarList.Success;

        } catch (Exception e) {
            e.printStackTrace();
            return VarList.RSP_ERROR;
        }
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(course -> modelMapper.map(course, CourseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CourseDTO getCourseById(UUID courseId) {
        return courseRepository.findById(courseId)
                .map(course -> modelMapper.map(course, CourseDTO.class))
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    @Override
    public int updateCourse(CourseDTO courseDTO) {
        try {
            if (!courseRepository.existsById(courseDTO.getCourseId())) {
                return VarList.Not_Found;
            }

            User instructor = userRepository.findById((courseDTO.getInstructorId()))
                    .orElseThrow(() -> new RuntimeException("Instructor not found"));

            if (!"INSTRUCTOR".equals(instructor.getRole())) {
                return VarList.Not_Acceptable;
            }

            Course existingCourse = courseRepository.findById(courseDTO.getCourseId()).get();
            existingCourse.setTitle(courseDTO.getTitle());
            existingCourse.setDescription(courseDTO.getDescription());
            existingCourse.setInstructor(instructor);
            existingCourse.setInstructorName(instructor.getFullName());

            courseRepository.save(existingCourse);
            return VarList.Success;

        } catch (Exception e) {
            e.printStackTrace();
            return VarList.RSP_ERROR;
        }
    }

    @Override
    public int deleteCourse(UUID courseId) {
        try {
            if (!courseRepository.existsById(courseId)) {
                return VarList.Not_Found;
            }
            courseRepository.deleteById(courseId);
            return VarList.Success;
        } catch (Exception e) {
            e.printStackTrace();
            return VarList.RSP_ERROR;
        }
    }

    @Override
    public CourseDTO resetCourse(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setTitle("");
        course.setDescription("");
        course.setInstructorName("");

        Course resetCourse = courseRepository.save(course);
        return modelMapper.map(resetCourse, CourseDTO.class);
    }

    @Override
    public List<CourseDTO> getCoursesByInstructor(UUID instructorId) {
        return courseRepository.findByInstructorUserId(instructorId).stream()
                .map(course -> modelMapper.map(course, CourseDTO.class))
                .collect(Collectors.toList());
    }
}