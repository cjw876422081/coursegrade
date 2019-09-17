package com.niitcoder.coursegrade.repository;

import com.niitcoder.coursegrade.domain.CourseInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CourseInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseInfoRepository extends JpaRepository<CourseInfo, Long> {
    Page<CourseInfo> findByCourseUser(String login, Pageable page);
}
