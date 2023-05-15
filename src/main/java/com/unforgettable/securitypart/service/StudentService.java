package com.unforgettable.securitypart.service;

import com.unforgettable.securitypart.dto.CourseDTO;
import com.unforgettable.securitypart.dto.LaboratoryWorkDTO;
import com.unforgettable.securitypart.entity.*;
import com.unforgettable.securitypart.exception.NoLaboratoryWorkException;
import com.unforgettable.securitypart.model.CommonResponse;
import com.unforgettable.securitypart.repository.CourseRepository;
import com.unforgettable.securitypart.repository.LaboratoryWorkRepository;
import com.unforgettable.securitypart.repository.StudentRepository;
import com.unforgettable.securitypart.repository.TaskRepository;
import com.unforgettable.securitypart.utils.EducationUtils;
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
    private final EducationUtils educationUtils;

    @Autowired
    public StudentService(StudentRepository studentRepository,
                          CourseRepository courseRepository,
                          LaboratoryWorkRepository laboratoryWorkRepository,
                          TaskRepository taskRepository,
                          JwtService jwtService,
                          EducationUtils educationUtils) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.laboratoryWorkRepository = laboratoryWorkRepository;
        this.taskRepository = taskRepository;
        this.jwtService = jwtService;
        this.educationUtils = educationUtils;
    }

//    private Long getStudentId(HttpServletRequest request, Long courseId) {
//        Long studentId = jwtService.getStudentId(request);
//
//        List<Long> studentCourseIdList = studentRepository.findStudentsIdByCourse(courseId);
//
//        for (Long studentCourseId : studentCourseIdList) {
//            if (studentCourseId.equals(studentId)) {
//                return studentId;
//            }
//        }
//        throw new NoSuchStudentOnCourseException("There is no student with id = " + studentId +
//                " on course with id = " + courseId);
//    }

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

    public CommonResponse editStudentProfile(HttpServletRequest request, Student updatedStudent){
        Long studentId = jwtService.getStudentId(request);
        Student student = studentRepository.findById(studentId).get();
        student.updateStudent(updatedStudent);
        studentRepository.save(student);
        return new CommonResponse(true);
    }
}
