package com.niitcoder.coursegrade.web.rest;

import com.niitcoder.coursegrade.CoursegradeApp;
import com.niitcoder.coursegrade.domain.StudentHomework;
import com.niitcoder.coursegrade.repository.StudentHomeworkRepository;
import com.niitcoder.coursegrade.service.CourseAttachmentService;
import com.niitcoder.coursegrade.service.StudentHomeworkService;
import com.niitcoder.coursegrade.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
 * Integration tests for the {@link StudentHomeworkResource} REST controller.
 */
@SpringBootTest(classes = CoursegradeApp.class)
public class StudentHomeworkResourceIT {

    private static final String DEFAULT_SUBMIT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_SUBMIT_MEMO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_SUBMIT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SUBMIT_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_SUBMIT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_READ_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_READ_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_READ_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_GRADE = 1;
    private static final Integer UPDATED_GRADE = 2;
    private static final Integer SMALLER_GRADE = 1 - 1;
    
    private static final String DEFAULT_STUDENT = "AAAAAAAAAA";
    private static final String UPDATED_STUDENT = "BBBBBBBBBB";

    private static final String DEFAULT_TEACHER = "AAAAAAAAAA";
    private static final String UPDATED_TEACHER = "BBBBBBBBBB";

    @Autowired
    private StudentHomeworkRepository studentHomeworkRepository;

    @Autowired
    private StudentHomeworkService studentHomeworkService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;


    @Qualifier("")
    @Autowired
    private Validator validator;

    private MockMvc restStudentHomeworkMockMvc;

    private StudentHomework studentHomework;

