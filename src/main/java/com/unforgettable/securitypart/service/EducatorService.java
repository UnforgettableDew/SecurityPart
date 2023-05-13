package com.unforgettable.securitypart.service;

import com.unforgettable.securitypart.dto.CourseDTO;
import com.unforgettable.securitypart.dto.LaboratoryWorkDTO;
import com.unforgettable.securitypart.dto.StudentDTO;
import com.unforgettable.securitypart.dto.TaskDTO;
import com.unforgettable.securitypart.entity.Course;
import com.unforgettable.securitypart.entity.Educator;
import com.unforgettable.securitypart.entity.Task;
import com.unforgettable.securitypart.entity.UserEntity;
import com.unforgettable.securitypart.feign.GithubFeign;
import com.unforgettable.securitypart.repository.*;
import com.unforgettable.securitypart.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EducatorService {
    private final CourseRepository courseRepository;
    private final EducatorRepository educatorRepository;
    private final StudentRepository studentRepository;
    private final LaboratoryWorkRepository laboratoryWorkRepository;
    private final TaskRepository taskRepository;
    private final JwtUtils jwtUtils;
    private final JwtService jwtService;
    private final GithubFeign githubFeign;

    @Autowired
    public EducatorService(CourseRepository courseRepository,
                           EducatorRepository educatorRepository,
                           StudentRepository studentRepository,
                           LaboratoryWorkRepository laboratoryWorkRepository,
                           TaskRepository taskRepository,
                           JwtUtils jwtUtils,
                           JwtService jwtService,
                           GithubFeign githubFeign) {
        this.courseRepository = courseRepository;
        this.educatorRepository = educatorRepository;
        this.studentRepository = studentRepository;
        this.laboratoryWorkRepository = laboratoryWorkRepository;
        this.taskRepository = taskRepository;
        this.jwtUtils = jwtUtils;
        this.jwtService = jwtService;
        this.githubFeign = githubFeign;
    }

    public List<CourseDTO> getEducatorCourses(HttpServletRequest request) {
        Long educatorId = jwtUtils.getEducatorId(request);

        return courseRepository.findCoursesByEducatorId(educatorId)
                .stream()
                .map(CourseDTO::new).toList();
    }

    public CourseDTO getEducatorCourse(HttpServletRequest request, Long courseId) {
        Long educatorId = jwtUtils.getEducatorId(request);

        Long educatorCourseId = educatorRepository.findEducatorIdByCourse(courseId);
        if(!educatorCourseId.equals(educatorId))
            return null;

        return new CourseDTO(courseRepository.findCourseByIdAndEducatorId(courseId, educatorId));
    }

    public List<StudentDTO> getListOfStudentsByCourse(HttpServletRequest request, Long courseId) {
        Long educatorId = jwtUtils.getEducatorId(request);

        Long educatorCourseId = educatorRepository.findEducatorIdByCourse(courseId);
        if(!educatorCourseId.equals(educatorId))
            return null;

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
        Long educatorId = jwtUtils.getEducatorId(request);

        Long educatorCourseId = educatorRepository.findEducatorIdByCourse(courseId);
        if(!educatorCourseId.equals(educatorId))
            return null;

        StudentDTO student = studentRepository.findStudentByIdAndCourseId(studentId, courseId, educatorId);

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
        Long educatorId = jwtUtils.getEducatorId(request);

        Long educatorCourseId = educatorRepository.findEducatorIdByCourse(courseId);
        if(!educatorCourseId.equals(educatorId))
            return null;

        List<StudentDTO> students = studentRepository.findStudentsWhoPassedTask(courseId, taskId);
        for(StudentDTO student: students){
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
                                                                 Long labId){
        Long educatorId = jwtUtils.getEducatorId(request);

        Long educatorCourseId = educatorRepository.findEducatorIdByCourse(courseId);
        if(!educatorCourseId.equals(educatorId))
            return null;

        List<Long> studentCourseIdList = studentRepository.findStudentIdByCourse(courseId);

        for (Long studentCourseId : studentCourseIdList) {
            if (studentCourseId.equals(studentId)) {
                LaboratoryWorkDTO laboratoryWork = laboratoryWorkRepository
                        .findLaboratoryWorksByStudentIdAndCourseIdAndLWId(
                                studentId,
                                courseId,
                                labId);
                laboratoryWork.setTask(taskRepository.findTaskByLaboratoryWorkId(labId));

                return laboratoryWork;
            }
        }
        return null;
    }

    public Map<String, Object> courseStats(HttpServletRequest request, Long courseId){
        Long educatorId = jwtUtils.getEducatorId(request);

        Long educatorCourseId = educatorRepository.findEducatorIdByCourse(courseId);
        if(!educatorCourseId.equals(educatorId))
            return null;

        Map<String, Object> stats = new HashMap<>();
        Integer studentsCount = studentRepository.countStudentsByCourseId(courseId);
        Integer tasksCount = taskRepository.countTaskByCourseId(courseId);
//        Integer lwCount = laboratoryWorkRepository.countByCourseId(courseId);

        List<TaskDTO> tasks = taskRepository.findTasksByCourseId(courseId);
        List<LaboratoryWorkDTO> laboratoryWorks = new ArrayList<>();

        for(TaskDTO task:tasks){
            LaboratoryWorkDTO laboratoryWork = new LaboratoryWorkDTO(laboratoryWorkRepository
                    .avgScoreForLWByCourseAndTask(courseId, task.getId()));
            laboratoryWork.setTask(task);
            laboratoryWork.setStudentsCount(studentRepository.countStudentsByTaskId(task.getId()));
            laboratoryWorks.add(laboratoryWork);
        }

//        stats.put("current_laboratory_works_count", lwCount);
        stats.put("total_laboratory_works_count", studentsCount*tasksCount);
        stats.put("laboratory_works", laboratoryWorks);
        return stats;
    }

    public List<StudentDTO> getStudentsWithUncheckedLabs(HttpServletRequest request,
                                                         Long courseId){
        Long educatorId = jwtUtils.getEducatorId(request);

        Long educatorCourseId = educatorRepository.findEducatorIdByCourse(courseId);
        if(!educatorCourseId.equals(educatorId))
            return null;

        List<StudentDTO> students = studentRepository.findStudentWithUncheckedLabs(courseId);

        for(StudentDTO student:students){
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
                                      Long labId){
        Long educatorId = jwtUtils.getEducatorId(request);

        Long educatorCourseId = educatorRepository.findEducatorIdByCourse(courseId);
        if(!educatorCourseId.equals(educatorId))
            return null;

        List<Long> studentCourseIdList = studentRepository.findStudentIdByCourse(courseId);

        for (Long studentCourseId : studentCourseIdList) {
            if (studentCourseId.equals(studentId)) {
                String githubReference = laboratoryWorkRepository.findGithubReferenceByLabId(labId);
                String[] parts = githubReference.split("/");
                String username = parts[parts.length - 2];
                String repo = parts[parts.length - 1];
                return githubFeign.getAllCommits(username, repo);
            }
        }
        return null;
    }
    public void createCourse(HttpServletRequest request, Course course){
        Long educatorId = jwtUtils.getEducatorId(request);
        Educator educator = educatorRepository.findById(educatorId).get();
        course.setEducator(educator);
        courseRepository.save(course);
    }

    public void addTask(HttpServletRequest request, Long courseId, Task task){
        Long educatorId = jwtUtils.getEducatorId(request);

        Long educatorCourseId = educatorRepository.findEducatorIdByCourse(courseId);
        if(!educatorCourseId.equals(educatorId))
            return;

        Course course = courseRepository.findById(courseId).get();
        task.setCourse(course);

        taskRepository.save(task);
    }

    public void createProfile(HttpServletRequest request, Educator educator){
        UserEntity user = jwtService.getUserByJwt(request);
        educator.setUser(user);

        educatorRepository.save(educator);
    }
}
