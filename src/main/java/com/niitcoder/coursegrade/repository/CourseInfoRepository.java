package com.niitcoder.coursegrade.repository;

import com.niitcoder.coursegrade.domain.CourseInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the CourseInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseInfoRepository extends JpaRepository<CourseInfo, Long> {
    Page<CourseInfo> findByCourseUser(String login, Pageable page);
    /**
     * 根据学生名查找课程信息
     * @param student
     * @return
     */
    @Query(nativeQuery = true, value = "SELECT * FROM course_info WHERE id =(\n" +
        "            SELECT course_id FROM course_group WHERE id=(\n" +
        "            SELECT group_id FROM student_course_group WHERE student=?1))")
    List<CourseInfo> findByStudent(String student);
}
