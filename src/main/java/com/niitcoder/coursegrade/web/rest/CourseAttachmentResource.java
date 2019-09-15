package com.niitcoder.coursegrade.web.rest;

import com.niitcoder.coursegrade.domain.CourseAttachment;
import com.niitcoder.coursegrade.service.CourseAttachmentService;
import com.niitcoder.coursegrade.service.dto.FileInfo;
import com.niitcoder.coursegrade.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
 * REST controller for managing {@link com.niitcoder.coursegrade.domain.CourseAttachment}.
 */
@RestController
@RequestMapping("/api")
public class CourseAttachmentResource {

    private final Logger log = LoggerFactory.getLogger(CourseAttachmentResource.class);

    private static final String ENTITY_NAME = "courseAttachment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseAttachmentService courseAttachmentService;

    public CourseAttachmentResource(CourseAttachmentService courseAttachmentService) {
        this.courseAttachmentService = courseAttachmentService;
    }


    @ApiOperation(value="将上传完成的文件添加到附件")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "type",value = "附件的类型"),
        @ApiImplicitParam(name = "entityId",value = "关联实体的id，如笔记id，作业id"),
    })
    @PostMapping("/course-attachments/single")
    public ResponseEntity addCourseAttachment(String type, Long entityId, FileInfo file){
        CourseAttachment result=courseAttachmentService.save(type,entityId,file);
        return  ResponseEntity.ok(result);
    }



    /**
     * {@code DELETE  /course-attachments/:id} : delete the "id" courseAttachment.
     *
     * @param id the id of the courseAttachment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/course-attachments/{id}")
    public ResponseEntity<Void> deleteCourseAttachment(@PathVariable Long id) {
        log.debug("REST request to delete CourseAttachment : {}", id);
        courseAttachmentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
