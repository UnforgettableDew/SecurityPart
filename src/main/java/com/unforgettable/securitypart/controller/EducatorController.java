package com.unforgettable.securitypart.controller;

import com.unforgettable.securitypart.dto.CourseDTO;
import com.unforgettable.securitypart.dto.LaboratoryWorkDTO;
import com.unforgettable.securitypart.dto.StudentDTO;
import com.unforgettable.securitypart.entity.Course;
import com.unforgettable.securitypart.entity.Educator;
import com.unforgettable.securitypart.entity.Task;
import com.unforgettable.securitypart.model.CommonResponse;
import com.unforgettable.securitypart.service.EducatorService;
import com.unforgettable.securitypart.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private final FileService fileService;

    @Autowired
    public EducatorController(EducatorService educatorService, FileService fileService) {
        this.educatorService = educatorService;
        this.fileService = fileService;
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

    @GetMapping("/courses/{courseId}/student/{studentId}/lab/{labId}/download")
    public ResponseEntity<Resource> downloadStudentLaboratoryWork(HttpServletRequest request,
                                                                  @PathVariable Long courseId,
                                                                  @PathVariable Long studentId,
                                                                  @PathVariable Long labId) throws MalformedURLException {
        Resource resource = educatorService.downloadLab(request, courseId, studentId, labId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
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
                                                      @RequestBody Educator educator) {
        return new ResponseEntity<>(educatorService.editProfile(request, educator), OK);
    }

    @GetMapping("/createdir")
    public void createDir() {
        fileService.createDirectories();
    }

    @PostMapping("/course/{courseId}/task/{taskId}/upload")
    public ResponseEntity<CommonResponse> uploadTask(HttpServletRequest request,
                                                     @PathVariable Long courseId,
                                                     @PathVariable Long taskId,
                                                     @RequestParam("file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(educatorService.uploadTask(request, courseId, taskId, file), OK);
    }


//    public ResponseEntity<CommonResponse> downloadLab(HttpServletRequest request,
//                                                      @PathVariable Long labId){
//        return new ResponseEntity<>();
//    }
}
