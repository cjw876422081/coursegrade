package com.niitcoder.coursegrade.repository;

import com.niitcoder.coursegrade.domain.CourseHomework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the CourseHomework entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseHomeworkRepository extends JpaRepository<CourseHomework, Long> {
@Query(value = "SELECT * FROM course_homework WHERE plan_id = :id", nativeQuery = true)
List<CourseHomework> findByPlanId(@Param("id") Long id);

//查询指定课程的
@Query("select a from CourseHomework a where a.plan.course.id=?")
    List<CourseHomework> findByCourseId(Long course_id);


    /**
     * 通过授课内容查找所有作业
     *
     * @param planId
     * @return
     */
    @Query("SELECT a FROM CourseHomework a ,CoursePlan b WHERE a.plan = b.id AND b.id = ?1")
    List<CourseHomework> findByPlanId(int planId);


    @Override
    Optional<CourseHomework> findById(Long aLong);
}
