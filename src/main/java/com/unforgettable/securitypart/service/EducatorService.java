package com.unforgettable.securitypart.service;

import com.unforgettable.securitypart.dto.CourseDTO;
import com.unforgettable.securitypart.dto.PassedTaskDTO;
import com.unforgettable.securitypart.dto.StudentDTO;
import com.unforgettable.securitypart.dto.TaskDTO;
import com.unforgettable.securitypart.entity.Course;
import com.unforgettable.securitypart.entity.Educator;
import com.unforgettable.securitypart.entity.Task;
import com.unforgettable.securitypart.entity.UserEntity;
import com.unforgettable.securitypart.exception.NoPassedTaskException;
import com.unforgettable.securitypart.exception.NoSuchStudentOnCourseException;
import com.unforgettable.securitypart.feign.GithubFeign;
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
import java.util.*;

@Service
public class EducatorService {
    private final CourseRepository courseRepository;
    private final EducatorRepository educatorRepository;
    private final StudentRepository studentRepository;
    private final PassedTaskRepository passedTaskRepository;
    private final TaskRepository taskRepository;
    private final JwtService jwtService;
    private final GithubFeign githubFeign;
    private final EducationUtils educationUtils;

    @Autowired
    public EducatorService(CourseRepository courseRepository,
                           EducatorRepository educatorRepository,
                           StudentRepository studentRepository,
                           PassedTaskRepository passedTaskRepository,
                           TaskRepository taskRepository,
                           JwtService jwtService,
                           GithubFeign githubFeign,
                           EducationUtils educationUtils) {
        this.courseRepository = courseRepository;
        this.educatorRepository = educatorRepository;
        this.studentRepository = studentRepository;
        this.passedTaskRepository = passedTaskRepository;
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
            List<PassedTaskDTO> passedTasks = passedTaskRepository.
                    findPassedTasksByStudentIdAndCourseId(student.getId(), courseId);

            passedTasks.forEach(passedTaskDTO ->
                    passedTaskDTO.setTask(taskRepository.
                            findTaskByPassedTaskId(passedTaskDTO.getId())));

            student.setPassedTasks(passedTasks);
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

        List<PassedTaskDTO> passedTasks = passedTaskRepository.
                findPassedTasksByStudentIdAndCourseId(studentId, courseId);

        passedTasks.forEach(passedTaskDTO ->
                passedTaskDTO.setTask(taskRepository.
                        findTaskByPassedTaskId(passedTaskDTO.getId())));
        student.setPassedTasks(passedTasks);
        return student;
    }

    public List<StudentDTO> getStudentsWhoPassedTask(HttpServletRequest request,
                                                     Long courseId,
                                                     Long taskId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        Long checkedCourseId = educationUtils.getCourseId(courseId, taskId);

        List<StudentDTO> students = studentRepository.findStudentsWhoPassedTask(courseId, taskId);
        for (StudentDTO student : students) {
            PassedTaskDTO passedTask = passedTaskRepository
                    .findPassedTaskByStudentIdCourseIdTaskId(student.getId(), courseId, taskId);

            passedTask.setTask(taskRepository
                    .findTaskByPassedTaskId(passedTask.getId()));

            student.addPassedTask(passedTask);
        }
        return students;
    }

