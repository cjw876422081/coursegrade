package com.niitcoder.coursegrade.service.impl;

import com.niitcoder.coursegrade.domain.*;
import com.niitcoder.coursegrade.repository.*;
import com.niitcoder.coursegrade.security.SecurityUtils;
import com.niitcoder.coursegrade.service.CourseGroupService;
import com.niitcoder.coursegrade.service.CourseInfoService;
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
 * Service Implementation for managing {@link CourseGroup}.
 */
@Service
@Transactional
public class CourseGroupServiceImpl implements CourseGroupService {

    private final Logger log = LoggerFactory.getLogger(CourseGroupServiceImpl.class);

    private final CourseGroupRepository courseGroupRepository;
    private final StudentCourseGroupRepository studentCourseGroupRepository;
    private final CourseInfoRepository courseInfoRepository;
    private final CourseInfoService courseInfoService;

    public CourseGroupServiceImpl(CourseGroupRepository courseGroupRepository, StudentCourseGroupRepository studentCourseGroupRepository,CourseInfoRepository courseInfoRepository, CourseInfoService courseInfoService) {
        this.courseGroupRepository = courseGroupRepository;
        this.studentCourseGroupRepository = studentCourseGroupRepository;
        this.courseInfoRepository = courseInfoRepository;
        this.courseInfoService = courseInfoService;
    }

    /**
     * 检查班级是否存在,存在，则返回查到的班级，不存在，则抛出异常
     * @param id
     * @return
     * @throws Exception
     */
    public CourseGroup isExistCourseGroup(Long id) throws Exception {/*Info改为Group*/
        //检查班级是否存在
        Optional<CourseGroup> courseGroup=courseGroupRepository.findById(id);
        if(!courseGroup.isPresent()){
            throw new Exception("班级不存在.");
        }
        return courseGroup.get();
    }
    /**
     * Save a courseGroup.
     *
     * @param courseGroup the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CourseGroup save(CourseGroup courseGroup,Long courseInfoId) throws Exception {
        log.debug("Request to save CourseGroup : {}", courseGroup);
        //检查课程是否由该用户创建，若不是，则无权限创建和更改此课程下的班级
        if(!courseInfoService.isCreateByLogin(courseInfoId)){
            throw new Exception("无权限创建和更改此班级.");
        }
        courseGroup.setDataTime(ZonedDateTime.now());
        CourseInfo courseInfo=new CourseInfo();
        courseInfo.setId(courseInfoId);
        courseGroup.setCourse(courseInfo);
        return courseGroupRepository.save(courseGroup);
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
        //检查用户是否创建过课程，若没有，则无查询班级权限
        if(!courseInfoService.checkLoginName()){
            throw new Exception("无权限查询班级.");
        }
        String loginName = SecurityUtils.getCurrentUserLogin().get();
        return courseGroupRepository.findByCourseCourseUser(loginName);
    }

    /**
     * 通过课程ID检索开设的班级
     * @param id
     * @return
     */
    @Override
    public List<CourseGroup> findByCourseId(Long id) throws Exception {
        log.debug("Request to findByCourseName  : {}", id);
        //检查课程是否由该用户创建，若不是，则无权限查询此课程下的班级
        if(!courseInfoService.isCreateByLogin(id)){
            throw new Exception("无权限查询此课程下的班级");
        }
        return courseGroupRepository.findByCourseId(id);
    }

    public boolean isCreateByLogin(CourseGroup courseGroup,String loginName){
        if(courseGroup.getCourse()!=null
            && courseGroup.getCourse().getCourseUser()!=null
            &&!courseGroup.getCourse().getCourseUser().equals(loginName)){
            return true;
        }
        return false;
    }

    /**
     * Get one courseGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CourseGroup> findOne(Long id) throws Exception {
        log.debug("Request to get CourseGroup : {}", id);
        Optional<CourseGroup> result=courseGroupRepository.findById(id);
        if(result.isPresent()){
            //检查班级是否由该用户创建
            String loginName = SecurityUtils.getCurrentUserLogin().get();
            CourseGroup courseGroup=result.get();
            if(!isCreateByLogin(courseGroup,loginName)){
                throw new Exception("无权限查询此班级.");
            }

        }
        return result;
    }
    /**
     * Delete the courseGroup by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) throws Exception {
        log.debug("Request to delete CourseGroup : {}", id);
        //检查班级是否存在
        CourseGroup courseGroup=isExistCourseGroup(id);/*Info改为Group*/
        //检查班级是否由该用户创建
        String loginName = SecurityUtils.getCurrentUserLogin().get();
        if(!isCreateByLogin(courseGroup,loginName)){
            throw new Exception("无权限删除此班级.");
        }
        //检查将要删除的班级是否有学生，若有则不能解散班级
        List<StudentCourseGroup> studentCourseGroups= studentCourseGroupRepository.findByGroupId(id);
        if(studentCourseGroups!=null &&studentCourseGroups.size()>0){
            throw new Exception("班级存在学生信息.");
        }
        courseGroupRepository.deleteById(id);
    }
}
