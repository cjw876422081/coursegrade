package com.niitcoder.coursegrade.service.impl;

import com.alibaba.fastjson.util.TypeUtils;
import com.niitcoder.coursegrade.domain.CourseHomework;
import com.niitcoder.coursegrade.domain.CourseInfo;
import com.niitcoder.coursegrade.repository.CourseHomeworkRepository;
import com.niitcoder.coursegrade.repository.CourseInfoRepository;
import com.niitcoder.coursegrade.service.CourseHomeworkService;
import com.niitcoder.coursegrade.service.CoursePlanService;
import com.niitcoder.coursegrade.domain.CoursePlan;
import com.niitcoder.coursegrade.repository.CoursePlanRepository;
import com.niitcoder.coursegrade.service.dto.CourseInfoPlan;
import com.niitcoder.coursegrade.service.dto.CoursePlanDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Implementation for managing {@link CoursePlan}.
 */
@Service
@Transactional
public class CoursePlanServiceImpl implements CoursePlanService {

    private final Logger log = LoggerFactory.getLogger(CoursePlanServiceImpl.class);

    private final CoursePlanRepository coursePlanRepository;

    private final CourseInfoRepository courseInfoRepository;

    private final CourseHomeworkRepository courseHomeworkRepository;

    private final JdbcTemplate jdbcTemplate;

