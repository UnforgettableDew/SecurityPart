package com.unforgettable.securitypart.service;

import com.unforgettable.securitypart.dto.CourseDTO;
import com.unforgettable.securitypart.dto.PassedTaskDTO;
import com.unforgettable.securitypart.entity.*;
import com.unforgettable.securitypart.exception.NoPassedTaskException;
import com.unforgettable.securitypart.model.CommonResponse;
import com.unforgettable.securitypart.repository.*;
import com.unforgettable.securitypart.utils.EducationUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final PassedTaskRepository passedTaskRepository;
    private final TaskRepository taskRepository;
    private final EducatorRepository educatorRepository;
    private final JwtService jwtService;
    private final EducationUtils educationUtils;

    @Autowired
    public StudentService(StudentRepository studentRepository,
                          CourseRepository courseRepository,
                          PassedTaskRepository passedTaskRepository,
                          TaskRepository taskRepository,
                          EducatorRepository educatorRepository,
                          JwtService jwtService,
                          EducationUtils educationUtils) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.passedTaskRepository = passedTaskRepository;
        this.taskRepository = taskRepository;
        this.educatorRepository = educatorRepository;
        this.jwtService = jwtService;
        this.educationUtils = educationUtils;
    }

    public List<CourseDTO> getStudentCourses(HttpServletRequest request) {
        Long studentId = jwtService.getStudentId(request);

        return courseRepository.findCoursesByStudentId(studentId)
                .stream()
                .map(CourseDTO::new).toList();
    }


    public List<PassedTaskDTO> getStudentPassedTasks(HttpServletRequest request,
                                                     Long courseId) {
        Long studentId = educationUtils.getStudentId(request, courseId);

        List<PassedTaskDTO> passedTasks = passedTaskRepository.
                findPassedTasksByStudentIdAndCourseId(studentId, courseId);

        passedTasks.forEach(passedTaskDTO ->
                passedTaskDTO.setTask(taskRepository.
                        findTaskByPassedTaskId(passedTaskDTO.getId())));
        return passedTasks;
    }

    public PassedTaskDTO getPassedTaskByCourse(HttpServletRequest request,
                                               Long courseId,
                                               Long passedTaskId) {
        Long studentId = educationUtils.getStudentId(request, courseId);

        PassedTaskDTO passedTask = passedTaskRepository
                .findPassedTasksByStudentIdAndCourseIdAndLWId(
                        studentId,
                        courseId,
                        passedTaskId);

        if (passedTask == null)
            throw new NoPassedTaskException("No such passed task with id = " + passedTaskId
                    + " on course with id = " + courseId);

        passedTask.setTask(taskRepository.findTaskByPassedTaskId(passedTaskId));

        return passedTask;
    }

    public Map<String, Object> passedPassedTasksStats(HttpServletRequest request,
                                                      Long courseId) {

        Map<String, Object> passedTaskStats = new HashMap<>();

        Long studentId = educationUtils.getStudentId(request, courseId);
        Integer tasksCount = taskRepository.countTaskByCourseId(courseId);

        passedTaskStats.put("tasks_count", tasksCount);
        passedTaskStats.put("passed_tasks", getStudentPassedTasks(request, courseId));

        return passedTaskStats;
    }

    public CommonResponse addPassedTask(HttpServletRequest request,
                                        PassedTask passedTask,
                                        Long courseId,
                                        Long taskId) {
        Long studentId = jwtService.getStudentId(request);
        Student student = studentRepository.findById(studentId).get();

        Task task = taskRepository.findById(taskId).get();

        passedTask.setTask(task);
        passedTask.setPoint(null);
        passedTask.setIsAssessed(false);
        passedTask.setStudent(student);

        passedTaskRepository.save(passedTask);
        return new CommonResponse(true);
    }

    public CommonResponse addPassedTaskFile(HttpServletRequest request,
                                            MultipartFile file,
                                            Long passedTaskId,
                                            Long courseId,
                                            Long taskId) throws IOException {

        Long studentId = educationUtils.getStudentId(request, courseId);
        Student student = studentRepository.findById(studentId).get();

        Task task = taskRepository.findById(taskId).get();

        PassedTask passedTask = passedTaskRepository.findById(passedTaskId).get();

        String educatorName = educatorRepository.findEducatorNameByCourseId(courseId).replace(",","");

        String course = courseRepository.findCourseTitleById(courseId);

        String path = "D:\\Files\\" + educatorName +
                "\\" + course +
                "\\Students\\" + student.getFirstname() + student.getLastname() +
                "\\Labs\\";

        passedTask.setReference(path + file.getOriginalFilename());
        passedTask.setTask(task);
        passedTask.setPoint(null);
        passedTask.setIsAssessed(false);
        passedTask.setStudent(student);
        passedTaskRepository.save(passedTask);


        Path filePath = Paths.get(path, file.getOriginalFilename());
        file.transferTo(filePath.toFile());
        return new CommonResponse(true);
    }

    public Resource downloadTask(HttpServletRequest request,
                                Long courseId,
                                Long taskId) throws MalformedURLException {
        Long studentId = educationUtils.getStudentId(request, courseId);
        Task task = taskRepository.findById(taskId).get();

        Path path = Paths.get(task.getReference());
        return new UrlResource(path.toUri());

    }

    public Student createProfile(HttpServletRequest request, Student student) {
        UserEntity user = jwtService.getUserByJwt(request);
        student.setUser(user);
        student.setRegistrationDate(Timestamp.valueOf(LocalDateTime.now()));
        studentRepository.save(student);
        return student;
    }

    public CommonResponse joinCourse(HttpServletRequest request, Long courseId) {
        Long studentId = jwtService.getStudentId(request);
        Student student = studentRepository.findById(studentId).get();
        Course course = courseRepository.findById(courseId).get();

        student.addCourse(course);

        studentRepository.save(student);
        return new CommonResponse(true);
    }

    public CommonResponse editStudentProfile(HttpServletRequest request, Student updatedStudent) {
        Long studentId = jwtService.getStudentId(request);
        Student student = studentRepository.findById(studentId).get();
        student.updateStudent(updatedStudent);
        studentRepository.save(student);
        return new CommonResponse(true);
    }
}
