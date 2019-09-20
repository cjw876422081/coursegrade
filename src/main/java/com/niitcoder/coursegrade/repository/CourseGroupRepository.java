package com.niitcoder.coursegrade.repository;

import com.niitcoder.coursegrade.domain.CourseGroup;
import com.niitcoder.coursegrade.domain.CourseInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    List<CourseInfo> findCourseByCourseId(Long id);

    String findCourseCourseUserByCourseId(Long id);

    List<CourseGroup> findByCourseCourseUser(String courseUser);

    List<CourseGroup> findAllByCourseId(Long id);

    Page<CourseGroup> findByCourse(Long id, Pageable pageable) throws Exception;

}
