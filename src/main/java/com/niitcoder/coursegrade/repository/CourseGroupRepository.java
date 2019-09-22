package com.niitcoder.coursegrade.repository;

import com.niitcoder.coursegrade.domain.CourseGroup;
import com.niitcoder.coursegrade.domain.CourseInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the CourseGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseGroupRepository extends JpaRepository<CourseGroup, Long> {
    List<CourseGroup> findByCourseId(Long id);
    List<CourseGroup> findByCourseCourseUser(String courseUser);

    @Override
    Optional<CourseGroup> findById(Long id);
}
