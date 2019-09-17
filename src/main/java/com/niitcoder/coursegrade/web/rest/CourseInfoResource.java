package com.niitcoder.coursegrade.web.rest;

import com.niitcoder.coursegrade.domain.CourseInfo;
import com.niitcoder.coursegrade.service.CourseInfoService;
import com.niitcoder.coursegrade.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
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

    /**
     * {@code POST  /course-infos} : Create a new courseInfo.
     *
     * @param courseInfo the courseInfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseInfo, or with status {@code 400 (Bad Request)} if the courseInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
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

    /**
     * {@code PUT  /course-infos} : Updates an existing courseInfo.
     *
     * @param courseInfo the courseInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseInfo,
     * or with status {@code 400 (Bad Request)} if the courseInfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/course-infos")
    public ResponseEntity<CourseInfo> updateCourseInfo(@RequestBody CourseInfo courseInfo) throws URISyntaxException {
        log.debug("REST request to update CourseInfo : {}", courseInfo);
        if (courseInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CourseInfo result = courseInfoService.save(courseInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courseInfo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /course-infos} : get all the courseInfos.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courseInfos in body.
     */
    @GetMapping("/course-infos")
    public ResponseEntity<List<CourseInfo>> getAllCourseInfos(Pageable pageable) {
        log.debug("REST request to get a page of CourseInfos");
        Page<CourseInfo> page = courseInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /course-infos/:id} : get the "id" courseInfo.
     *
     * @param id the id of the courseInfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseInfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/course-infos/{id}")
    public ResponseEntity<CourseInfo> getCourseInfo(@PathVariable Long id) {
        log.debug("REST request to get CourseInfo : {}", id);
        Optional<CourseInfo> courseInfo = courseInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courseInfo);
    }

    /**
     * {@code DELETE  /course-infos/:id} : delete the "id" courseInfo.
     *
     * @param id the id of the courseInfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/course-infos/{id}")
    public ResponseEntity<Void> deleteCourseInfo(@PathVariable Long id) {
        log.debug("REST request to delete CourseInfo : {}", id);
        courseInfoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
