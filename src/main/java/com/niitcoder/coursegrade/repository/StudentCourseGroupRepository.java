package com.niitcoder.coursegrade.repository;

import com.niitcoder.coursegrade.domain.StudentCourseGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StudentCourseGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentCourseGroupRepository extends JpaRepository<StudentCourseGroup, Long> {

}
