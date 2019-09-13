package com.niitcoder.coursegrade.repository;

import com.niitcoder.coursegrade.domain.CourseAttachment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CourseAttachment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseAttachmentRepository extends JpaRepository<CourseAttachment, Long> {

}
