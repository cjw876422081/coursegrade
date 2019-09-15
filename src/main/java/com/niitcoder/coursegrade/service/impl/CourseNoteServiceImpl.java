package com.niitcoder.coursegrade.service.impl;

import com.alibaba.fastjson.util.TypeUtils;
import com.niitcoder.coursegrade.service.CourseNoteService;
import com.niitcoder.coursegrade.domain.CourseNote;
import com.niitcoder.coursegrade.repository.CourseNoteRepository;
import com.niitcoder.coursegrade.service.dto.CourseNoteDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Implementation for managing {@link CourseNote}.
 */
@Service
@Transactional
public class CourseNoteServiceImpl implements CourseNoteService {

    private final Logger log = LoggerFactory.getLogger(CourseNoteServiceImpl.class);

    private final CourseNoteRepository courseNoteRepository;

    private final JdbcTemplate jdbcTemplate;

    public CourseNoteServiceImpl(CourseNoteRepository courseNoteRepository, JdbcTemplate jdbcTemplate) {
        this.courseNoteRepository = courseNoteRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Save a courseNote.
     *
     * @param courseNote the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CourseNote save(CourseNote courseNote) {
        log.debug("Request to save CourseNote : {}", courseNote);
        return courseNoteRepository.save(courseNote);
    }

    /**
     * Get all the courseNotes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CourseNote> findAll(Pageable pageable) {
        log.debug("Request to get all CourseNotes");
        return courseNoteRepository.findAll(pageable);
    }


    /**
     * Get one courseNote by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CourseNote> findOne(Long id) {
        log.debug("Request to get CourseNote : {}", id);
        return courseNoteRepository.findById(id);
    }

    /**
     * Delete the courseNote by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseNote : {}", id);
        //将当前笔记的下级笔记与当前笔记脱离引用关系
        String sql="update course_note set pid=null where pid="+id;
        jdbcTemplate.execute(sql);

        //删除当前行
        courseNoteRepository.deleteById(id);
    }

    @Override
    public Page<CourseNote> findRootNoteByCourse(Long courseId, Pageable pageable) {
        return courseNoteRepository.findByCourseId(courseId,pageable);
    }

    @Override
    public Page<CourseNote> findRootNoteByHomewrok(Long homeworkId, Pageable pageable) {
        return courseNoteRepository.findByHomeworkId(homeworkId,pageable);
    }

    @Override
    public Page<CourseNote> findRootNoteByCoursePlan(Long planId, Pageable pageable) {
        return courseNoteRepository.findByPlanId(planId,pageable);
    }

    @Override
    public Page<CourseNoteDTO> getNoteTreeList(Page<CourseNote> notes) {
        List<CourseNote> noteList=notes.getContent();
        List<CourseNoteDTO> result=new ArrayList<>();
        if(noteList!=null && !noteList.isEmpty()){
            for (CourseNote courseNote : noteList) {
                result.add(getNoteItem(courseNote));
            }
        }
        return new PageImpl<CourseNoteDTO>(result,notes.getPageable(),notes.getTotalElements());
    }

    @Override
    public CourseNoteDTO getNoteItem(CourseNote note) {
        return getNoteItemById(note.getId());
    }

    @Override
    public CourseNoteDTO getNoteItemById(Long noteId) {
        String sql="select a.*,(select count(1) from course_note b where b.parent_note_id=a.id) as leaf from course_note a where id="+noteId;
        Map<String,Object> sqlResult=this.jdbcTemplate.queryForMap(sql);
        CourseNoteDTO courseNoteDTO=new CourseNoteDTO();
        courseNoteDTO.setPublishUser(TypeUtils.castToString(sqlResult.get("publish_user")));
        courseNoteDTO.setLeaf(TypeUtils.castToInt(sqlResult.get("leaf"))>0?false:true);
        courseNoteDTO.setId(TypeUtils.castToLong(sqlResult.get("id")));
        courseNoteDTO.setNoteMemo(TypeUtils.castToString(sqlResult.get("note_memo")));
        String time=TypeUtils.castToString(sqlResult.get("note_time"));
        time=time.replace(".0",".000Z").replace(" ","T");
        courseNoteDTO.setNoteTime(ZonedDateTime.parse(time, DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault())));
        courseNoteDTO.setNoteType(TypeUtils.castToString(sqlResult.get("note_type")));
        courseNoteDTO.setPid(TypeUtils.castToLong(sqlResult.get("parent_note_id")));
        //获取下级笔记
        if(!courseNoteDTO.isLeaf()){
            courseNoteDTO.setChildren(getSubNotes(noteId));
        }
        return courseNoteDTO;
    }

    @Override
    public List<CourseNoteDTO> getSubNotes(Long pid){
        String sql="select a.*,(select count(1) from course_note b where b.parent_note_id=a.id) as leaf from course_note a where parent_note_id="+pid;
        List<Map<String,Object>> sqlResults=jdbcTemplate.queryForList(sql);
        List<CourseNoteDTO> result=new ArrayList<>();
        for (Map<String, Object> sqlResult : sqlResults) {
            CourseNoteDTO courseNoteDTO=new CourseNoteDTO();
            courseNoteDTO.setPublishUser(TypeUtils.castToString(sqlResult.get("publish_user")));
            courseNoteDTO.setLeaf(TypeUtils.castToInt(sqlResult.get("leaf"))>0?false:true);
            courseNoteDTO.setId(TypeUtils.castToLong(sqlResult.get("id")));
            courseNoteDTO.setNoteMemo(TypeUtils.castToString(sqlResult.get("note_memo")));
            String time=TypeUtils.castToString(sqlResult.get("note_time"));
            time=time.replace(".0",".000Z").replace(" ","T");
            courseNoteDTO.setNoteTime(ZonedDateTime.parse(time, DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault())));
            courseNoteDTO.setNoteType(TypeUtils.castToString(sqlResult.get("note_type")));
            courseNoteDTO.setPid(TypeUtils.castToLong(sqlResult.get("parent_note_id")));
            if(!courseNoteDTO.isLeaf()){
                courseNoteDTO.setChildren(getSubNotes(courseNoteDTO.getId()));
            }
            result.add(courseNoteDTO);
        }
        return result;

    }
}
