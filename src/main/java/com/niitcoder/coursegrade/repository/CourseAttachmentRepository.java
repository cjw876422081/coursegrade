package com.niitcoder.coursegrade.repository;

import com.niitcoder.coursegrade.domain.CourseAttachment;
import com.niitcoder.coursegrade.domain.CourseHomework;
import com.niitcoder.coursegrade.domain.CourseHomework_;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the CourseAttachment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseAttachmentRepository extends JpaRepository<CourseAttachment, Long> {

    /**
     * 查询已提交作业所携带的附件
     * @param fileUser
     * @param homeworkId
     * @return
     */
    Optional<List<CourseAttachment>> findCourseAttachmentsByFileUserAndHomework_Id(String fileUser,Long homeworkId);

    /**
     * 删除已提交作业所携带的附件
     * @param fileUser
     * @param homeworkId
     */
    void deleteCourseAttachmentByFileUserAndHomework_Id(String fileUser,Long homeworkId);

}
