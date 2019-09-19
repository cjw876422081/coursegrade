package com.niitcoder.coursegrade.web.rest;

import com.niitcoder.coursegrade.domain.CourseInfo;
import com.niitcoder.coursegrade.security.SecurityUtils;
import com.niitcoder.coursegrade.service.CourseInfoService;
import com.niitcoder.coursegrade.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.niitcoder.coursegrade.domain.CourseInfo}.
 */
@RestController
@RequestMapping("/api")
public class CourseInfoResource {

    private final Logger log = LoggerFactory.getLogger(CourseInfoResource.class);

    private static final String ENTITY_NAME = "courseInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseInfoService courseInfoService;

    public CourseInfoResource(CourseInfoService courseInfoService) {

        this.courseInfoService = courseInfoService;
    }

    @ApiOperation(value="教师创建新课程")
    @PostMapping("/course-infos")
    public ResponseEntity<CourseInfo> createCourseInfo(@RequestBody CourseInfo courseInfo) throws URISyntaxException {
        log.debug("REST request to save CourseInfo : {}", courseInfo);
        if (courseInfo.getId() != null) {
            throw new BadRequestAlertException("A new courseInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseInfo result = courseInfoService.save(courseInfo);
        return ResponseEntity.created(new URI("/api/course-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }



    @ApiOperation("根据当前登录的教师，获取教师已创建的课程")
    @GetMapping("/course-infos/teacher")
    public ResponseEntity getAllCourseInfos(Pageable pageable) {
        log.debug("REST request to get a page of CourseInfos");
        String loginName= SecurityUtils.getCurrentUserLogin().get();
        Page<CourseInfo> page = courseInfoService.findByLogin(loginName, pageable);
        return ResponseEntity.ok(page);
    }

    @ApiOperation(value="根据课程编码获取课程信息")
    @GetMapping("/course-infos/{id}")
    public ResponseEntity<CourseInfo> getCourseInfo(@PathVariable Long id) {
        log.debug("REST request to get CourseInfo : {}", id);
        CourseInfo courseInfo = null;
        try {
            courseInfo = courseInfoService.findById(id);
            return ResponseEntity.ok(courseInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "not found");
        }

    }

    @ApiOperation(value="删除已创建课程")
    @DeleteMapping("/course-infos/{id}")
    public ResponseEntity<Void> deleteCourseInfo(@PathVariable Long id) {
        log.debug("REST request to delete CourseInfo : {}", id);
        try {
            courseInfoService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "group exists");
        }
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