    public PassedTaskDTO getPassedTaskByCourseAndStudent(HttpServletRequest request,
                                                         Long courseId,
                                                         Long studentId,
                                                         Long passedTaskId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        List<Long> studentCourseIdList = studentRepository.findStudentsIdByCourse(courseId);
//
        for (Long studentCourseId : studentCourseIdList) {
            if (studentCourseId.equals(studentId)) {
                PassedTaskDTO passedTask = passedTaskRepository
                        .findPassedTasksByStudentIdAndCourseIdAndLWId(
                                studentId,
                                courseId,
                                passedTaskId);
                if (passedTask == null)
                    throw new NoPassedTaskException("No such passed task with id = " + passedTaskId
                            + " on course with id = " + courseId + " and student id = " + studentId);
                passedTask.setTask(taskRepository.findTaskByPassedTaskId(passedTaskId));
                return passedTask;
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
        List<PassedTaskDTO> passedTasks = new ArrayList<>();

        for (TaskDTO task : tasks) {
            PassedTaskDTO passedTask = new PassedTaskDTO(passedTaskRepository
                    .avgScoreForLWByCourseAndTask(courseId, task.getId()));
            passedTask.setTask(task);
            passedTask.setStudentsCount(studentRepository.countStudentsByTaskId(task.getId()));
            passedTasks.add(passedTask);
        }

        stats.put("total_tasks_count", studentsCount * tasksCount);
        stats.put("passed_tasks", passedTasks);
        return stats;
    }

    public List<StudentDTO> getStudentsWithUncheckedPassedTasks(HttpServletRequest request,
                                                                Long courseId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        List<StudentDTO> students = studentRepository.findStudentWithUncheckedLabs(courseId);

        for (StudentDTO student : students) {
            List<PassedTaskDTO> passedTasks = passedTaskRepository.
                    findUncheckedLWByStudentAndCourse(student.getId(), courseId);

            passedTasks.forEach(passedTaskDTO ->
                    passedTaskDTO.setTask(taskRepository.
                            findTaskByPassedTaskId(passedTaskDTO.getId())));
            student.setPassedTasks(passedTasks);
        }

        return students;
    }

    public List<Object> getCommitList(HttpServletRequest request,
                                      Long courseId, Long studentId,
                                      Long passedTaskId) {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

//        List<Long> studentCourseIdList = studentRepository.findStudentsIdByCourse(courseId);
//
//        for (Long studentCourseId : studentCourseIdList) {
//            if (studentCourseId.equals(studentId)) {
        String githubReference = passedTaskRepository.findGithubReferenceByPassedTaskId(passedTaskId, courseId);
        if (githubReference == null)
            throw new NoPassedTaskException("No such passed task with id = " + passedTaskId
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

    public CommonResponse editProfile(HttpServletRequest request, Educator updatedEducator) {
        Long educatorId = jwtService.getEducatorId(request);
        Educator educator = educatorRepository.findById(educatorId).get();
        educator.updateEducator(updatedEducator);
        educatorRepository.save(educator);

        return new CommonResponse(true);
    }

    public Resource downloadPassedTask(HttpServletRequest request,
                                       Long courseId,
                                       Long studentId,
                                       Long passedTaskId) throws MalformedURLException {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        List<Long> studentCourseIdList = studentRepository.findStudentsIdByCourse(courseId);
//
        for (Long studentCourseId : studentCourseIdList) {
            if (studentCourseId.equals(studentId)) {
                String passedTaskTitle = passedTaskRepository
                        .findPassedTaskTitleByStudentIdAndCourseIdAndPassedTaskId(
                                studentId,
                                courseId,
                                passedTaskId);
                if (passedTaskTitle.isEmpty())
                    throw new NoPassedTaskException("No such passed task with id = " + passedTaskId
                            + " on course with id = " + courseId + " and student id = " + studentId);
                Path path = Paths.get(passedTaskTitle);
                return new UrlResource(path.toUri());
            }
        }
        throw new NoSuchStudentOnCourseException("There is no student with id = " + studentId +
                " on course with id = " + courseId);
    }

    public CommonResponse uploadTask(HttpServletRequest request,
                                     Long courseId,
                                     Long taskId,
                                     MultipartFile file) throws IOException {
        Long educatorId = educationUtils.getEducatorId(request, courseId);

        Course course = courseRepository.findById(courseId).get();
        Task task = taskRepository.findById(taskId).get();
        String educatorName = educatorRepository.findEducatorNameByCourseId(courseId).replace(",", "");

        String path = "D:\\Files\\" + educatorName +
                "\\" + course.getTitle() +
                "\\Tasks\\";
        task.setReference(path + file.getOriginalFilename());
        Path filePath = Paths.get(path, file.getOriginalFilename());
        file.transferTo(filePath.toFile());
        taskRepository.save(task);
        return new CommonResponse(true);
    }
}
