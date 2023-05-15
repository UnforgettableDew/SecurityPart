package com.unforgettable.securitypart.controller;

import com.unforgettable.securitypart.dto.CourseDTO;
import com.unforgettable.securitypart.dto.LaboratoryWorkDTO;
import com.unforgettable.securitypart.entity.LaboratoryWork;
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

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/student")
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
    public ResponseEntity<List<LaboratoryWorkDTO>> getStudentCourseLaboratoryWork(HttpServletRequest request,
                                                                                  @PathVariable Long courseId) {
        return new ResponseEntity<>(studentService.getStudentLaboratoryWorks(request, courseId), OK);
    }

    @GetMapping("/courses/{courseId}/labs/{lwId}")
    public ResponseEntity<LaboratoryWorkDTO> getLaboratoryWorkByCourse(HttpServletRequest request,
                                                                       @PathVariable Long courseId,
                                                                       @PathVariable Long lwId) {
        return new ResponseEntity<>(studentService.getLaboratoryWorkByCourse(request, courseId, lwId), OK);
    }

    @GetMapping("/courses/{courseId}/passed-lw-info")
    public ResponseEntity<Map<String, Object>> passedLWStats(HttpServletRequest request,
                                                             @PathVariable Long courseId) {
        return new ResponseEntity<>(studentService.passedLabsStats(request, courseId), OK);
    }

    @PostMapping("/profile/create")
    public ResponseEntity<CommonResponse> createProfile(HttpServletRequest request,
                                                        @RequestBody Student student) {
        return new ResponseEntity<>(studentService.createProfile(request, student), OK);
    }

    @PostMapping("/course/{courseId}/join")
    public ResponseEntity<CommonResponse> joinCourse(HttpServletRequest request,
                                                     @PathVariable Long courseId) {
        return new ResponseEntity<>(studentService.joinCourse(request, courseId), OK);
    }

    @PostMapping("/course/{courseId}/task/{taskId}")
    public ResponseEntity<CommonResponse> addLaboratoryWork(HttpServletRequest request,
                                                            @RequestBody LaboratoryWork laboratoryWork,
                                                            @PathVariable Long courseId,
                                                            @PathVariable Long taskId) {
        return new ResponseEntity<>(studentService.addLaboratoryWork(request, laboratoryWork, courseId, taskId), OK);
    }

    @PutMapping("/profile/edit")
    public ResponseEntity<CommonResponse> editProfile(HttpServletRequest request,
                                                      @RequestBody Student student){
        return new ResponseEntity<>(studentService.editStudentProfile(request, student), OK);
    }

    @PostMapping("/course/{courseId}/task/{taskId}/lab/{labId}/upload")
    public ResponseEntity<CommonResponse> uploadLab(HttpServletRequest request,
                                                    @RequestParam("file") MultipartFile file,
                                                    @PathVariable Long labId,
                                                    @PathVariable Long courseId,
                                                    @PathVariable Long taskId) throws IOException {
        return new ResponseEntity<>(studentService.addLabFile(request, file, labId, courseId, taskId), OK);
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
