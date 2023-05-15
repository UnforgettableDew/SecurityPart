package com.unforgettable.securitypart.controller;

import com.unforgettable.securitypart.dto.CourseDTO;
import com.unforgettable.securitypart.dto.LaboratoryWorkDTO;
import com.unforgettable.securitypart.dto.StudentDTO;
import com.unforgettable.securitypart.entity.Course;
import com.unforgettable.securitypart.entity.Educator;
import com.unforgettable.securitypart.entity.Task;
import com.unforgettable.securitypart.model.CommonResponse;
import com.unforgettable.securitypart.service.EducatorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/educator")
@CrossOrigin(origins = "*",
        allowedHeaders = "*",
        exposedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
        maxAge = 3600)
public class EducatorController {
    private final EducatorService educatorService;

    @Autowired
    public EducatorController(EducatorService educatorService) {
        this.educatorService = educatorService;
    }

    @GetMapping("/profile")
    public ResponseEntity<Educator> getEducatorProfile(HttpServletRequest request) {
        return new ResponseEntity<>(educatorService.getEducatorProfile(request), OK);
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseDTO>> getEducatorCourses(HttpServletRequest request) {
        return new ResponseEntity<>(educatorService.getEducatorCourses(request), OK);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<CourseDTO> getEducatorCourse(HttpServletRequest request,
                                                       @PathVariable Long courseId) {
        return new ResponseEntity<>(educatorService.getEducatorCourse(request, courseId), OK);
    }

    @GetMapping("/courses/{courseId}/students")
    public ResponseEntity<List<StudentDTO>> getListOfStudentsByCourse(HttpServletRequest request,
                                                                      @PathVariable Long courseId) {
        return new ResponseEntity<>(educatorService.getListOfStudentsByCourse(request, courseId), OK);
    }

    @GetMapping("/courses/{courseId}/students/{studentId}")
    public ResponseEntity<StudentDTO> getStudentProfileByCourse(HttpServletRequest request,
                                                                @PathVariable Long courseId,
                                                                @PathVariable Long studentId) {
        return new ResponseEntity<>(educatorService.getStudentProfile(request, courseId, studentId), OK);
    }

    @GetMapping("/courses/{courseId}/task/{taskId}")
    public ResponseEntity<List<StudentDTO>> getStudentsWhoPassedTask(HttpServletRequest request,
                                                                     @PathVariable Long courseId,
                                                                     @PathVariable Long taskId) {
        return new ResponseEntity<>(educatorService.getStudentsWhoPassedTask(request, courseId, taskId), OK);
    }

    @GetMapping("/courses/{courseId}/student/{studentId}/lab/{labId}")
    public ResponseEntity<LaboratoryWorkDTO> getStudentLaboratoryWork(HttpServletRequest request,
                                                                      @PathVariable Long courseId,
                                                                      @PathVariable Long studentId,
                                                                      @PathVariable Long labId) {
        return new ResponseEntity<>(educatorService.
                getLaboratoryWorkByCourseAndStudent(request, courseId, studentId, labId), OK);
    }

    @GetMapping("/courses/{courseId}/stats")
    public ResponseEntity<Map<String, Object>> getCourseStats(HttpServletRequest request,
                                                              @PathVariable Long courseId) {
        return new ResponseEntity<>(educatorService.courseStats(request, courseId), OK);
    }

    @GetMapping("/courses/{courseId}/unchecked-lw")
    public ResponseEntity<List<StudentDTO>> getStudentsWithUncheckedLabs(HttpServletRequest request,
                                                                         @PathVariable Long courseId) {
        return new ResponseEntity<>(educatorService.getStudentsWithUncheckedLabs(request, courseId), OK);
    }

    @GetMapping("/courses/{courseId}/student/{studentId}/lab/{labId}/commits")
    public ResponseEntity<List<Object>> getStudentLabCommits(HttpServletRequest request,
                                                             @PathVariable Long courseId,
                                                             @PathVariable Long studentId,
                                                             @PathVariable Long labId) {
        return new ResponseEntity<>(educatorService.getCommitList(request, courseId, studentId, labId), OK);
    }

    @PostMapping("/course/create")
    public ResponseEntity<Course> createCourse(HttpServletRequest request,
                                               @RequestBody Course course) {

        return new ResponseEntity<>(educatorService.createCourse(request, course), CREATED);
    }

    @PostMapping("/course/{courseId}/task/create")
    public ResponseEntity<Task> createTask(HttpServletRequest request,
                                           @PathVariable Long courseId,
                                           @RequestBody Task task) {
        return new ResponseEntity<>(educatorService.addTask(request, courseId, task), CREATED);
    }

    @PostMapping("/profile/create")
    public ResponseEntity<Educator> createProfile(HttpServletRequest request,
                                                  @RequestBody Educator educator) {
        return new ResponseEntity<>(educatorService.createProfile(request, educator), CREATED);
    }

    @PutMapping("/profile/edit")
    public ResponseEntity<CommonResponse> editProfile(HttpServletRequest request,
                                                      @RequestBody Educator educator){
        return new ResponseEntity<>(educatorService.editProfile(request, educator), OK);
    }
}
