package com.niitcoder.coursegrade.web.rest;

import com.niitcoder.coursegrade.domain.CourseNote;
import com.niitcoder.coursegrade.security.SecurityUtils;
import com.niitcoder.coursegrade.service.CourseNoteService;
import com.niitcoder.coursegrade.service.dto.CourseNoteDTO;
import com.niitcoder.coursegrade.service.dto.CourseNoteType;
import com.niitcoder.coursegrade.service.dto.NotePostItem;
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

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.niitcoder.coursegrade.domain.CourseNote}.
 */
@RestController
@RequestMapping("/api")
public class CourseNoteResource {

    private final Logger log = LoggerFactory.getLogger(CourseNoteResource.class);

    private static final String ENTITY_NAME = "courseNote";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseNoteService courseNoteService;

    public CourseNoteResource(CourseNoteService courseNoteService) {
        this.courseNoteService = courseNoteService;
    }

    @ApiOperation(value="添加笔记")
    @PostMapping("/course-notes")
    public ResponseEntity<CourseNote> createCourseNote(@RequestBody NotePostItem courseNote) throws URISyntaxException {
        log.debug("REST request to save CourseNote : {}", courseNote);
        if (courseNote.getId() != null) {
            throw new BadRequestAlertException("A new courseNote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseNote note=new CourseNote();
        note.setPublishUser(SecurityUtils.getCurrentUserLogin().get());
        note.setCourse(courseNote.getCourse());
        note.setHomework(courseNote.getHomework());
        note.setNoteMemo(courseNote.getNoteMemo());
        note.setNoteTime(ZonedDateTime.now());
        note.setNoteType(courseNote.getNoteType());
        note.setParentNote(courseNote.getParentNote());
        note.setPlan(courseNote.getPlan());

        CourseNote result = courseNoteService.save(note,courseNote.getFiles());
        return ResponseEntity.created(new URI("/api/course-notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    @ApiOperation(value="分页获取单个类型的笔记")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "type",value = "笔记类型：课程，授课内容，作业"),
        @ApiImplicitParam(name = "entityId",value = "对应的类型元素id，如课程id,授课内容id，作业id")
    })
    @GetMapping("/course-notes/type")
    public ResponseEntity<Page<CourseNoteDTO>> getAllCourseNotes(@RequestParam String type,@RequestParam Long entityId,Pageable pageable) {
        Page<CourseNote> notes=null;
        if(type.equalsIgnoreCase(CourseNoteType.COURSE)){
            notes=courseNoteService.findRootNoteByCourse(entityId,pageable);
        }else if(type.equalsIgnoreCase(CourseNoteType.PLAN)){
            notes=courseNoteService.findRootNoteByCoursePlan(entityId,pageable);
        }else if(type.equalsIgnoreCase(CourseNoteType.HOMEWORK)){
            notes=courseNoteService.findRootNoteByHomewrok(entityId,pageable);
        }

        Page<CourseNoteDTO> result=courseNoteService.getNoteTreeList(notes);
        return ResponseEntity.ok(result);
    }



    @ApiOperation(value="获取单个笔记及全部回复")
    @ApiImplicitParam(name = "id",value = "笔记编号")
    @GetMapping("/course-notes/{id}")
    public ResponseEntity<CourseNoteDTO> getCourseNote(@PathVariable Long id) {
        CourseNoteDTO result=courseNoteService.getNoteItemById(id);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="删除单个笔记")
    @ApiImplicitParam(name = "id",value = "笔记编号")
    @DeleteMapping("/course-notes/{id}")
    public ResponseEntity<Void> deleteCourseNote(@PathVariable Long id) {
        log.debug("REST request to delete CourseNote : {}", id);
        courseNoteService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
