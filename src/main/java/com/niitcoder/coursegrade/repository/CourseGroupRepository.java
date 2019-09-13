package com.niitcoder.coursegrade.repository;

import com.niitcoder.coursegrade.domain.CourseGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CourseGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseGroupRepository extends JpaRepository<CourseGroup, Long> {

}
