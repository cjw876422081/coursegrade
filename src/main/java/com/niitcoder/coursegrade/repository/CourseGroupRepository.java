package com.niitcoder.coursegrade.repository;

import com.niitcoder.coursegrade.domain.CourseGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the CourseGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseGroupRepository extends JpaRepository<CourseGroup, Long> {
    List<CourseGroup> findByCourseId(Long id);
}
