package com.unforgettable.securitypart.controller;

import com.unforgettable.securitypart.dto.CourseDTO;
import com.unforgettable.securitypart.dto.PassedTaskDTO;
import com.unforgettable.securitypart.entity.PassedTask;
import com.unforgettable.securitypart.entity.Student;
import com.unforgettable.securitypart.model.CommonResponse;
import com.unforgettable.securitypart.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "*",
        allowedHeaders = "*",
        exposedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
        maxAge = 3600)
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseDTO>> getStudentCourses(HttpServletRequest request) {
        return new ResponseEntity<>(studentService.getStudentCourses(request), OK);
    }

    @GetMapping("/courses/{courseId}/labs")
    public ResponseEntity<List<PassedTaskDTO>> getStudentCoursePassedTasks(HttpServletRequest request,
                                                                           @PathVariable Long courseId) {
        return new ResponseEntity<>(studentService.getStudentPassedTasks(request, courseId), OK);
    }

    @GetMapping("/courses/{courseId}/labs/{passedTaskId}")
    public ResponseEntity<PassedTaskDTO> getPassedTaskByCourse(HttpServletRequest request,
                                                               @PathVariable Long courseId,
                                                               @PathVariable Long passedTaskId) {
        return new ResponseEntity<>(studentService.getPassedTaskByCourse(request, courseId, passedTaskId), OK);
    }

    @GetMapping("/courses/{courseId}/passed-lw-info")
    public ResponseEntity<Map<String, Object>> passedLWStats(HttpServletRequest request,
                                                             @PathVariable Long courseId) {
        return new ResponseEntity<>(studentService.passedPassedTasksStats(request, courseId), OK);
    }

    @PostMapping("/profile/create")
    public ResponseEntity<Student> createProfile(HttpServletRequest request,
                                                        @RequestBody Student student) {
        return new ResponseEntity<>(studentService.createProfile(request, student), CREATED);
    }

    @PostMapping("/course/{courseId}/join")
    public ResponseEntity<CommonResponse> joinCourse(HttpServletRequest request,
                                                     @PathVariable Long courseId) {
        return new ResponseEntity<>(studentService.joinCourse(request, courseId), OK);
    }

    @PostMapping("/course/{courseId}/task/{taskId}")
    public ResponseEntity<CommonResponse> addPassedTask(HttpServletRequest request,
                                                        @RequestBody PassedTask passedTask,
                                                        @PathVariable Long courseId,
                                                        @PathVariable Long taskId) {
        return new ResponseEntity<>(studentService.addPassedTask(request, passedTask, courseId, taskId), OK);
    }

    @PutMapping("/profile/edit")
    public ResponseEntity<CommonResponse> editProfile(HttpServletRequest request,
                                                      @RequestBody Student student){
        return new ResponseEntity<>(studentService.editStudentProfile(request, student), OK);
    }

    @PostMapping("/course/{courseId}/task/{taskId}/lab/{passedTask}/upload")
    public ResponseEntity<CommonResponse> uploadPassedTask(HttpServletRequest request,
                                                           @RequestParam("file") MultipartFile file,
                                                           @PathVariable Long passedTask,
                                                           @PathVariable Long courseId,
                                                           @PathVariable Long taskId) throws IOException {
        return new ResponseEntity<>(studentService.addPassedTaskFile(request, file, passedTask, courseId, taskId), OK);
    }

    @GetMapping("/courses/{courseId}/task/{taskId}/download")
    public ResponseEntity<Resource> downloadTask(HttpServletRequest request,
                                                                  @PathVariable Long courseId,
                                                                  @PathVariable Long taskId) throws MalformedURLException {
        Resource resource = studentService.downloadTask(request, courseId, taskId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
