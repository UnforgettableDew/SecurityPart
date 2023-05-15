package com.unforgettable.securitypart.service;

import com.unforgettable.securitypart.dto.CourseDTO;
import com.unforgettable.securitypart.dto.LaboratoryWorkDTO;
import com.unforgettable.securitypart.dto.StudentDTO;
import com.unforgettable.securitypart.dto.TaskDTO;
import com.unforgettable.securitypart.entity.Course;
import com.unforgettable.securitypart.entity.Educator;
import com.unforgettable.securitypart.entity.Task;
import com.unforgettable.securitypart.entity.UserEntity;
import com.unforgettable.securitypart.exception.NoLaboratoryWorkException;
import com.unforgettable.securitypart.exception.NoSuchStudentOnCourseException;
import com.unforgettable.securitypart.feign.GithubFeign;
import com.unforgettable.securitypart.model.CommonResponse;
import com.unforgettable.securitypart.repository.*;
import com.unforgettable.securitypart.utils.EducationUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EducatorService {
    private final CourseRepository courseRepository;
    private final EducatorRepository educatorRepository;
    private final StudentRepository studentRepository;
    private final LaboratoryWorkRepository laboratoryWorkRepository;
    private final TaskRepository taskRepository;
    private final JwtService jwtService;
    private final GithubFeign githubFeign;
    private final EducationUtils educationUtils;

    @Autowired
    public EducatorService(CourseRepository courseRepository,
                           EducatorRepository educatorRepository,
                           StudentRepository studentRepository,
                           LaboratoryWorkRepository laboratoryWorkRepository,
                           TaskRepository taskRepository,
                           JwtService jwtService,
                           GithubFeign githubFeign,
                           EducationUtils educationUtils) {
        this.courseRepository = courseRepository;
        this.educatorRepository = educatorRepository;
        this.studentRepository = studentRepository;
        this.laboratoryWorkRepository = laboratoryWorkRepository;
        this.taskRepository = taskRepository;
        this.jwtService = jwtService;
        this.githubFeign = githubFeign;
        this.educationUtils = educationUtils;
    }

//    private Long getEducatorId(HttpServletRequest request, Long courseId) {
//        Long educatorId = jwtService.getEducatorId(request);
//
//        Long educatorCourseId = educatorRepository.findEducatorIdByCourse(courseId);
//        if (!educatorCourseId.equals(educatorId))
//            throw new CourseDoesntBelongEducatorException("Course with id = " + courseId
//                    + " does not belong educator with id = " + educatorId);
//        return educatorId;
//    }
//
//    private Long getCourseId(Long courseId ,Long taskId){
//        Long courseIdByTaskId = courseRepository.findCourseIdByTaskId(taskId);
//        if(!courseId.equals(courseIdByTaskId))
//            throw new TaskDoesntBelongCourseException("Task with id = " + taskId +
//                    " does not belong course with id = " + courseId);
//        return courseIdByTaskId;
//    }

    public List<CourseDTO> getEducatorCourses(HttpServletRequest request) {
        Long educatorId = jwtService.getEducatorId(request);

        return courseRepository.findCoursesByEducatorId(educatorId)
                .stream()
                .map(CourseDTO::new).toList();
    }

    public CourseDTO getEducatorCourse(HttpServletRequest request, Long courseId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        return new CourseDTO(courseRepository.findCourseByIdAndEducatorId(courseId, educatorId));
    }

    public List<StudentDTO> getListOfStudentsByCourse(HttpServletRequest request, Long courseId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        List<StudentDTO> students = studentRepository.findStudentsByCourseIdAndEducatorId(courseId, educatorId);

        for (StudentDTO student : students) {
            List<LaboratoryWorkDTO> laboratoryWorks = laboratoryWorkRepository.
                    findLaboratoryWorksByStudentIdAndCourseId(student.getId(), courseId);

            laboratoryWorks.forEach(laboratoryWorkDTO ->
                    laboratoryWorkDTO.setTask(taskRepository.
                            findTaskByLaboratoryWorkId(laboratoryWorkDTO.getId())));

            student.setLaboratoryWorks(laboratoryWorks);
        }
        return students;
    }

    public StudentDTO getStudentProfile(HttpServletRequest request,
                                        Long courseId,
                                        Long studentId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        StudentDTO student = studentRepository.findStudentByIdAndCourseId(studentId, courseId, educatorId);

        if (student == null)
            throw new NoSuchStudentOnCourseException("There is no student with id = " + studentId +
                    " on course with id = " + courseId);

        List<LaboratoryWorkDTO> laboratoryWorks = laboratoryWorkRepository.
                findLaboratoryWorksByStudentIdAndCourseId(studentId, courseId);

        laboratoryWorks.forEach(laboratoryWorkDTO ->
                laboratoryWorkDTO.setTask(taskRepository.
                        findTaskByLaboratoryWorkId(laboratoryWorkDTO.getId())));
        student.setLaboratoryWorks(laboratoryWorks);
        return student;
    }

    public List<StudentDTO> getStudentsWhoPassedTask(HttpServletRequest request,
                                                     Long courseId,
                                                     Long taskId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        Long checkedCourseId = educationUtils.getCourseId(courseId, taskId);

        List<StudentDTO> students = studentRepository.findStudentsWhoPassedTask(courseId, taskId);
        for (StudentDTO student : students) {
            LaboratoryWorkDTO laboratoryWork = laboratoryWorkRepository
                    .findLaboratoryWorkByStudentIdCourseIdTaskId(student.getId(), courseId, taskId);

            laboratoryWork.setTask(taskRepository
                    .findTaskByLaboratoryWorkId(laboratoryWork.getId()));

            student.addLaboratoryWork(laboratoryWork);
        }
        return students;
    }

    public LaboratoryWorkDTO getLaboratoryWorkByCourseAndStudent(HttpServletRequest request,
                                                                 Long courseId,
                                                                 Long studentId,
                                                                 Long labId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        List<Long> studentCourseIdList = studentRepository.findStudentsIdByCourse(courseId);
//
        for (Long studentCourseId : studentCourseIdList) {
            if (studentCourseId.equals(studentId)) {
                LaboratoryWorkDTO laboratoryWork = laboratoryWorkRepository
                        .findLaboratoryWorksByStudentIdAndCourseIdAndLWId(
                                studentId,
                                courseId,
                                labId);
                if (laboratoryWork == null)
                    throw new NoLaboratoryWorkException("No such laboratory work with id = " + labId
                            + " on course with id = " + courseId + " and student id = " + studentId);
                laboratoryWork.setTask(taskRepository.findTaskByLaboratoryWorkId(labId));
                return laboratoryWork;
            }
        }
        throw new NoSuchStudentOnCourseException("There is no student with id = " + studentId +
                " on course with id = " + courseId);
    }

    public Map<String, Object> courseStats(HttpServletRequest request, Long courseId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        Map<String, Object> stats = new HashMap<>();
        Integer studentsCount = studentRepository.countStudentsByCourseId(courseId);
        Integer tasksCount = taskRepository.countTaskByCourseId(courseId);

        List<TaskDTO> tasks = taskRepository.findTasksByCourseId(courseId);
        List<LaboratoryWorkDTO> laboratoryWorks = new ArrayList<>();

        for (TaskDTO task : tasks) {
            LaboratoryWorkDTO laboratoryWork = new LaboratoryWorkDTO(laboratoryWorkRepository
                    .avgScoreForLWByCourseAndTask(courseId, task.getId()));
            laboratoryWork.setTask(task);
            laboratoryWork.setStudentsCount(studentRepository.countStudentsByTaskId(task.getId()));
            laboratoryWorks.add(laboratoryWork);
        }

        stats.put("total_laboratory_works_count", studentsCount * tasksCount);
        stats.put("laboratory_works", laboratoryWorks);
        return stats;
    }

    public List<StudentDTO> getStudentsWithUncheckedLabs(HttpServletRequest request,
                                                         Long courseId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        List<StudentDTO> students = studentRepository.findStudentWithUncheckedLabs(courseId);

        for (StudentDTO student : students) {
            List<LaboratoryWorkDTO> laboratoryWorks = laboratoryWorkRepository.
                    findUncheckedLWByStudentAndCourse(student.getId(), courseId);

            laboratoryWorks.forEach(laboratoryWorkDTO ->
                    laboratoryWorkDTO.setTask(taskRepository.
                            findTaskByLaboratoryWorkId(laboratoryWorkDTO.getId())));
            student.setLaboratoryWorks(laboratoryWorks);
        }

        return students;
    }

    public List<Object> getCommitList(HttpServletRequest request,
                                      Long courseId, Long studentId,
                                      Long labId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

//        List<Long> studentCourseIdList = studentRepository.findStudentsIdByCourse(courseId);
//
//        for (Long studentCourseId : studentCourseIdList) {
//            if (studentCourseId.equals(studentId)) {
        String githubReference = laboratoryWorkRepository.findGithubReferenceByLabId(labId, courseId);
        if (githubReference == null)
            throw new NoLaboratoryWorkException("No such laboratory work with id = " + labId
                    + " on course with id = " + courseId);
        String[] parts = githubReference.split("/");
        String username = parts[parts.length - 2];
        String repo = parts[parts.length - 1];
        return githubFeign.getAllCommits(username, repo);
//            }
//        }
//        return null;
    }

    public Course createCourse(HttpServletRequest request, Course course) {
        Long educatorId = jwtService.getEducatorId(request);
        Educator educator = educatorRepository.findById(educatorId).get();
        course.setEducator(educator);
        courseRepository.save(course);
        return course;
    }

    public Task addTask(HttpServletRequest request, Long courseId, Task task) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        Course course = courseRepository.findById(courseId).get();
        task.setCourse(course);

        taskRepository.save(task);
        return task;
    }

    public Educator createProfile(HttpServletRequest request, Educator educator) {
        UserEntity user = jwtService.getUserByJwt(request);
        educator.setUser(user);

        educatorRepository.save(educator);
        return educator;
    }

    public Educator getEducatorProfile(HttpServletRequest request) {
        Long educatorId = jwtService.getEducatorId(request);

        return educatorRepository.findById(educatorId)
                .orElseThrow();
    }

    public CommonResponse editProfile(HttpServletRequest request, Educator updatedEducator){
        Long educatorId = jwtService.getEducatorId(request);
        Educator educator = educatorRepository.findById(educatorId).get();
        educator.updateEducator(updatedEducator);
        educatorRepository.save(educator);

        return new CommonResponse(true);
    }
}