    public CoursePlanServiceImpl(CoursePlanRepository coursePlanRepository, CourseInfoRepository courseInfoRepository,
                                 CourseHomeworkRepository courseHomeworkRepository, JdbcTemplate jdbcTemplate) {
        this.coursePlanRepository = coursePlanRepository;
        this.courseInfoRepository = courseInfoRepository;
        this.courseHomeworkRepository = courseHomeworkRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Save a coursePlan.
     *
     * @param coursePlan the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CoursePlan save(CoursePlan coursePlan) {
        log.debug("Request to save CoursePlan : {}", coursePlan);
        coursePlan.setDataTime(ZonedDateTime.now());
        return coursePlanRepository.save(coursePlan);
    }

    /**
     * Get all the coursePlans.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CoursePlan> findAll(Pageable pageable) {
        log.debug("Request to get all CoursePlans");
        return coursePlanRepository.findAll(pageable);
    }


    /**
     * Get one coursePlan by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CoursePlan> findOne(Long id) {
        log.debug("Request to get CoursePlan : {}", id);
        return coursePlanRepository.findById(id);
    }

    /**
     * Delete the coursePlan by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CoursePlan : {}", id);
        coursePlanRepository.deleteById(id);
    }

    @Override
    public CourseInfoPlan getCourseInfoPlan(Long courseId) {
        CourseInfo courseInfo = new CourseInfo();
        courseInfo = courseInfoRepository.findById(courseId).get();
        CourseInfoPlan courseInfoPlan = new CourseInfoPlan();
        courseInfoPlan.setId(courseInfo.getId());
        courseInfoPlan.setCourseCode(courseInfo.getCourseCode());
        courseInfoPlan.setCourseName(courseInfo.getCourseName());
        courseInfoPlan.setCourseCount(courseInfo.getCourseCount());
        courseInfoPlan.setCourseWeekCount(courseInfo.getCourseWeekCount());
        courseInfoPlan.setCourseMemo(courseInfo.getCourseMemo());
        courseInfoPlan.setDataTime(courseInfo.getDataTime());
        courseInfoPlan.setCourseUser(courseInfo.getCourseUser());

        List<CoursePlanDTO> coursePlanDTOList = new ArrayList<>();
        coursePlanDTOList = getCoursePlanDTOByCourseId(courseInfoPlan.getId());
        courseInfoPlan.setChildren(coursePlanDTOList);

        return courseInfoPlan;
    }

    @Override
    public List<CoursePlan> getPlanByParentId(Long id) {
        List<CoursePlan> coursePlanList = new ArrayList<>();
        coursePlanList = coursePlanRepository.findByParentPlanId(id);
        return coursePlanList;
    }

    @Override
    public List<CoursePlanDTO> getCoursePlanDTOByCourseId(Long courseId) {
        String sql = "SELECT a.*,(SELECT COUNT(1) FROM course_plan b WHERE b.parent_plan_id=a.id) as leaf FROM course_plan a WHERE a.course_id =" + courseId;
        List<Map<String, Object>> sqlResults = jdbcTemplate.queryForList(sql);
        List<CoursePlanDTO> result = new ArrayList<>();
        for (Map<String, Object> sqlResult : sqlResults) {
            CoursePlanDTO coursePlanDTO = new CoursePlanDTO();
            coursePlanDTO.setId(TypeUtils.castToLong(sqlResult.get("id")));
            coursePlanDTO.setpId(TypeUtils.castToLong(sqlResult.get("parent_plan_id")));
            coursePlanDTO.setLeaf(TypeUtils.castToInt(sqlResult.get("leaf")) > 0 ? false : true);
            coursePlanDTO.setPlanMemo(TypeUtils.castToString(sqlResult.get("plan_memo")));
            coursePlanDTO.setPlanTarget(TypeUtils.castToString(sqlResult.get("plan_target")));
            String time = TypeUtils.castToString(sqlResult.get("data_time"));
            time = time.replace(".0", ".000Z").replace(" ", "T");
            coursePlanDTO.setPlanTime(ZonedDateTime.parse(time, DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault())));
            coursePlanDTO.setPlanCount(TypeUtils.castToInt(sqlResult.get("plan_count")));
            coursePlanDTO.setCourse(TypeUtils.castToLong(sqlResult.get("course_id")));

            List<CourseHomework> courseHomeworks = new ArrayList<>();
            courseHomeworks = courseHomeworkRepository.findByPlanId(TypeUtils.castToLong(sqlResult.get("id")));
            coursePlanDTO.setCourseHomeworks(courseHomeworks);
            if (!coursePlanDTO.isLeaf()) {
                coursePlanDTO.setChildren(getSubPlan(coursePlanDTO.getId()));
            }
            result.add(coursePlanDTO);
        }
        return result;
    }

    @Override
    public List<CoursePlanDTO> getSubPlan(Long pid) {
        String sql = "select a.*,(select count(1) from course_plan b where b.parent_plan_id=a.id) as leaf from course_plan a where parent_plan_id=" + pid;
        List<Map<String, Object>> sqlResults = jdbcTemplate.queryForList(sql);
        List<CoursePlanDTO> result = new ArrayList<>();
        for (Map<String, Object> sqlResult : sqlResults) {
            CoursePlanDTO coursePlanDTO = new CoursePlanDTO();
            coursePlanDTO.setId(TypeUtils.castToLong(sqlResult.get("id")));
            coursePlanDTO.setpId(TypeUtils.castToLong(sqlResult.get("parent_plan_id")));
            coursePlanDTO.setLeaf(TypeUtils.castToInt(sqlResult.get("leaf")) > 0 ? false : true);
            coursePlanDTO.setPlanMemo(TypeUtils.castToString(sqlResult.get("plan_memo")));
            coursePlanDTO.setPlanTarget(TypeUtils.castToString(sqlResult.get("plan_target")));
            String time = TypeUtils.castToString(sqlResult.get("data_time"));
            time = time.replace(".0", ".000Z").replace(" ", "T");
            coursePlanDTO.setPlanTime(ZonedDateTime.parse(time, DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault())));
            coursePlanDTO.setPlanCount(TypeUtils.castToInt(sqlResult.get("plan_count")));
            coursePlanDTO.setCourse(TypeUtils.castToLong(sqlResult.get("course_id")));

            List<CourseHomework> courseHomeworks = new ArrayList<>();
            courseHomeworks = courseHomeworkRepository.findByPlanId(TypeUtils.castToLong(sqlResult.get("id")));
            coursePlanDTO.setCourseHomeworks(courseHomeworks);
            if (!coursePlanDTO.isLeaf()) {
                coursePlanDTO.setChildren(getSubPlan(coursePlanDTO.getId()));
            }
            result.add(coursePlanDTO);
        }
        return result;
    }
}
