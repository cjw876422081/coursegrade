package com.niitcoder.coursegrade.web.rest;

import com.niitcoder.coursegrade.domain.CourseGroup;
import com.niitcoder.coursegrade.service.CourseGroupService;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.niitcoder.coursegrade.domain.CourseGroup}.
 */
@RestController
@RequestMapping("/api")
public class CourseGroupResource {

    private final Logger log = LoggerFactory.getLogger(CourseGroupResource.class);

    private static final String ENTITY_NAME = "courseGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseGroupService courseGroupService;

    public CourseGroupResource(CourseGroupService courseGroupService) {
        this.courseGroupService = courseGroupService;
    }

    /**
     * {@code POST  /course-groups} : Create a new courseGroup.
     *
     * @param courseGroup the courseGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseGroup, or with status {@code 400 (Bad Request)} if the courseGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/course-groups")
    public ResponseEntity<CourseGroup> createCourseGroup(@RequestBody CourseGroup courseGroup) throws URISyntaxException {
        log.debug("REST request to save CourseGroup : {}", courseGroup);
        if (courseGroup.getId() != null) {
            throw new BadRequestAlertException("A new courseGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseGroup result = courseGroupService.save(courseGroup);
        return ResponseEntity.created(new URI("/api/course-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /course-groups} : Updates an existing courseGroup.
     *
     * @param courseGroup the courseGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseGroup,
     * or with status {@code 400 (Bad Request)} if the courseGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/course-groups")
    public ResponseEntity<CourseGroup> updateCourseGroup(@RequestBody CourseGroup courseGroup) throws URISyntaxException {
        log.debug("REST request to update CourseGroup : {}", courseGroup);
        if (courseGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CourseGroup result = courseGroupService.save(courseGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courseGroup.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /course-groups} : get all the courseGroups.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courseGroups in body.
     */
    @GetMapping("/course-groups")
    public ResponseEntity<List<CourseGroup>> getAllCourseGroups(Pageable pageable) {
        log.debug("REST request to get a page of CourseGroups");
        Page<CourseGroup> page = courseGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /course-groups/:id} : get the "id" courseGroup.
     *
     * @param id the id of the courseGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/course-groups/{id}")
    public ResponseEntity<CourseGroup> getCourseGroup(@PathVariable Long id) {
        log.debug("REST request to get CourseGroup : {}", id);
        Optional<CourseGroup> courseGroup = courseGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courseGroup);
    }

    /**
     * {@code DELETE  /course-groups/:id} : delete the "id" courseGroup.
     *
     * @param id the id of the courseGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/course-groups/{id}")
    public ResponseEntity<Void> deleteCourseGroup(@PathVariable Long id) {
        log.debug("REST request to delete CourseGroup : {}", id);
        courseGroupService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
