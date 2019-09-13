package com.niitcoder.coursegrade.web.rest;

import com.niitcoder.coursegrade.CoursegradeApp;
import com.niitcoder.coursegrade.domain.StudentCourseGroup;
import com.niitcoder.coursegrade.repository.StudentCourseGroupRepository;
import com.niitcoder.coursegrade.service.StudentCourseGroupService;
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
 * Integration tests for the {@link StudentCourseGroupResource} REST controller.
 */
@SpringBootTest(classes = CoursegradeApp.class)
public class StudentCourseGroupResourceIT {

    private static final String DEFAULT_STUDENT = "AAAAAAAAAA";
    private static final String UPDATED_STUDENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_JOIN_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_JOIN_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_JOIN_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private StudentCourseGroupRepository studentCourseGroupRepository;

    @Autowired
    private StudentCourseGroupService studentCourseGroupService;

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

    private MockMvc restStudentCourseGroupMockMvc;

    private StudentCourseGroup studentCourseGroup;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StudentCourseGroupResource studentCourseGroupResource = new StudentCourseGroupResource(studentCourseGroupService);
        this.restStudentCourseGroupMockMvc = MockMvcBuilders.standaloneSetup(studentCourseGroupResource)
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
    public static StudentCourseGroup createEntity(EntityManager em) {
        StudentCourseGroup studentCourseGroup = new StudentCourseGroup()
            .student(DEFAULT_STUDENT)
            .joinTime(DEFAULT_JOIN_TIME);
        return studentCourseGroup;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentCourseGroup createUpdatedEntity(EntityManager em) {
        StudentCourseGroup studentCourseGroup = new StudentCourseGroup()
            .student(UPDATED_STUDENT)
            .joinTime(UPDATED_JOIN_TIME);
        return studentCourseGroup;
    }

    @BeforeEach
    public void initTest() {
        studentCourseGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudentCourseGroup() throws Exception {
        int databaseSizeBeforeCreate = studentCourseGroupRepository.findAll().size();

        // Create the StudentCourseGroup
        restStudentCourseGroupMockMvc.perform(post("/api/student-course-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentCourseGroup)))
            .andExpect(status().isCreated());

        // Validate the StudentCourseGroup in the database
        List<StudentCourseGroup> studentCourseGroupList = studentCourseGroupRepository.findAll();
        assertThat(studentCourseGroupList).hasSize(databaseSizeBeforeCreate + 1);
        StudentCourseGroup testStudentCourseGroup = studentCourseGroupList.get(studentCourseGroupList.size() - 1);
        assertThat(testStudentCourseGroup.getStudent()).isEqualTo(DEFAULT_STUDENT);
        assertThat(testStudentCourseGroup.getJoinTime()).isEqualTo(DEFAULT_JOIN_TIME);
    }

    @Test
    @Transactional
    public void createStudentCourseGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studentCourseGroupRepository.findAll().size();

        // Create the StudentCourseGroup with an existing ID
        studentCourseGroup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentCourseGroupMockMvc.perform(post("/api/student-course-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentCourseGroup)))
            .andExpect(status().isBadRequest());

        // Validate the StudentCourseGroup in the database
        List<StudentCourseGroup> studentCourseGroupList = studentCourseGroupRepository.findAll();
        assertThat(studentCourseGroupList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllStudentCourseGroups() throws Exception {
        // Initialize the database
        studentCourseGroupRepository.saveAndFlush(studentCourseGroup);

        // Get all the studentCourseGroupList
        restStudentCourseGroupMockMvc.perform(get("/api/student-course-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentCourseGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].student").value(hasItem(DEFAULT_STUDENT.toString())))
            .andExpect(jsonPath("$.[*].joinTime").value(hasItem(sameInstant(DEFAULT_JOIN_TIME))));
    }
    
    @Test
    @Transactional
    public void getStudentCourseGroup() throws Exception {
        // Initialize the database
        studentCourseGroupRepository.saveAndFlush(studentCourseGroup);

        // Get the studentCourseGroup
        restStudentCourseGroupMockMvc.perform(get("/api/student-course-groups/{id}", studentCourseGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(studentCourseGroup.getId().intValue()))
            .andExpect(jsonPath("$.student").value(DEFAULT_STUDENT.toString()))
            .andExpect(jsonPath("$.joinTime").value(sameInstant(DEFAULT_JOIN_TIME)));
    }

    @Test
    @Transactional
    public void getNonExistingStudentCourseGroup() throws Exception {
        // Get the studentCourseGroup
        restStudentCourseGroupMockMvc.perform(get("/api/student-course-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudentCourseGroup() throws Exception {
        // Initialize the database
        studentCourseGroupService.save(studentCourseGroup);

        int databaseSizeBeforeUpdate = studentCourseGroupRepository.findAll().size();

        // Update the studentCourseGroup
        StudentCourseGroup updatedStudentCourseGroup = studentCourseGroupRepository.findById(studentCourseGroup.getId()).get();
        // Disconnect from session so that the updates on updatedStudentCourseGroup are not directly saved in db
        em.detach(updatedStudentCourseGroup);
        updatedStudentCourseGroup
            .student(UPDATED_STUDENT)
            .joinTime(UPDATED_JOIN_TIME);

        restStudentCourseGroupMockMvc.perform(put("/api/student-course-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStudentCourseGroup)))
            .andExpect(status().isOk());

        // Validate the StudentCourseGroup in the database
        List<StudentCourseGroup> studentCourseGroupList = studentCourseGroupRepository.findAll();
        assertThat(studentCourseGroupList).hasSize(databaseSizeBeforeUpdate);
        StudentCourseGroup testStudentCourseGroup = studentCourseGroupList.get(studentCourseGroupList.size() - 1);
        assertThat(testStudentCourseGroup.getStudent()).isEqualTo(UPDATED_STUDENT);
        assertThat(testStudentCourseGroup.getJoinTime()).isEqualTo(UPDATED_JOIN_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingStudentCourseGroup() throws Exception {
        int databaseSizeBeforeUpdate = studentCourseGroupRepository.findAll().size();

        // Create the StudentCourseGroup

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentCourseGroupMockMvc.perform(put("/api/student-course-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentCourseGroup)))
            .andExpect(status().isBadRequest());

        // Validate the StudentCourseGroup in the database
        List<StudentCourseGroup> studentCourseGroupList = studentCourseGroupRepository.findAll();
        assertThat(studentCourseGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStudentCourseGroup() throws Exception {
        // Initialize the database
        studentCourseGroupService.save(studentCourseGroup);

        int databaseSizeBeforeDelete = studentCourseGroupRepository.findAll().size();

        // Delete the studentCourseGroup
        restStudentCourseGroupMockMvc.perform(delete("/api/student-course-groups/{id}", studentCourseGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudentCourseGroup> studentCourseGroupList = studentCourseGroupRepository.findAll();
        assertThat(studentCourseGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentCourseGroup.class);
        StudentCourseGroup studentCourseGroup1 = new StudentCourseGroup();
        studentCourseGroup1.setId(1L);
        StudentCourseGroup studentCourseGroup2 = new StudentCourseGroup();
        studentCourseGroup2.setId(studentCourseGroup1.getId());
        assertThat(studentCourseGroup1).isEqualTo(studentCourseGroup2);
        studentCourseGroup2.setId(2L);
        assertThat(studentCourseGroup1).isNotEqualTo(studentCourseGroup2);
        studentCourseGroup1.setId(null);
        assertThat(studentCourseGroup1).isNotEqualTo(studentCourseGroup2);
    }
}
