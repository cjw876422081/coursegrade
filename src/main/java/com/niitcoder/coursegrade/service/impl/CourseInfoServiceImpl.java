package com.niitcoder.coursegrade.service.impl;

import com.niitcoder.coursegrade.domain.CourseGroup;
import com.niitcoder.coursegrade.domain.CourseNote;
import com.niitcoder.coursegrade.domain.CoursePlan;
import com.niitcoder.coursegrade.repository.*;
import com.niitcoder.coursegrade.security.SecurityUtils;
import com.niitcoder.coursegrade.service.CourseInfoService;
import com.niitcoder.coursegrade.domain.CourseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link CourseInfo}.
 */
@Service
@Transactional
public class CourseInfoServiceImpl implements CourseInfoService {

    private final Logger log = LoggerFactory.getLogger(CourseInfoServiceImpl.class);

    private final CourseInfoRepository courseInfoRepository;
    private final CourseGroupRepository courseGroupRepository;
    private final CoursePlanRepository coursePlanRepository;
    private final CourseNoteRepository courseNoteRepository;
    private final CourseHomeworkRepository courseHomeworkRepository;
    private final CourseAttachmentRepository courseAttachmentRepository;
    public CourseInfoServiceImpl(CourseInfoRepository courseInfoRepository, CourseGroupRepository courseGroupRepository, CoursePlanRepository coursePlanRepository, CourseNoteRepository courseNoteRepository, CourseHomeworkRepository courseHomeworkRepository, CourseAttachmentRepository courseAttachmentRepository) {
        this.courseInfoRepository = courseInfoRepository;
        this.courseGroupRepository = courseGroupRepository;
        this.coursePlanRepository = coursePlanRepository;
        this.courseNoteRepository = courseNoteRepository;
        this.courseHomeworkRepository = courseHomeworkRepository;
        this.courseAttachmentRepository = courseAttachmentRepository;
    }

    /**
     * Save a courseInfo.
     *
     * @param courseInfo the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CourseInfo save(CourseInfo courseInfo) {
        log.debug("Request to save CourseInfo : {}", courseInfo);
        String loginName = SecurityUtils.getCurrentUserLogin().get();
        courseInfo.setCourseUser(loginName);
        courseInfo.setDataTime(ZonedDateTime.now());
        return courseInfoRepository.save(courseInfo);
    }

    /**
     * Get all the courseInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CourseInfo> findAll(Pageable pageable) {
        log.debug("Request to get all CourseInfos");
        return courseInfoRepository.findAll(pageable);
    }


    /**
     * Delete the courseInfo by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) throws Exception {
        log.debug("Request to delete CourseInfo : {}", id);
        //当前id对应的课程是否是当前登录用户的
        Optional<CourseInfo> courseInfo=courseInfoRepository.findById(id);
        if(courseInfo.isPresent()){ //id可以查询到课程
            CourseInfo course = courseInfo.get();
            String loginName= SecurityUtils.getCurrentUserLogin().get();
            if(!course.getCourseUser().equals(loginName)){
                throw new Exception("无权限删除此课程.");
            }
            //检查课程有没有开班
            List<CourseGroup> groups = courseGroupRepository.findByCourseId(id);
            if(groups!=null && !groups.isEmpty()){
                throw new Exception("课程已经存在开班信息。");
            }
            //检查课程有没有授课内容
            List<CoursePlan> plans=coursePlanRepository.findByCourseId(id);
            if(plans!=null && !plans.isEmpty()){
                throw new Exception("课程已经存在授课内容信息。");
            }
            //检查课程有没有笔记
            Page<CourseNote> notes=courseNoteRepository.findByCourseId(id, PageRequest.of(0,5));
            if(notes.getTotalElements()>0){
                throw new Exception("课程已经存在笔记信息。");
            }

            courseInfoRepository.deleteById(id);

        }else{ //id对应的课程不存在
            throw new Exception("课程不存在");
        }
    }

    @Override
    public Page<CourseInfo> findByLogin(String login, Pageable page) {
        return courseInfoRepository.findByCourseUser(login,page);
    }

    @Override
    public CourseInfo findById(Long id) throws Exception {
        Optional<CourseInfo> courseInfo=courseInfoRepository.findById(id);
        if(courseInfo.isPresent()){
            return courseInfo.get();
        }else{
            throw new Exception("未找到课程");
        }
    }

    /**
     * 检查登陆用户是否创建过课程
     * @return
     * @throws Exception
     */
    public boolean checkLoginName() throws Exception {
        String loginName = SecurityUtils.getCurrentUserLogin().get();
        List<CourseInfo> courseInfos=courseInfoRepository.findByCourseUser(loginName);
        //检查用户是否创建过课程,若有,则设置flag为true
        if(courseInfos!=null && courseInfos.size()>0){
            return true;
        }
        return false;
    }

    /**
     * 检查课程是否存在
     * @param courseInfoId
     * @return
     * @throws Exception
     */
    public CourseInfo isExistCourseInfo(Long courseInfoId) throws Exception {

        Optional<CourseInfo> courseInfo=courseInfoRepository.findById(courseInfoId);
        //检查课程是否存在，存在，则返回查到的课程，不存在，则抛出异常
        if(courseInfo.isPresent()){
            return courseInfo.get();
        }else{
            throw new Exception("课程不存在");
        }
    }

    /**
     * 检查课程是否由该用户创建
     * @param courseInfoId
     * @return
     * @throws Exception
     */
    public boolean isCreateByLogin(Long courseInfoId) throws Exception {
        String loginName = SecurityUtils.getCurrentUserLogin().get();
        //检查课程是否存在
        CourseInfo courseInfo=isExistCourseInfo(courseInfoId);
        //检查课程是否由该用户创建，若是，则设置flag为true
        if(courseInfo.getCourseUser()!=null
            &&courseInfo.getCourseUser().equals(loginName)){
            return true;
        }
        return false;
    }
}
