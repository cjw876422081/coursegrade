package com.niitcoder.coursegrade.repository;


import com.niitcoder.coursegrade.domain.CourseHomework;
import com.niitcoder.coursegrade.domain.CourseInfo;
import com.niitcoder.coursegrade.domain.StudentHomework;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the StudentHomework entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentHomeworkRepository extends JpaRepository<StudentHomework, Long> {

    Page<StudentHomework> findByStudent(String name, Pageable pageable);
    List<StudentHomework> findByHomeworkId(Long id);

    /*@Query(nativeQuery = true ,value = "SELECT course_info.* FROM course_info WHERE id=(\n" +
        "SELECT course_plan.course_id FROM course_plan WHERE id=(\n" +
        "SELECT course_homework.plan_id FROM course_homework WHERE id=?1))")
    CourseInfo findById(Long id);*/



}
