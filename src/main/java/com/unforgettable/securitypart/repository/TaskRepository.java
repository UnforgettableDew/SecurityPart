package com.unforgettable.securitypart.repository;

import com.unforgettable.securitypart.dto.TaskDTO;
import com.unforgettable.securitypart.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("select new com.unforgettable.securitypart.dto.TaskDTO(" +
            "t.id, t.title, t.maxScore) from Task t " +
            "join t.laboratoryWorks lw where lw.id=:laboratoryWorkId")
    TaskDTO findTaskByLaboratoryWorkId(Long laboratoryWorkId);

//    @Query("select count(c.tasks) from Course c where c.id=:courseId")
    Integer countTaskByCourseId(Long courseId);

    @Query("select t.maxScore from Task t join t.laboratoryWorks lw" +
            " where lw.id=:laboratoryWorkId")
    Float scoreByLaboratoryWorkId(Long laboratoryWorkId);

    @Query("select new com.unforgettable.securitypart.dto.TaskDTO(t.id, t.title, t.maxScore)" +
            " from Task t where t.course.id=:courseId")
    List<TaskDTO> findTasksByCourseId(Long courseId);
}
