package com.niitcoder.coursegrade.service.impl;

import com.alibaba.fastjson.util.TypeUtils;
import com.niitcoder.coursegrade.domain.CourseAttachment;
import com.niitcoder.coursegrade.repository.CourseAttachmentRepository;
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
import java.util.*;

/**
 * Service Implementation for managing {@link StudentHomework}.
 */
@Service
@Transactional
public class StudentHomeworkServiceImpl implements StudentHomeworkService {

    private final Logger log = LoggerFactory.getLogger(StudentHomeworkServiceImpl.class);

    private final StudentHomeworkRepository studentHomeworkRepository;
    private final CourseAttachmentRepository courseAttachmentRepository;
    private final JdbcTemplate jdbcTemplate;

    public StudentHomeworkServiceImpl(StudentHomeworkRepository studentHomeworkRepository, CourseAttachmentRepository courseAttachmentRepository,JdbcTemplate jdbcTemplate) {
        this.studentHomeworkRepository = studentHomeworkRepository;
        this.courseAttachmentRepository = courseAttachmentRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Save a studentHomework.
     *
     * @param studentHomework the entity to save.
     * @return the persisted entity.
     */
    @Override
    public StudentHomework save(StudentHomework studentHomework) throws Exception {
        log.debug("Request to save StudentHomework : {}", studentHomework);
        Long homeworkId = studentHomework.getHomework().getId();
        // 验证学生提交作业对应的作业是否属于自己的课程
       String sql = "SELECT c4.id " +
                   "FROM student_course_group s1,course_group c1,course_info c2,course_plan c3,course_homework c4 " +
                   "WHERE s1.group_id=c1.id " +
                   "AND c1.course_id=c2.id " +
                   "AND c3.course_id=c2.id " +
                   "AND c4.plan_id=c3.id " +
                   "AND c3.parent_plan_id !=\"\";";
        List<Map<String,Object>> list=this.jdbcTemplate.queryForList(sql);
        Boolean flag = false;
        for (int i = 0; i <list.size() ; i++) {
            Map idMap = list.get(i);
           if(idMap.get("id") == homeworkId){
               flag = true;
               break;
           }
        }
        if(flag){
            String loginName = SecurityUtils.getCurrentUserLogin().get();
            studentHomework.setStudent(loginName);
            studentHomework.setSubmitTime(ZonedDateTime.now());
            studentHomeworkRepository.save(studentHomework);
            return studentHomework;
        }
        throw new Exception("该作业不属于你的课程！");
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
        Optional<StudentHomework> studentHomework = studentHomeworkRepository.findById(id);
        String loginName = SecurityUtils.getCurrentUserLogin().get();
        Long homeworkId = studentHomework.get().getHomework().getId();
        // 查找提交作业时所携带的附件 如果有则删除
        Optional<List<CourseAttachment>> courseAttachments = courseAttachmentRepository.findCourseAttachmentsByFileUserAndHomework_Id(loginName,homeworkId);
        if(courseAttachments.isPresent()){
            courseAttachmentRepository.deleteCourseAttachmentByFileUserAndHomework_Id(loginName,homeworkId);
        }
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

    @Override
    public Page<StudentHomewrokDTO> getStudentHomeworkByCourseHomework(String homeworkCode, Pageable pageable) {
        log.debug("Request to get StudentHomework : {}", homeworkCode);
        String sql="SELECT a.* FROM student_homework a,course_homework b WHERE b.homework_code='"+
            homeworkCode+"' AND a.homework_id=b.id";

        List<Map<String,Object>> sqlResult=this.jdbcTemplate.queryForList(sql);
        List<StudentHomewrokDTO> result = new ArrayList<StudentHomewrokDTO>();

        if(sqlResult!=null && sqlResult.size()>0){
            for (Map<String, Object> sqlItem : sqlResult) {
                StudentHomewrokDTO item = new StudentHomewrokDTO();
                item.setId(TypeUtils.castToLong(sqlItem.get("id")));
                item.setSubmitMemo(TypeUtils.castToString(sqlItem.get("submit_memo")));

                String time1=TypeUtils.castToString(sqlItem.get("read_time"));
                time1=time1.replace(".0",".000Z").replace(" ","T");
                item.setSubmitTime(ZonedDateTime.parse(time1, DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault())));

                String time2=TypeUtils.castToString(sqlItem.get("submit_time"));
                time2=time2.replace(".0",".000Z").replace(" ","T");
                item.setReadTime(ZonedDateTime.parse(time2, DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault())));

                item.setGrade(TypeUtils.castToInt(sqlItem.get("grade")));
                item.setStudent(TypeUtils.castToString(sqlItem.get("student")));
                item.setTeacher(TypeUtils.castToString(sqlItem.get("teacher")));
                result.add(item);
            }
            return listConvertToPage(result,pageable);
        }
        return null;
    }

    @Override
    public Optional<StudentHomework> updateStudentHomeworkGrade(Long id, Long grade) {
        log.debug("Request to update StudentHomeworkGrade : {},{}", id,grade);
        String sql="UPDATE student_homework SET grade="+grade+" WHERE id="+id;
        this.jdbcTemplate.update(sql);
        return studentHomeworkRepository.findById(id);
    }

}
