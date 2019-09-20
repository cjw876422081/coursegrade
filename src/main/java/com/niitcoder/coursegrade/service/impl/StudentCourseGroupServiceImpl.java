package com.niitcoder.coursegrade.service.impl;

import com.alibaba.fastjson.util.TypeUtils;
import com.niitcoder.coursegrade.domain.CourseInfo;
import com.niitcoder.coursegrade.repository.CourseInfoRepository;
import com.niitcoder.coursegrade.security.SecurityUtils;
import com.niitcoder.coursegrade.service.StudentCourseGroupService;
import com.niitcoder.coursegrade.domain.StudentCourseGroup;
import com.niitcoder.coursegrade.repository.StudentCourseGroupRepository;
import com.niitcoder.coursegrade.service.dto.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Implementation for managing {@link StudentCourseGroup}.
 */
@Service
@Transactional
public class StudentCourseGroupServiceImpl implements StudentCourseGroupService {

    private final Logger log = LoggerFactory.getLogger(StudentCourseGroupServiceImpl.class);

    private final StudentCourseGroupRepository studentCourseGroupRepository;
    private final CourseInfoRepository courseInfoRepository;
    private final JdbcTemplate jdbcTemplate ;

    public StudentCourseGroupServiceImpl(StudentCourseGroupRepository studentCourseGroupRepository,CourseInfoRepository courseInfoRepository, JdbcTemplate jdbcTemplate) {
        this.studentCourseGroupRepository = studentCourseGroupRepository;
        this.courseInfoRepository= courseInfoRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Save a studentCourseGroup.
     *
     * @param studentCourseGroup the entity to save.
     * @return the persisted entity.
     */
    @Override
    public StudentCourseGroup save(StudentCourseGroup studentCourseGroup) {
        log.debug("Request to save StudentCourseGroup : {}", studentCourseGroup);
        return studentCourseGroupRepository.save(studentCourseGroup);
    }

    /**
     * Get all the studentCourseGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StudentCourseGroup> findAll(Pageable pageable) {
        log.debug("Request to get all StudentCourseGroups");
        return studentCourseGroupRepository.findAll(pageable);
    }


    /**
     * Get one studentCourseGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StudentCourseGroup> findOne(Long id) {
        log.debug("Request to get StudentCourseGroup : {}", id);
        return studentCourseGroupRepository.findById(id);
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

    @Override
    public Page<StudentCourseGroup> findStudentByGroup(Long id, Pageable pageable)throws Exception {
        log.debug("Request to findByCourseName  : {}", id);

        List<StudentCourseGroup> studentCourseGroups=studentCourseGroupRepository.findByGroupId(id);

        String loginName= SecurityUtils.getCurrentUserLogin().get();
        if(studentCourseGroups!=null &&studentCourseGroups.size()>0) {
            String userName=studentCourseGroups.get(0).getGroup().getCourse().getCourseUser();
            if (!userName.equals(loginName)) {
                throw new Exception("无权搜索该课程学生！");
            } else {
                return listConvertToPage(studentCourseGroups, pageable);
            }

        }else {
            throw new Exception("课程不存在");
        }
    }

    /**
     * Delete the studentCourseGroup by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StudentCourseGroup : {}", id);
        studentCourseGroupRepository.deleteById(id);
    }

    @Override
    public List<Map<String, Object>> getCourseGroup(String student, Long course_id) {
        String sql = "select student from student_course_group where group_id = (\n" +
            "\tselect id from course_group where course_id = "+ course_id +" and id =(\n" +
            "\tselect group_id from student_course_group where student = \""+student+"\"\n" +
            ")\n" +
            ") " ;
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql) ;
        return result ;
    }

    @Override
    public List<CourseInfo> getMyCourse(String student) {
        return courseInfoRepository.findByStudent(student);
    }
}