    private CourseAttachmentService courseAttachmentService;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StudentHomeworkResource studentHomeworkResource = new StudentHomeworkResource(studentHomeworkService, courseAttachmentService);
        this.restStudentHomeworkMockMvc = MockMvcBuilders.standaloneSetup(studentHomeworkResource)
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
    public static StudentHomework createEntity(EntityManager em) {
        StudentHomework studentHomework = new StudentHomework()
            .submitMemo(DEFAULT_SUBMIT_MEMO)
            .submitTime(DEFAULT_SUBMIT_TIME)
            .readTime(DEFAULT_READ_TIME)
            .grade(DEFAULT_GRADE)
            .student(DEFAULT_STUDENT)
            .teacher(DEFAULT_TEACHER);
        return studentHomework;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentHomework createUpdatedEntity(EntityManager em) {
        StudentHomework studentHomework = new StudentHomework()
            .submitMemo(UPDATED_SUBMIT_MEMO)
            .submitTime(UPDATED_SUBMIT_TIME)
            .readTime(UPDATED_READ_TIME)
            .grade(UPDATED_GRADE)
            .student(UPDATED_STUDENT)
            .teacher(UPDATED_TEACHER);
        return studentHomework;
    }

    @BeforeEach
    public void initTest() {
        studentHomework = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudentHomework() throws Exception {
        int databaseSizeBeforeCreate = studentHomeworkRepository.findAll().size();

        // Create the StudentHomework
        restStudentHomeworkMockMvc.perform(post("/api/student-homeworks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentHomework)))
            .andExpect(status().isCreated());

        // Validate the StudentHomework in the database
        List<StudentHomework> studentHomeworkList = studentHomeworkRepository.findAll();
        assertThat(studentHomeworkList).hasSize(databaseSizeBeforeCreate + 1);
        StudentHomework testStudentHomework = studentHomeworkList.get(studentHomeworkList.size() - 1);
        assertThat(testStudentHomework.getSubmitMemo()).isEqualTo(DEFAULT_SUBMIT_MEMO);
        assertThat(testStudentHomework.getSubmitTime()).isEqualTo(DEFAULT_SUBMIT_TIME);
        assertThat(testStudentHomework.getReadTime()).isEqualTo(DEFAULT_READ_TIME);
        assertThat(testStudentHomework.getGrade()).isEqualTo(DEFAULT_GRADE);
        assertThat(testStudentHomework.getStudent()).isEqualTo(DEFAULT_STUDENT);
        assertThat(testStudentHomework.getTeacher()).isEqualTo(DEFAULT_TEACHER);
    }

    @Test
    @Transactional
    public void createStudentHomeworkWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studentHomeworkRepository.findAll().size();

        // Create the StudentHomework with an existing ID
        studentHomework.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentHomeworkMockMvc.perform(post("/api/student-homeworks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentHomework)))
            .andExpect(status().isBadRequest());

        // Validate the StudentHomework in the database
        List<StudentHomework> studentHomeworkList = studentHomeworkRepository.findAll();
        assertThat(studentHomeworkList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllStudentHomeworks() throws Exception {
        // Initialize the database
        studentHomeworkRepository.saveAndFlush(studentHomework);

        // Get all the studentHomeworkList
        restStudentHomeworkMockMvc.perform(get("/api/student-homeworks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentHomework.getId().intValue())))
            .andExpect(jsonPath("$.[*].submitMemo").value(hasItem(DEFAULT_SUBMIT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].submitTime").value(hasItem(sameInstant(DEFAULT_SUBMIT_TIME))))
            .andExpect(jsonPath("$.[*].readTime").value(hasItem(sameInstant(DEFAULT_READ_TIME))))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE)))
            .andExpect(jsonPath("$.[*].student").value(hasItem(DEFAULT_STUDENT.toString())))
            .andExpect(jsonPath("$.[*].teacher").value(hasItem(DEFAULT_TEACHER.toString())));
    }

    @Test
    @Transactional
    public void getStudentHomework() throws Exception {
        // Initialize the database
        studentHomeworkRepository.saveAndFlush(studentHomework);

        // Get the studentHomework
        restStudentHomeworkMockMvc.perform(get("/api/student-homeworks/{id}", studentHomework.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(studentHomework.getId().intValue()))
            .andExpect(jsonPath("$.submitMemo").value(DEFAULT_SUBMIT_MEMO.toString()))
            .andExpect(jsonPath("$.submitTime").value(sameInstant(DEFAULT_SUBMIT_TIME)))
            .andExpect(jsonPath("$.readTime").value(sameInstant(DEFAULT_READ_TIME)))
            .andExpect(jsonPath("$.grade").value(DEFAULT_GRADE))
            .andExpect(jsonPath("$.student").value(DEFAULT_STUDENT.toString()))
            .andExpect(jsonPath("$.teacher").value(DEFAULT_TEACHER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStudentHomework() throws Exception {
        // Get the studentHomework
        restStudentHomeworkMockMvc.perform(get("/api/student-homeworks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudentHomework() throws Exception {
        // Initialize the database
        studentHomeworkService.save(studentHomework);

        int databaseSizeBeforeUpdate = studentHomeworkRepository.findAll().size();

        // Update the studentHomework
        StudentHomework updatedStudentHomework = studentHomeworkRepository.findById(studentHomework.getId()).get();
        // Disconnect from session so that the updates on updatedStudentHomework are not directly saved in db
        em.detach(updatedStudentHomework);
        updatedStudentHomework
            .submitMemo(UPDATED_SUBMIT_MEMO)
            .submitTime(UPDATED_SUBMIT_TIME)
            .readTime(UPDATED_READ_TIME)
            .grade(UPDATED_GRADE)
            .student(UPDATED_STUDENT)
            .teacher(UPDATED_TEACHER);

        restStudentHomeworkMockMvc.perform(put("/api/student-homeworks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStudentHomework)))
            .andExpect(status().isOk());

        // Validate the StudentHomework in the database
        List<StudentHomework> studentHomeworkList = studentHomeworkRepository.findAll();
        assertThat(studentHomeworkList).hasSize(databaseSizeBeforeUpdate);
        StudentHomework testStudentHomework = studentHomeworkList.get(studentHomeworkList.size() - 1);
        assertThat(testStudentHomework.getSubmitMemo()).isEqualTo(UPDATED_SUBMIT_MEMO);
        assertThat(testStudentHomework.getSubmitTime()).isEqualTo(UPDATED_SUBMIT_TIME);
        assertThat(testStudentHomework.getReadTime()).isEqualTo(UPDATED_READ_TIME);
        assertThat(testStudentHomework.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testStudentHomework.getStudent()).isEqualTo(UPDATED_STUDENT);
        assertThat(testStudentHomework.getTeacher()).isEqualTo(UPDATED_TEACHER);
    }

    @Test
    @Transactional
    public void updateNonExistingStudentHomework() throws Exception {
        int databaseSizeBeforeUpdate = studentHomeworkRepository.findAll().size();

        // Create the StudentHomework

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentHomeworkMockMvc.perform(put("/api/student-homeworks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentHomework)))
            .andExpect(status().isBadRequest());

        // Validate the StudentHomework in the database
        List<StudentHomework> studentHomeworkList = studentHomeworkRepository.findAll();
        assertThat(studentHomeworkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStudentHomework() throws Exception {
        // Initialize the database
        studentHomeworkService.save(studentHomework);

        int databaseSizeBeforeDelete = studentHomeworkRepository.findAll().size();

        // Delete the studentHomework
        restStudentHomeworkMockMvc.perform(delete("/api/student-homeworks/{id}", studentHomework.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudentHomework> studentHomeworkList = studentHomeworkRepository.findAll();
        assertThat(studentHomeworkList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentHomework.class);
        StudentHomework studentHomework1 = new StudentHomework();
        studentHomework1.setId(1L);
        StudentHomework studentHomework2 = new StudentHomework();
        studentHomework2.setId(studentHomework1.getId());
        assertThat(studentHomework1).isEqualTo(studentHomework2);
        studentHomework2.setId(2L);
        assertThat(studentHomework1).isNotEqualTo(studentHomework2);
        studentHomework1.setId(null);
        assertThat(studentHomework1).isNotEqualTo(studentHomework2);
    }
}
