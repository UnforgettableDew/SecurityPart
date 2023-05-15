package com.unforgettable.securitypart.service;

import com.unforgettable.securitypart.dto.CourseDTO;
import com.unforgettable.securitypart.dto.LaboratoryWorkDTO;
import com.unforgettable.securitypart.entity.*;
import com.unforgettable.securitypart.exception.NoLaboratoryWorkException;
import com.unforgettable.securitypart.exception.NoSuchStudentOnCourseException;
import com.unforgettable.securitypart.model.CommonResponse;
import com.unforgettable.securitypart.repository.*;
import com.unforgettable.securitypart.utils.EducationUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final LaboratoryWorkRepository laboratoryWorkRepository;
    private final TaskRepository taskRepository;
    private final EducatorRepository educatorRepository;
    private final JwtService jwtService;
    private final EducationUtils educationUtils;

    @Autowired
    public StudentService(StudentRepository studentRepository,
                          CourseRepository courseRepository,
                          LaboratoryWorkRepository laboratoryWorkRepository,
                          TaskRepository taskRepository,
                          EducatorRepository educatorRepository,
                          JwtService jwtService,
                          EducationUtils educationUtils) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.laboratoryWorkRepository = laboratoryWorkRepository;
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


    public List<LaboratoryWorkDTO> getStudentLaboratoryWorks(HttpServletRequest request,
                                                             Long courseId) {
        Long studentId = educationUtils.getStudentId(request, courseId);

        List<LaboratoryWorkDTO> laboratoryWorks = laboratoryWorkRepository.
                findLaboratoryWorksByStudentIdAndCourseId(studentId, courseId);

        laboratoryWorks.forEach(laboratoryWorkDTO ->
                laboratoryWorkDTO.setTask(taskRepository.
                        findTaskByLaboratoryWorkId(laboratoryWorkDTO.getId())));
        return laboratoryWorks;
    }

    public LaboratoryWorkDTO getLaboratoryWorkByCourse(HttpServletRequest request,
                                                       Long courseId,
                                                       Long labId) {
        Long studentId = educationUtils.getStudentId(request, courseId);

        LaboratoryWorkDTO laboratoryWork = laboratoryWorkRepository
                .findLaboratoryWorksByStudentIdAndCourseIdAndLWId(
                        studentId,
                        courseId,
                        labId);

        if (laboratoryWork == null)
            throw new NoLaboratoryWorkException("No such laboratory work with id = " + labId
                    + " on course with id = " + courseId);

        laboratoryWork.setTask(taskRepository.findTaskByLaboratoryWorkId(labId));

        return laboratoryWork;
    }

    public Map<String, Object> passedLabsStats(HttpServletRequest request,
                                               Long courseId) {

        Map<String, Object> labStats = new HashMap<>();

        Long studentId = educationUtils.getStudentId(request, courseId);
        Integer tasksCount = taskRepository.countTaskByCourseId(courseId);

        labStats.put("tasks_count", tasksCount);
        labStats.put("laboratory_works", getStudentLaboratoryWorks(request, courseId));

        return labStats;
    }

    public CommonResponse addLaboratoryWork(HttpServletRequest request,
                                            LaboratoryWork laboratoryWork,
                                            Long courseId,
                                            Long taskId) {
        Long studentId = jwtService.getStudentId(request);
        Student student = studentRepository.findById(studentId).get();

        Task task = taskRepository.findById(taskId).get();

        laboratoryWork.setTask(task);
        laboratoryWork.setScore(null);
        laboratoryWork.setIsPassed(false);
        laboratoryWork.setStudent(student);

        laboratoryWorkRepository.save(laboratoryWork);
        return new CommonResponse(true);
    }

    public CommonResponse addLabFile(HttpServletRequest request,
                                     MultipartFile file,
                                     Long labId,
                                     Long courseId,
                                     Long taskId) throws IOException {

        Long studentId = educationUtils.getStudentId(request, courseId);
        Student student = studentRepository.findById(studentId).get();

        Task task = taskRepository.findById(taskId).get();

        LaboratoryWork laboratoryWork = laboratoryWorkRepository.findById(labId).get();

        String educatorName = educatorRepository.findEducatorNameByCourseId(courseId).replace(",","");

        String course = courseRepository.findCourseTitleById(courseId);

        String path = "D:\\Files\\" + educatorName +
                "\\" + course +
                "\\Students\\" + student.getFirstname() + student.getLastname() +
                "\\Labs\\";

        laboratoryWork.setTitle(path + file.getOriginalFilename());
        laboratoryWork.setTask(task);
        laboratoryWork.setScore(null);
        laboratoryWork.setIsPassed(false);
        laboratoryWork.setStudent(student);
        laboratoryWorkRepository.save(laboratoryWork);


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

    public CommonResponse createProfile(HttpServletRequest request, Student student) {
        UserEntity user = jwtService.getUserByJwt(request);
        student.setUser(user);
        studentRepository.save(student);
        return new CommonResponse(true);
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
