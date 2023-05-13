package com.unforgettable.securitypart.service;

import com.unforgettable.securitypart.dto.CourseDTO;
import com.unforgettable.securitypart.dto.LaboratoryWorkDTO;
import com.unforgettable.securitypart.entity.*;
import com.unforgettable.securitypart.repository.CourseRepository;
import com.unforgettable.securitypart.repository.LaboratoryWorkRepository;
import com.unforgettable.securitypart.repository.StudentRepository;
import com.unforgettable.securitypart.repository.TaskRepository;
import com.unforgettable.securitypart.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final LaboratoryWorkRepository laboratoryWorkRepository;
    private final TaskRepository taskRepository;
    private final JwtService jwtService;
    private final JwtUtils jwtUtils;

    @Autowired
    public StudentService(StudentRepository studentRepository,
                          CourseRepository courseRepository,
                          LaboratoryWorkRepository laboratoryWorkRepository,
                          TaskRepository taskRepository, JwtService jwtService, JwtUtils jwtUtils) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.laboratoryWorkRepository = laboratoryWorkRepository;
        this.taskRepository = taskRepository;
        this.jwtService = jwtService;
        this.jwtUtils = jwtUtils;
    }

    public List<CourseDTO> getStudentCourses(HttpServletRequest request) {
        Long studentId = jwtUtils.getStudentId(request);

        return courseRepository.findCoursesByStudentId(studentId)
                .stream()
                .map(CourseDTO::new).toList();
    }

    public List<LaboratoryWorkDTO> getStudentLaboratoryWorks(HttpServletRequest request,
                                                             Long courseId) {
        Long studentId = jwtUtils.getStudentId(request);

        List<Long> studentCourseIdList = studentRepository.findStudentIdByCourse(courseId);
        for (Long studentCourseId : studentCourseIdList) {
            if (studentCourseId.equals(studentId)) {
                List<LaboratoryWorkDTO> laboratoryWorks = laboratoryWorkRepository.
                        findLaboratoryWorksByStudentIdAndCourseId(studentId, courseId);

                laboratoryWorks.forEach(laboratoryWorkDTO ->
                        laboratoryWorkDTO.setTask(taskRepository.
                                findTaskByLaboratoryWorkId(laboratoryWorkDTO.getId())));
                return laboratoryWorks;
            }
        }
        return null;
    }

    public LaboratoryWorkDTO getLaboratoryWorkByCourse(HttpServletRequest request,
                                                       Long courseId,
                                                       Long lwId){
        Long studentId = jwtUtils.getStudentId(request);
        List<Long> studentCourseIdList = studentRepository.findStudentIdByCourse(courseId);

        for (Long studentCourseId : studentCourseIdList) {
            if (studentCourseId.equals(studentId)) {
                LaboratoryWorkDTO laboratoryWork = laboratoryWorkRepository
                        .findLaboratoryWorksByStudentIdAndCourseIdAndLWId(
                                studentId,
                                courseId,
                                lwId);
                laboratoryWork.setTask(taskRepository.findTaskByLaboratoryWorkId(lwId));

                return laboratoryWork;
            }
        }
        return null;
    }

    public Map<String, Object> passedLWStats(HttpServletRequest request,
                                              Long courseId){
        Long studentId = jwtUtils.getStudentId(request);
        List<Long> studentCourseIdList = studentRepository.findStudentIdByCourse(courseId);
        Map<String, Object> lwStats = new HashMap<>();

        for (Long studentCourseId : studentCourseIdList) {
            if (studentCourseId.equals(studentId)) {
                Integer tasksCount = taskRepository.countTaskByCourseId(courseId);

                lwStats.put("tasks_count", tasksCount);
                lwStats.put("laboratory_works", getStudentLaboratoryWorks(request, courseId));

                return lwStats;
            }
        }
        return null;
    }

    public void addLaboratoryWork(HttpServletRequest request,
                                  LaboratoryWork laboratoryWork,
                                  Long courseId,
                                  Long taskId){
        Long studentId = jwtUtils.getStudentId(request);
        Student student = studentRepository.findById(studentId).get();

        Task task = taskRepository.findById(taskId).get();

        laboratoryWork.setTask(task);
        laboratoryWork.setScore(null);
        laboratoryWork.setIsPassed(false);
        laboratoryWork.setStudent(student);

        laboratoryWorkRepository.save(laboratoryWork);
    }

    public void createProfile(HttpServletRequest request, Student student){
        UserEntity user = jwtService.getUserByJwt(request);
        student.setUser(user);
        studentRepository.save(student);
    }

    public void joinCourse(HttpServletRequest request, Long courseId){
        Long studentId = jwtUtils.getStudentId(request);
        Student student = studentRepository.findById(studentId).get();
        Course course = courseRepository.findById(courseId).get();

        student.addCourse(course);

        studentRepository.save(student);
    }
}
