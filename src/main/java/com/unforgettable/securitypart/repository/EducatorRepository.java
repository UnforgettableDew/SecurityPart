package com.unforgettable.securitypart.repository;

import com.unforgettable.securitypart.entity.Educator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EducatorRepository extends JpaRepository<Educator, Long> {
    @Query("select c.educator.id from Course c where c.id=:courseId")
    Long findEducatorIdByCourse(Long courseId);

    @Query("select e.id from Educator e where e.user.id=:userId")
    Long findEducatorIdByUserId(Long userId);
}
