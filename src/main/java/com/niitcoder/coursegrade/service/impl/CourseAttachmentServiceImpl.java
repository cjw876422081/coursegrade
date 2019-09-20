package com.niitcoder.coursegrade.service.impl;

import com.niitcoder.coursegrade.domain.CourseNote;
import com.niitcoder.coursegrade.domain.StudentHomework;
import com.niitcoder.coursegrade.repository.CourseHomeworkRepository;
import com.niitcoder.coursegrade.repository.CourseNoteRepository;
import com.niitcoder.coursegrade.repository.StudentHomeworkRepository;
import com.niitcoder.coursegrade.security.SecurityUtils;
import com.niitcoder.coursegrade.service.CourseAttachmentService;
import com.niitcoder.coursegrade.domain.CourseAttachment;
import com.niitcoder.coursegrade.repository.CourseAttachmentRepository;
import com.niitcoder.coursegrade.service.dto.CourseNoteType;
import com.niitcoder.coursegrade.service.dto.FileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link CourseAttachment}.
 */
@Service
@Transactional
public class CourseAttachmentServiceImpl implements CourseAttachmentService {
    private final Logger log = LoggerFactory.getLogger(CourseAttachmentServiceImpl.class);

    private final CourseAttachmentRepository courseAttachmentRepository;


    public CourseAttachmentServiceImpl(CourseAttachmentRepository courseAttachmentRepository, CourseNoteRepository courseNoteRepository, CourseHomeworkRepository courseHomeworkRepository, StudentHomeworkRepository studentHomeworkRepository) {
        this.courseAttachmentRepository = courseAttachmentRepository;
    }

    /**
     * Save a courseAttachment.
     *
     * @param courseAttachment the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CourseAttachment save(CourseAttachment courseAttachment) {
        log.debug("Request to save CourseAttachment : {}", courseAttachment);
        return courseAttachmentRepository.save(courseAttachment);
    }

    @Override
    public CourseAttachment save(String type, Long entity, FileInfo fileInfo) {
        CourseAttachment courseAttachment = new CourseAttachment();
        courseAttachment.setAttachmentType(type);
        courseAttachment.setFileName(fileInfo.getFullPath());
        courseAttachment.setFilePath(fileInfo.getPath());
        courseAttachment.setFileUser(SecurityUtils.getCurrentUserLogin().get());
        courseAttachment.setFileSize(fileInfo.getSize());
        courseAttachment.setOriginName(fileInfo.getFileName() + "." + fileInfo.getFileExtName());
        courseAttachment.setUploadTime(ZonedDateTime.now());
        //根据类型创建对象
        if (type.equalsIgnoreCase(CourseNoteType.NOTE)) {
            CourseNote note = new CourseNote();
            note.setId(entity);
            courseAttachment.setNote(note);

        } else if (type.equalsIgnoreCase(CourseNoteType.HOMEWORK)) {
            StudentHomework homework = new StudentHomework();
            homework.setId(entity);
            courseAttachment.setHomework(homework);
        }
        courseAttachment = courseAttachmentRepository.save(courseAttachment);
        return courseAttachment;
    }

    /**
     * Get all the courseAttachments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CourseAttachment> findAll(Pageable pageable) {
        log.debug("Request to get all CourseAttachments");
        return courseAttachmentRepository.findAll(pageable);
    }


    /**
     * Get one courseAttachment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CourseAttachment> findOne(Long id) {
        log.debug("Request to get CourseAttachment : {}", id);
        return courseAttachmentRepository.findById(id);
    }

    /**
     * Delete the courseAttachment by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseAttachment : {}", id);
        courseAttachmentRepository.deleteById(id);
    }

    @Override
    public void deleteByFileUserAndHomeworkId(String loginName, Long homeworkId) {

    }

    @Override
    public Optional<List<CourseAttachment>> getCourseAttachmentsByFileUserAndHomeworkId(String loginName, Long homeworkId) {
        return Optional.empty();
    }
}

