package com.unforgettable.securitypart.controller;

import com.unforgettable.securitypart.dto.CourseDTO;
import com.unforgettable.securitypart.dto.LaboratoryWorkDTO;
import com.unforgettable.securitypart.dto.StudentDTO;
import com.unforgettable.securitypart.entity.Course;
import com.unforgettable.securitypart.entity.Educator;
import com.unforgettable.securitypart.entity.Task;
import com.unforgettable.securitypart.service.EducatorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/educator")
public class EducatorController {
    private final EducatorService educatorService;

    @Autowired
    public EducatorController(EducatorService educatorService) {
        this.educatorService = educatorService;
    }

    @GetMapping("/courses")
    public List<CourseDTO> getEducatorCourses(HttpServletRequest request) {
        return educatorService.getEducatorCourses(request);
    }

    @GetMapping("/courses/{courseId}")
    public CourseDTO getEducatorCourse(HttpServletRequest request,
                                       @PathVariable Long courseId) {
        return educatorService.getEducatorCourse(request, courseId);
    }

    @GetMapping("/courses/{courseId}/students")
    public List<StudentDTO> getListOfStudentsByCourse(HttpServletRequest request,
                                                      @PathVariable Long courseId) {
        return educatorService.getListOfStudentsByCourse(request, courseId);
    }

    @GetMapping("/courses/{courseId}/students/{studentId}")
    public StudentDTO getStudentProfileByCourse(HttpServletRequest request,
                                                @PathVariable Long courseId,
                                                @PathVariable Long studentId) {
        return educatorService.getStudentProfile(request, courseId, studentId);
    }

    @GetMapping("/courses/{courseId}/task/{taskId}")
    public List<StudentDTO> getStudentsWhoPassedTask(HttpServletRequest request,
                                                     @PathVariable Long courseId,
                                                     @PathVariable Long taskId) {
        return educatorService.getStudentsWhoPassedTask(request, courseId, taskId);
    }

    @GetMapping("/courses/{courseId}/student/{studentId}/lab/{labId}")
    public LaboratoryWorkDTO getStudentLaboratoryWork(HttpServletRequest request,
                                                      @PathVariable Long courseId,
                                                      @PathVariable Long studentId,
                                                      @PathVariable Long labId){
        return educatorService.getLaboratoryWorkByCourseAndStudent(request, courseId, studentId, labId);
    }
    @GetMapping("/courses/{courseId}/stats")
    public Map<String, Object> getCourseStats(HttpServletRequest request,
                                              @PathVariable Long courseId) {
        return educatorService.courseStats(request, courseId);
    }

    @GetMapping("/courses/{courseId}/unchecked-lw")
    public List<StudentDTO> getStudentsWithUncheckedLabs(HttpServletRequest request,
                                                         @PathVariable Long courseId) {
        return educatorService.getStudentsWithUncheckedLabs(request, courseId);
    }
    @GetMapping("/courses/{courseId}/student/{studentId}/lab/{labId}/commits")
    public List<Object> getStudentLabCommits(HttpServletRequest request,
                                             @PathVariable Long courseId,
                                             @PathVariable Long studentId,
                                             @PathVariable Long labId){
        return educatorService.getCommitList(request, courseId, studentId, labId);
    }

    @PostMapping("/course/create")
    public void createCourse(HttpServletRequest request,
                             @RequestBody Course course) {
        educatorService.createCourse(request, course);
    }

    @PostMapping("/course/{courseId}/task/create")
    public void createTask(HttpServletRequest request,
                           @PathVariable Long courseId,
                           @RequestBody Task task) {
        educatorService.addTask(request, courseId, task);
    }

    @PostMapping("/profile/edit")
    public void createProfile(HttpServletRequest request,
                              @RequestBody Educator educator) {
        educatorService.createProfile(request, educator);
    }
}
