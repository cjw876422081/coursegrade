package com.niitcoder.coursegrade.repository;

import com.niitcoder.coursegrade.domain.CourseInfo;
import com.niitcoder.coursegrade.domain.StudentCourseGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the StudentCourseGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentCourseGroupRepository extends JpaRepository<StudentCourseGroup, Long> {

    List<StudentCourseGroup> findByGroupId(Long id);

    Optional<StudentCourseGroup> findByStudent(String login);

    /**
     * 根据班级id和登录名查询
     * @param groupId
     * @param studentName
     * @return
     */
    Optional<StudentCourseGroup> findByGroupIdAndStudent(Long groupId,String studentName);
}
