package com.niitcoder.coursegrade.repository;

import com.niitcoder.coursegrade.domain.CourseInfo;
import com.niitcoder.coursegrade.domain.StudentCourseGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the StudentCourseGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentCourseGroupRepository extends JpaRepository<StudentCourseGroup, Long> {
    List<StudentCourseGroup> findByGroupId(Long id);
}
