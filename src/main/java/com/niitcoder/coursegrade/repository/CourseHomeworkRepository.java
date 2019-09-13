package com.niitcoder.coursegrade.repository;

import com.niitcoder.coursegrade.domain.CourseHomework;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CourseHomework entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseHomeworkRepository extends JpaRepository<CourseHomework, Long> {

}
