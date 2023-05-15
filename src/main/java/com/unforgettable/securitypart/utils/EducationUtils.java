package com.unforgettable.securitypart.utils;

import com.unforgettable.securitypart.exception.CourseDoesntBelongEducatorException;
import com.unforgettable.securitypart.exception.NoSuchStudentOnCourseException;
import com.unforgettable.securitypart.exception.TaskDoesntBelongCourseException;
import com.unforgettable.securitypart.repository.CourseRepository;
import com.unforgettable.securitypart.repository.EducatorRepository;
import com.unforgettable.securitypart.repository.StudentRepository;
import com.unforgettable.securitypart.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EducationUtils {
    private final JwtService jwtService;
    private final EducatorRepository educatorRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public EducationUtils(JwtService jwtService,
                          EducatorRepository educatorRepository,
                          StudentRepository studentRepository,
                          CourseRepository courseRepository) {
        this.jwtService = jwtService;
        this.educatorRepository = educatorRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public Long getEducatorId(HttpServletRequest request, Long courseId) {
        Long educatorId = jwtService.getEducatorId(request);

        Long educatorCourseId = educatorRepository.findEducatorIdByCourse(courseId);
        if (!educatorCourseId.equals(educatorId))
            throw new CourseDoesntBelongEducatorException("Course with id = " + courseId
                    + " does not belong educator with id = " + educatorId);
        return educatorId;
    }

    public Long getCourseId(Long courseId ,Long taskId){
        Long courseIdByTaskId = courseRepository.findCourseIdByTaskId(taskId);
        if(!courseId.equals(courseIdByTaskId))
            throw new TaskDoesntBelongCourseException("Task with id = " + taskId +
                    " does not belong course with id = " + courseId);
        return courseIdByTaskId;
    }

    public Long getStudentId(HttpServletRequest request, Long courseId) {
        Long studentId = jwtService.getStudentId(request);

        List<Long> studentCourseIdList = studentRepository.findStudentsIdByCourse(courseId);

        for (Long studentCourseId : studentCourseIdList) {
            if (studentCourseId.equals(studentId)) {
                return studentId;
            }
        }
        throw new NoSuchStudentOnCourseException("There is no student with id = " + studentId +
                " on course with id = " + courseId);
    }
}
