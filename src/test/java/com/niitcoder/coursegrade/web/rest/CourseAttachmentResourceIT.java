package com.niitcoder.coursegrade.web.rest;

import com.niitcoder.coursegrade.CoursegradeApp;
import com.niitcoder.coursegrade.domain.CourseAttachment;
import com.niitcoder.coursegrade.repository.CourseAttachmentRepository;
import com.niitcoder.coursegrade.service.CourseAttachmentService;
import com.niitcoder.coursegrade.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.niitcoder.coursegrade.web.rest.TestUtil.sameInstant;
import static com.niitcoder.coursegrade.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CourseAttachmentResource} REST controller.
 */
@SpringBootTest(classes = CoursegradeApp.class)
public class CourseAttachmentResourceIT {

    private static final String DEFAULT_ATTACHMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ATTACHMENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ATTACHMENT_USE = "AAAAAAAAAA";
    private static final String UPDATED_ATTACHMENT_USE = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ORIGIN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORIGIN_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final Long DEFAULT_FILE_SIZE = 1L;
    private static final Long UPDATED_FILE_SIZE = 2L;
    private static final Long SMALLER_FILE_SIZE = 1L - 1L;

    private static final ZonedDateTime DEFAULT_UPLOAD_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPLOAD_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPLOAD_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_FILE_USER = "AAAAAAAAAA";
    private static final String UPDATED_FILE_USER = "BBBBBBBBBB";

    @Autowired
    private CourseAttachmentRepository courseAttachmentRepository;

    @Autowired
    private CourseAttachmentService courseAttachmentService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restCourseAttachmentMockMvc;

