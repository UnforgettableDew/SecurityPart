package com.unforgettable.securitypart.repository;

import com.unforgettable.securitypart.dto.LaboratoryWorkDTO;
import com.unforgettable.securitypart.entity.LaboratoryWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaboratoryWorkRepository extends JpaRepository<LaboratoryWork, Long> {
    @Query("select new com.unforgettable.securitypart.dto.LaboratoryWorkDTO(" +
            "lw.id, lw.score, lw.isPassed) from LaboratoryWork lw " +
            "where lw.student.id=:studentId and lw.task.course.id=:courseId")
    List<LaboratoryWorkDTO> findLaboratoryWorksByStudentIdAndCourseId(Long studentId, Long courseId);

    @Query("select new com.unforgettable.securitypart.dto.LaboratoryWorkDTO(" +
            "lw.id, lw.title, lw.githubReference, lw.score, lw.isPassed, lw.comment) from LaboratoryWork lw " +
            "where lw.student.id=:studentId and lw.task.course.id=:courseId and lw.id=:LWId")
    LaboratoryWorkDTO findLaboratoryWorksByStudentIdAndCourseIdAndLWId(Long studentId,
                                                                             Long courseId,
                                                                             Long LWId);
    @Query("select new com.unforgettable.securitypart.dto.LaboratoryWorkDTO(" +
            "lw.id, lw.score, lw.isPassed) from LaboratoryWork lw " +
            "join lw.task t where lw.student.id=:studentId and t.id=:taskId and t.course.id=:courseId")
    LaboratoryWorkDTO findLaboratoryWorkByStudentIdCourseIdTaskId(Long studentId, Long courseId, Long taskId);

    @Query("select new com.unforgettable.securitypart.dto.LaboratoryWorkDTO(" +
            "lw.id, lw.score, lw.isPassed, lw.githubReference) from LaboratoryWork lw " +
            "join lw.task t where lw.student.id=:studentId and t.course.id=:courseId " +
            "and lw.isPassed=false")
    List<LaboratoryWorkDTO> findUncheckedLWByStudentAndCourse(Long studentId, Long courseId);

    @Query("select count(lw) from LaboratoryWork lw " +
            "where lw.task.course.id=:courseId and lw.student.id=:studentId")
    Integer countPassedLwByCourseAndStudent(Long courseId, Long studentId);

    @Query("select count(lw) from LaboratoryWork lw " +
            "where lw.task.course.id=:courseId")
    Integer countByCourseId(Long courseId);

    @Query("select avg(lw.score) from LaboratoryWork lw " +
            "where lw.task.course.id=:courseId and lw.task.id=:taskId")
    Float avgScoreForLWByCourseAndTask(Long courseId, Long taskId);

    @Query("select lw.githubReference from LaboratoryWork lw where lw.id=:labId")
    String findGithubReferenceByLabId(Long labId);
}
