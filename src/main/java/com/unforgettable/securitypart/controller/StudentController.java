package com.unforgettable.securitypart.controller;

import com.unforgettable.securitypart.dto.CourseDTO;
import com.unforgettable.securitypart.dto.LaboratoryWorkDTO;
import com.unforgettable.securitypart.entity.LaboratoryWork;
import com.unforgettable.securitypart.entity.Student;
import com.unforgettable.securitypart.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/courses")
    public List<CourseDTO> getStudentCourses(HttpServletRequest request){
        return studentService.getStudentCourses(request);
    }

    @GetMapping("/courses/{courseId}/labs")
    public List<LaboratoryWorkDTO> getStudentCourseLaboratoryWork(HttpServletRequest request,
                                                                  @PathVariable Long courseId){
        return studentService.getStudentLaboratoryWorks(request, courseId);
    }

    @GetMapping("/courses/{courseId}/labs/{lwId}")
    public LaboratoryWorkDTO getLaboratoryWorkByCourse(HttpServletRequest request,
                                                       @PathVariable Long courseId,
                                                       @PathVariable Long lwId){
        return studentService.getLaboratoryWorkByCourse(request, courseId, lwId);
    }

    @GetMapping("/courses/{courseId}/passed-lw-info")
    public Map<String, Object> passedLWStats(HttpServletRequest request,
                                              @PathVariable Long courseId){
        return studentService.passedLWStats(request, courseId);
    }

    @PostMapping("/profile/edit")
    public void createProfile(HttpServletRequest request,
                              @RequestBody Student student){
        studentService.createProfile(request, student);
    }

    @PostMapping("/course/{courseId}/join")
    public void joinCourse(HttpServletRequest request,
                           @PathVariable Long courseId){
        studentService.joinCourse(request, courseId);
    }

    @PostMapping("/course/{courseId}/task/{taskId}")
    public void addLaboratoryWork(HttpServletRequest request,
                                  @RequestBody LaboratoryWork laboratoryWork,
                                  @PathVariable Long courseId,
                                  @PathVariable Long taskId){
        studentService.addLaboratoryWork(request, laboratoryWork, courseId, taskId);
    }
}