    private CourseAttachment courseAttachment;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CourseAttachmentResource courseAttachmentResource = new CourseAttachmentResource(courseAttachmentService);
        this.restCourseAttachmentMockMvc = MockMvcBuilders.standaloneSetup(courseAttachmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseAttachment createEntity(EntityManager em) {
        CourseAttachment courseAttachment = new CourseAttachment()
            .attachmentType(DEFAULT_ATTACHMENT_TYPE)
            .attachmentUse(DEFAULT_ATTACHMENT_USE)
            .fileName(DEFAULT_FILE_NAME)
            .originName(DEFAULT_ORIGIN_NAME)
            .filePath(DEFAULT_FILE_PATH)
            .fileSize(DEFAULT_FILE_SIZE)
            .uploadTime(DEFAULT_UPLOAD_TIME)
            .fileUser(DEFAULT_FILE_USER);
        return courseAttachment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseAttachment createUpdatedEntity(EntityManager em) {
        CourseAttachment courseAttachment = new CourseAttachment()
            .attachmentType(UPDATED_ATTACHMENT_TYPE)
            .attachmentUse(UPDATED_ATTACHMENT_USE)
            .fileName(UPDATED_FILE_NAME)
            .originName(UPDATED_ORIGIN_NAME)
            .filePath(UPDATED_FILE_PATH)
            .fileSize(UPDATED_FILE_SIZE)
            .uploadTime(UPDATED_UPLOAD_TIME)
            .fileUser(UPDATED_FILE_USER);
        return courseAttachment;
    }

    @BeforeEach
    public void initTest() {
        courseAttachment = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourseAttachment() throws Exception {
        int databaseSizeBeforeCreate = courseAttachmentRepository.findAll().size();

        // Create the CourseAttachment
        restCourseAttachmentMockMvc.perform(post("/api/course-attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseAttachment)))
            .andExpect(status().isCreated());

        // Validate the CourseAttachment in the database
        List<CourseAttachment> courseAttachmentList = courseAttachmentRepository.findAll();
        assertThat(courseAttachmentList).hasSize(databaseSizeBeforeCreate + 1);
        CourseAttachment testCourseAttachment = courseAttachmentList.get(courseAttachmentList.size() - 1);
        assertThat(testCourseAttachment.getAttachmentType()).isEqualTo(DEFAULT_ATTACHMENT_TYPE);
        assertThat(testCourseAttachment.getAttachmentUse()).isEqualTo(DEFAULT_ATTACHMENT_USE);
        assertThat(testCourseAttachment.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testCourseAttachment.getOriginName()).isEqualTo(DEFAULT_ORIGIN_NAME);
        assertThat(testCourseAttachment.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testCourseAttachment.getFileSize()).isEqualTo(DEFAULT_FILE_SIZE);
        assertThat(testCourseAttachment.getUploadTime()).isEqualTo(DEFAULT_UPLOAD_TIME);
        assertThat(testCourseAttachment.getFileUser()).isEqualTo(DEFAULT_FILE_USER);
    }

    @Test
    @Transactional
    public void createCourseAttachmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseAttachmentRepository.findAll().size();

        // Create the CourseAttachment with an existing ID
        courseAttachment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseAttachmentMockMvc.perform(post("/api/course-attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseAttachment)))
            .andExpect(status().isBadRequest());

        // Validate the CourseAttachment in the database
        List<CourseAttachment> courseAttachmentList = courseAttachmentRepository.findAll();
        assertThat(courseAttachmentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCourseAttachments() throws Exception {
        // Initialize the database
        courseAttachmentRepository.saveAndFlush(courseAttachment);

        // Get all the courseAttachmentList
        restCourseAttachmentMockMvc.perform(get("/api/course-attachments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].attachmentType").value(hasItem(DEFAULT_ATTACHMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].attachmentUse").value(hasItem(DEFAULT_ATTACHMENT_USE.toString())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].originName").value(hasItem(DEFAULT_ORIGIN_NAME.toString())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH.toString())))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].uploadTime").value(hasItem(sameInstant(DEFAULT_UPLOAD_TIME))))
            .andExpect(jsonPath("$.[*].fileUser").value(hasItem(DEFAULT_FILE_USER.toString())));
    }
    
    @Test
    @Transactional
    public void getCourseAttachment() throws Exception {
        // Initialize the database
        courseAttachmentRepository.saveAndFlush(courseAttachment);

        // Get the courseAttachment
        restCourseAttachmentMockMvc.perform(get("/api/course-attachments/{id}", courseAttachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(courseAttachment.getId().intValue()))
            .andExpect(jsonPath("$.attachmentType").value(DEFAULT_ATTACHMENT_TYPE.toString()))
            .andExpect(jsonPath("$.attachmentUse").value(DEFAULT_ATTACHMENT_USE.toString()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME.toString()))
            .andExpect(jsonPath("$.originName").value(DEFAULT_ORIGIN_NAME.toString()))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH.toString()))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.intValue()))
            .andExpect(jsonPath("$.uploadTime").value(sameInstant(DEFAULT_UPLOAD_TIME)))
            .andExpect(jsonPath("$.fileUser").value(DEFAULT_FILE_USER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCourseAttachment() throws Exception {
        // Get the courseAttachment
        restCourseAttachmentMockMvc.perform(get("/api/course-attachments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourseAttachment() throws Exception {
        // Initialize the database
        courseAttachmentService.save(courseAttachment);

        int databaseSizeBeforeUpdate = courseAttachmentRepository.findAll().size();

        // Update the courseAttachment
        CourseAttachment updatedCourseAttachment = courseAttachmentRepository.findById(courseAttachment.getId()).get();
        // Disconnect from session so that the updates on updatedCourseAttachment are not directly saved in db
        em.detach(updatedCourseAttachment);
        updatedCourseAttachment
            .attachmentType(UPDATED_ATTACHMENT_TYPE)
            .attachmentUse(UPDATED_ATTACHMENT_USE)
            .fileName(UPDATED_FILE_NAME)
            .originName(UPDATED_ORIGIN_NAME)
            .filePath(UPDATED_FILE_PATH)
            .fileSize(UPDATED_FILE_SIZE)
            .uploadTime(UPDATED_UPLOAD_TIME)
            .fileUser(UPDATED_FILE_USER);

        restCourseAttachmentMockMvc.perform(put("/api/course-attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCourseAttachment)))
            .andExpect(status().isOk());

        // Validate the CourseAttachment in the database
        List<CourseAttachment> courseAttachmentList = courseAttachmentRepository.findAll();
        assertThat(courseAttachmentList).hasSize(databaseSizeBeforeUpdate);
        CourseAttachment testCourseAttachment = courseAttachmentList.get(courseAttachmentList.size() - 1);
        assertThat(testCourseAttachment.getAttachmentType()).isEqualTo(UPDATED_ATTACHMENT_TYPE);
        assertThat(testCourseAttachment.getAttachmentUse()).isEqualTo(UPDATED_ATTACHMENT_USE);
        assertThat(testCourseAttachment.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testCourseAttachment.getOriginName()).isEqualTo(UPDATED_ORIGIN_NAME);
        assertThat(testCourseAttachment.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testCourseAttachment.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testCourseAttachment.getUploadTime()).isEqualTo(UPDATED_UPLOAD_TIME);
        assertThat(testCourseAttachment.getFileUser()).isEqualTo(UPDATED_FILE_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingCourseAttachment() throws Exception {
        int databaseSizeBeforeUpdate = courseAttachmentRepository.findAll().size();

        // Create the CourseAttachment

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseAttachmentMockMvc.perform(put("/api/course-attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseAttachment)))
            .andExpect(status().isBadRequest());

        // Validate the CourseAttachment in the database
        List<CourseAttachment> courseAttachmentList = courseAttachmentRepository.findAll();
        assertThat(courseAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCourseAttachment() throws Exception {
        // Initialize the database
        courseAttachmentService.save(courseAttachment);

        int databaseSizeBeforeDelete = courseAttachmentRepository.findAll().size();

        // Delete the courseAttachment
        restCourseAttachmentMockMvc.perform(delete("/api/course-attachments/{id}", courseAttachment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CourseAttachment> courseAttachmentList = courseAttachmentRepository.findAll();
        assertThat(courseAttachmentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseAttachment.class);
        CourseAttachment courseAttachment1 = new CourseAttachment();
        courseAttachment1.setId(1L);
        CourseAttachment courseAttachment2 = new CourseAttachment();
        courseAttachment2.setId(courseAttachment1.getId());
        assertThat(courseAttachment1).isEqualTo(courseAttachment2);
        courseAttachment2.setId(2L);
        assertThat(courseAttachment1).isNotEqualTo(courseAttachment2);
        courseAttachment1.setId(null);
        assertThat(courseAttachment1).isNotEqualTo(courseAttachment2);
    }
}
