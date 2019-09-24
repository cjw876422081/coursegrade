package com.niitcoder.coursegrade.repository;


import com.niitcoder.coursegrade.domain.StudentHomework;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the StudentHomework entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentHomeworkRepository extends JpaRepository<StudentHomework, Long> {

    List<StudentHomework> findByStudent(String student, Pageable pageable);

    List<StudentHomework> findByHomeworkId(Long id);

    List<StudentHomework> findByStudentAndHomeworkPlanCourseId(String student,Long id,Pageable pageable);

    /**
     * 查询要删除的已提交作业是否是自己的
     * @param id 要删除的已提交作业id
     * @param studentName 登录名
     * @return
     */
    Optional<StudentHomework> findByIdAndStudent(Long id,String studentName);
}
