package com.niitcoder.coursegrade.service.impl;

import com.niitcoder.coursegrade.domain.CourseInfo;
import com.niitcoder.coursegrade.service.CourseGroupService;
import com.niitcoder.coursegrade.domain.CourseGroup;
import com.niitcoder.coursegrade.repository.CourseGroupRepository;
import com.alibaba.fastjson.util.TypeUtils;
import com.niitcoder.coursegrade.service.dto.CourseGroupDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.jdbc.core.JdbcTemplate;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Implementation for managing {@link CourseGroup}.
 */
@Service
@Transactional
public class CourseGroupServiceImpl implements CourseGroupService {

    private final Logger log = LoggerFactory.getLogger(CourseGroupServiceImpl.class);

    private final CourseGroupRepository courseGroupRepository;
    private final JdbcTemplate jdbcTemplate;

    public CourseGroupServiceImpl(CourseGroupRepository courseGroupRepository, JdbcTemplate jdbcTemplate) {
        this.courseGroupRepository = courseGroupRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Save a courseGroup.
     *
     * @param courseGroup the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CourseGroup save(CourseGroup courseGroup) {
        log.debug("Request to save CourseGroup : {}", courseGroup);
        return courseGroupRepository.save(courseGroup);
    }

    /**
     * Get all the courseGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CourseGroup> findAll(Pageable pageable) {
        log.debug("Request to get all CourseGroups");
        return courseGroupRepository.findAll(pageable);
    }


    /**
     * Get one courseGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CourseGroup> findOne(Long id) {
        log.debug("Request to get CourseGroup : {}", id);
        return courseGroupRepository.findById(id);
    }

    /**
     * list转page
     * @param list
     * @param pageable
     * @param <T>
     * @return
     */
    public <T> Page<T> listConvertToPage(List<T> list, Pageable pageable) {
        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : ( start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }
    /**
     * Get one courseGroup by course.
     *
     * @param course the course of the entity.
     * @return the entity.
     *//*
    @Override
    @Transactional(readOnly = true)
    public List<CourseGroup> findGroupByCourse(CourseInfo course) {
        log.debug("Request to get CourseGroup : {}", course);
        return courseGroupRepository.findAllByCourse(course);
    }*/

    /**
     * 通过课程名检索课程
     * @param course
     * @return
     */
    @Override
    public Page<CourseGroupDTO> findByCourse(String course,Pageable pageable) {
        log.debug("Request to findByCourseName  : {}", course);
        String sql="SELECT a.* FROM course_group a,course_info b WHERE b.course_name = '"+
                    course+"' AND a.course_id = b.id";

        List<Map<String,Object>> sqlResult=this.jdbcTemplate.queryForList(sql);
        List<CourseGroupDTO> result = new ArrayList<CourseGroupDTO>();

        if(sqlResult!=null && sqlResult.size()>0){
            for (Map<String, Object> sqlItem : sqlResult) {
                CourseGroupDTO item=new CourseGroupDTO();
                item.setId(TypeUtils.castToLong(sqlItem.get("id")));
                item.setGroupCode(TypeUtils.castToString(sqlItem.get("group_code")));
                item.setGroupName(TypeUtils.castToString(sqlItem.get("group_name")));
                item.setGroupCount(TypeUtils.castToInt(sqlItem.get("group_count")));

                String time=TypeUtils.castToString(sqlItem.get("data_time"));
                time=time.replace(".0",".000Z").replace(" ","T");
                item.setDataTime(ZonedDateTime.parse(time, DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault())));

                result.add(item);
            }
            return listConvertToPage(result,  pageable);
        }
        return null;
    }

    /**
     * Delete the courseGroup by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseGroup : {}", id);
        courseGroupRepository.deleteById(id);
    }
}
