package lk.ijse.online_course_management.controller;

import lk.ijse.online_course_management.dto.CourseDTO;
import lk.ijse.online_course_management.dto.ResponseDTO;
import lk.ijse.online_course_management.service.CourseService;
import lk.ijse.online_course_management.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/add")
    public ResponseEntity<ResponseDTO> createCourse(@RequestBody CourseDTO courseDTO) {
        try {
            int createdCourse = courseService.saveCourse(courseDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO(VarList.Success, "Course created successfully", createdCourse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.RSP_ERROR, "Error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<ResponseDTO> getCourseById(@PathVariable UUID courseId) {
        try {
            CourseDTO course = courseService.getCourseById(courseId);
            return ResponseEntity.ok(new ResponseDTO(VarList.Success, "Course retrieved successfully", course));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(VarList.Not_Found, "Course not found: " + e.getMessage(), null));
        }
    }

    @PutMapping("/update/{courseId}")
    public ResponseEntity<ResponseDTO> updateCourse(
            @PathVariable UUID courseId,
            @RequestBody CourseDTO courseDTO) {
        try {
            courseDTO.setCourseId(courseId);
            int updatedCourse = courseService.updateCourse(courseDTO);
            return ResponseEntity.ok(new ResponseDTO(VarList.Success, "Course updated successfully", updatedCourse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(VarList.Bad_Request, "Failed to update course: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<ResponseDTO> deleteCourse(@PathVariable UUID courseId) {
        try {
            courseService.deleteCourse(courseId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ResponseDTO(VarList.Success, "Course deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(VarList.Not_Found, "Course not found: " + e.getMessage(), null));
        }
    }

    @PatchMapping("/{courseId}/reset")
    public ResponseEntity<ResponseDTO> resetCourse(@PathVariable UUID courseId) {
        try {
            CourseDTO resetCourse = courseService.resetCourse(courseId);
            return ResponseEntity.ok(new ResponseDTO(VarList.Success, "Course reset successfully", resetCourse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(VarList.Not_Found, "Course not found: " + e.getMessage(), null));
        }
    }

    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<ResponseDTO> getCoursesByInstructor(@PathVariable UUID instructorId) {
        try {
            List<CourseDTO> courses = courseService.getCoursesByInstructor(instructorId);
            return ResponseEntity.ok(new ResponseDTO(VarList.Success, "Courses retrieved successfully", courses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.RSP_ERROR, "Error retrieving courses: " + e.getMessage(), null));
        }
    }
}