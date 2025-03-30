package lk.ijse.online_course_management.service;


import lk.ijse.online_course_management.dto.CourseDTO;

import java.util.List;
import java.util.UUID;

public interface CourseService {
    
    int saveCourse(CourseDTO courseDTO);

    List<CourseDTO> getAllCourses();

    CourseDTO getCourseById(UUID courseId);

    int updateCourse(CourseDTO courseDTO);

    int deleteCourse(UUID courseId);

    CourseDTO resetCourse(UUID courseId);

    List<CourseDTO> getCoursesByInstructor(UUID instructorId);
}
