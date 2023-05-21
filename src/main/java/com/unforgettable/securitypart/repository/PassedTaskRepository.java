package com.unforgettable.securitypart.repository;

import com.unforgettable.securitypart.dto.PassedTaskDTO;
import com.unforgettable.securitypart.entity.PassedTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassedTaskRepository extends JpaRepository<PassedTask, Long> {
    @Query("select new com.unforgettable.securitypart.dto.PassedTaskDTO(" +
            "lw.id, lw.point, lw.isAssessed) from PassedTask lw " +
            "where lw.student.id=:studentId and lw.task.course.id=:courseId")
    List<PassedTaskDTO> findPassedTasksByStudentIdAndCourseId(Long studentId, Long courseId);

    @Query("select new com.unforgettable.securitypart.dto.PassedTaskDTO(" +
            "lw.id, lw.reference, lw.githubReference, lw.point, lw.isAssessed, lw.comment) from PassedTask lw " +
            "where lw.student.id=:studentId and lw.task.course.id=:courseId and lw.id=:passedTaskId")
    PassedTaskDTO findPassedTasksByStudentIdAndCourseIdAndLWId(Long studentId,
                                                               Long courseId,
                                                               Long passedTaskId);
    @Query("select new com.unforgettable.securitypart.dto.PassedTaskDTO(" +
            "lw.id, lw.point, lw.isAssessed) from PassedTask lw " +
            "join lw.task t where lw.student.id=:studentId and t.id=:taskId and t.course.id=:courseId")
    PassedTaskDTO findPassedTaskByStudentIdCourseIdTaskId(Long studentId, Long courseId, Long taskId);

    @Query("select new com.unforgettable.securitypart.dto.PassedTaskDTO(" +
            "lw.id, lw.point, lw.isAssessed, lw.githubReference) from PassedTask lw " +
            "join lw.task t where lw.student.id=:studentId and t.course.id=:courseId " +
            "and lw.isAssessed=false")
    List<PassedTaskDTO> findUncheckedLWByStudentAndCourse(Long studentId, Long courseId);

    @Query("select count(lw) from PassedTask lw " +
            "where lw.task.course.id=:courseId and lw.student.id=:studentId")
    Integer countPassedLwByCourseAndStudent(Long courseId, Long studentId);

    @Query("select count(lw) from PassedTask lw " +
            "where lw.task.course.id=:courseId")
    Integer countByCourseId(Long courseId);

    @Query("select avg(lw.point) from PassedTask lw " +
            "where lw.task.course.id=:courseId and lw.task.id=:taskId")
    Float avgScoreForLWByCourseAndTask(Long courseId, Long taskId);

    @Query("select lw.githubReference from PassedTask lw " +
            "where lw.id=:labId and lw.task.course.id=:courseId")
    String findGithubReferenceByPassedTaskId(Long labId, Long courseId);

    @Query("select lw.reference from PassedTask lw " +
            "where lw.student.id=:studentId and lw.task.course.id=:courseId and lw.id=:LWId")
    String findPassedTaskTitleByStudentIdAndCourseIdAndPassedTaskId(Long studentId,
                                                                    Long courseId,
                                                                    Long LWId);
}
