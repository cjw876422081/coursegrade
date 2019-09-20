package com.niitcoder.coursegrade.service.impl;

import com.alibaba.fastjson.util.TypeUtils;
import com.niitcoder.coursegrade.domain.CourseInfo;
import com.niitcoder.coursegrade.security.SecurityUtils;
import com.niitcoder.coursegrade.service.StudentHomeworkService;
import com.niitcoder.coursegrade.domain.StudentHomework;
import com.niitcoder.coursegrade.repository.StudentHomeworkRepository;
import com.niitcoder.coursegrade.service.dto.StudentHomewrokDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
 * Service Implementation for managing {@link StudentHomework}.
 */
@Service
@Transactional
public class StudentHomeworkServiceImpl implements StudentHomeworkService {

    private final Logger log = LoggerFactory.getLogger(StudentHomeworkServiceImpl.class);

    private final StudentHomeworkRepository studentHomeworkRepository;
    private final JdbcTemplate jdbcTemplate;

    public StudentHomeworkServiceImpl(StudentHomeworkRepository studentHomeworkRepository, JdbcTemplate jdbcTemplate) {
        this.studentHomeworkRepository = studentHomeworkRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Save a studentHomework.
     *
     * @param studentHomework the entity to save.
     * @return the persisted entity.
     */
    @Override
    public StudentHomework save(StudentHomework studentHomework) {
        log.debug("Request to save StudentHomework : {}", studentHomework);
        return studentHomeworkRepository.save(studentHomework);
    }

    /**
     * Get all the studentHomeworks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StudentHomework> findAll(Pageable pageable) {
        log.debug("Request to get all StudentHomeworks");
        return studentHomeworkRepository.findAll(pageable);
    }


    /**
     * Get one studentHomework by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StudentHomework> findOne(Long id) {
        log.debug("Request to get StudentHomework : {}", id);
        return studentHomeworkRepository.findById(id);
    }

    /**
     * Delete the studentHomework by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StudentHomework : {}", id);
        studentHomeworkRepository.deleteById(id);
    }

    @Override
    public Integer getOrderCourseGrade(Integer homework, String student) {
        log.debug("Request to delete StudentHomework : {}", homework , student);
        String sql = "select grade from student_homework where homework_id=" +homework +" and student = "+ "\""+student +"\"";
        List<Map<String, Object>> sqlResult =jdbcTemplate.queryForList(sql);
        if (!sqlResult.isEmpty()){
            return (Integer) sqlResult.get(0).get("grade");
        }
        return 0 ;
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
    public Page<StudentHomework> findHomework(String name,Pageable pageable)
    {
        return studentHomeworkRepository.findByStudent(name,pageable);
    }

    /**
     * 根据作业id查学生提交情况
     * @param id
     * @return
     */

    @Override
    public Page<StudentHomework> getStudentHomeworkByCourseHomework(Long id,Pageable pageable) throws Exception{
        log.debug("Request to get StudentHomework : {}", id);
        //Optional<StudentHomework> courseInfo=studentHomeworkRepository.findById(id);
        List<StudentHomework> studentHomeworks=new ArrayList<StudentHomework>();
        studentHomeworks=studentHomeworkRepository.findByHomeworkId(id);
        String loginName= SecurityUtils.getCurrentUserLogin().get();
        if(studentHomeworks!=null && studentHomeworks.size()>0) {
            String userName=studentHomeworks.get(0).getHomework().getPlan().getCourse().getCourseUser();
            if (!userName.equals(loginName)) {
                throw new Exception("无权查询此作业");
            } else {
                return listConvertToPage(studentHomeworks, pageable);
            }
        }else {
            throw new Exception("该作业不存在");
        }
    }

    @Override
    public Optional<StudentHomework> updateStudentHomeworkGrade(Long id, Long grade) {
        log.debug("Request to update StudentHomeworkGrade : {},{}", id,grade);
        String sql="UPDATE student_homework SET grade="+grade+" WHERE id="+id;
        this.jdbcTemplate.update(sql);
        return studentHomeworkRepository.findById(id);
    }

}
