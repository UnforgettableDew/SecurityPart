package com.unforgettable.securitypart.repository;

import com.unforgettable.securitypart.dto.StudentDTO;
import com.unforgettable.securitypart.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("select s.id from Course c join c.students s where c.id=:courseId")
    List<Long> findStudentIdByCourse(Long courseId);
    @Query("select s.id from Student s where s.user.id=:userId")
    Long findStudentIdByUserId(Long userId);
    @Query("select new com.unforgettable.securitypart.dto.StudentDTO(" +
            "s.id, s.firstname, s.lastname, s.group) from Student s " +
            "join s.courses c where c.id=:courseId and c.educator.id=:educatorId")
    List<StudentDTO> findStudentsByCourseIdAndEducatorId(Long courseId, Long educatorId);
    @Query("select new com.unforgettable.securitypart.dto.StudentDTO(" +
            "s.id, s.firstname, s.lastname, s.group) " +
            "from Student s join s.laboratoryWorks lw " +
            "where lw.task.id=:taskId and lw.task.course.id=:courseId")
    List<StudentDTO> findStudentsWhoPassedTask(Long courseId, Long taskId);

    @Query("select distinct new com.unforgettable.securitypart.dto.StudentDTO(" +
            "s.id, s.firstname, s.lastname, s.group) from Student s " +
            "join s.laboratoryWorks lw " +
            "where lw.isPassed=false and lw.task.course.id=:courseId")
    List<StudentDTO> findStudentWithUncheckedLW(Long courseId);
    @Query("select new com.unforgettable.securitypart.dto.StudentDTO(" +
            "s.id, s.firstname, s.lastname, s.group, s.email, s.age, s.registrationDate, s.telegramContact)" +
            " from Student s join s.courses c" +
            " where s.id=:studentId and c.id=:courseId and c.educator.id=:educatorId")
    StudentDTO findStudentByIdAndCourseId(Long studentId, Long courseId, Long educatorId);

    @Query("select count(s) from Student s join s.courses c " +
            "where c.id=:courseId")
    Integer countStudentsByCourseId(Long courseId);

    @Query("select count(s) from Student s join s.laboratoryWorks lw " +
            "where lw.task.id=:taskId")
    Integer countStudentsByTaskId(Long taskId);
}
