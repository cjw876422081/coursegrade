package com.niitcoder.coursegrade.repository;

import com.niitcoder.coursegrade.domain.CourseHomework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the CourseHomework entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseHomeworkRepository extends JpaRepository<CourseHomework, Long> {
@Query(value = "SELECT * FROM course_homework WHERE plan_id = :id", nativeQuery = true)
List<CourseHomework> findByPlanId(@Param("id") Long id);
}
