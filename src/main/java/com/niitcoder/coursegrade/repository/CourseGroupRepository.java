package com.niitcoder.coursegrade.repository;

import com.niitcoder.coursegrade.domain.CourseGroup;
import com.niitcoder.coursegrade.domain.CourseInfo;
import com.niitcoder.coursegrade.service.dto.CourseGroupDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the CourseGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseGroupRepository extends JpaRepository<CourseGroup, Long> {

    List<CourseGroupDTO> findAllByCourse(CourseInfo course);

}
