package com.niitcoder.coursegrade.service.impl;

import com.niitcoder.coursegrade.domain.*;
import com.niitcoder.coursegrade.repository.*;
import com.niitcoder.coursegrade.security.SecurityUtils;
import com.niitcoder.coursegrade.service.CourseGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link CourseGroup}.
 */
@Service
@Transactional
public class CourseGroupServiceImpl implements CourseGroupService {

    private final Logger log = LoggerFactory.getLogger(CourseGroupServiceImpl.class);

    private final CourseGroupRepository courseGroupRepository;
    private final StudentCourseGroupRepository studentCourseGroupRepository;
    private final CoursePlanRepository coursePlanRepository;
    private final CourseNoteRepository courseNoteRepository;

    public CourseGroupServiceImpl(CourseGroupRepository courseGroupRepository, StudentCourseGroupRepository studentCourseGroupRepository, CoursePlanRepository coursePlanRepository, CourseNoteRepository courseNoteRepository) {
        this.courseGroupRepository = courseGroupRepository;
        this.studentCourseGroupRepository = studentCourseGroupRepository;
        this.coursePlanRepository = coursePlanRepository;
        this.courseNoteRepository = courseNoteRepository;
    }

    /**
     * Save a courseGroup.
     *
     * @param courseGroup the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CourseGroup save(CourseGroup courseGroup) throws Exception {
        log.debug("Request to save CourseGroup : {}", courseGroup);
        String loginName = SecurityUtils.getCurrentUserLogin().get();
        List<CourseGroup> courseGroups=courseGroupRepository.findByCourseCourseUser(loginName);
        if(courseGroups!=null &&courseGroups.size()>0){
            courseGroup.setDataTime(ZonedDateTime.now());
            courseGroup.setCourse(courseGroups.get(0).getCourse());
            return courseGroupRepository.save(courseGroup);
        }else{
            throw new Exception("无创建和更改班级权限.");
        }
    }

    /**
     * Get all the courseGroups.
     *
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CourseGroup> findAll() throws Exception {
        log.debug("Request to get all CourseGroups");

        String loginName = SecurityUtils.getCurrentUserLogin().get();
        List<CourseGroup> courseGroups=courseGroupRepository.findByCourseCourseUser(loginName);
        if(courseGroups!=null &&courseGroups.size()>0){
            Long courseId=courseGroups.get(0).getCourse().getId();
            return courseGroupRepository.findAllByCourseId(courseId);
        }else{
            throw new Exception("无查询班级权限.");
        }
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
     * Delete the courseGroup by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) throws Exception {
        log.debug("Request to delete CourseGroup : {}", id);
        String loginName = SecurityUtils.getCurrentUserLogin().get();
        //查询用户创建的班级，检查用户是否创建过班级，若没有则没有权限解散班级
        List<CourseGroup> courseGroups=courseGroupRepository.findByCourseCourseUser(loginName);
        if(courseGroups!=null &&courseGroups.size()>0){
            //默认没有权限删除班级，设置flag为false
            boolean flag=false;
           // 检查将要删除的班级是否是该用户创建
            for(CourseGroup item: courseGroups){
                //若在用户创建的班级中找到将要删除的班级的id，将flag设置为true
                if(item.getId()==id){
                    flag=true;
                    break;
                }
            }
            //flag为true时
            if(flag){
                //检查将要删除的班级是否有学生，若有则不能解散班级
                List<StudentCourseGroup> studentCourseGroups= studentCourseGroupRepository.findByGroupId(id);
                if(studentCourseGroups!=null &&studentCourseGroups.size()>0){
                    throw new Exception("班级存在学生加入信息.");
                }
                //通过班级id找到课程id
                Long courseInfoId=courseGroupRepository.findById(id).get().getCourse().getId();

                //检查课程有没有笔记
                Page<CourseNote> notes=courseNoteRepository.findByCourseId(courseInfoId, PageRequest.of(0,5));
                if(notes.getTotalElements()>0){
                    throw new Exception("班级对应课程存在笔记信息。");
                }
                //检查课程有没有授课内容
                List<CoursePlan> plans=coursePlanRepository.findByCourseId(courseInfoId);
                if(plans!=null && !plans.isEmpty()){
                    throw new Exception("班级对应课程已经存在授课内容信息。");
                }
                courseGroupRepository.deleteById(id);
            }else{//flag为false时
                throw new Exception("无权限解散此班级.");
            }
        }else{
            throw new Exception("无权限解散班级.");
        }
    }
}
