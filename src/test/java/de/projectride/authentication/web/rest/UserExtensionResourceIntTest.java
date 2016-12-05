package de.projectride.authentication.web.rest;

import de.projectride.authentication.AuthenticationApp;

import de.projectride.authentication.domain.UserExtension;
import de.projectride.authentication.repository.UserExtensionRepository;
import de.projectride.authentication.service.UserExtensionService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.projectride.authentication.domain.enumeration.Sex;
/**
 * Test class for the UserExtensionResource REST controller.
 *
 * @see UserExtensionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthenticationApp.class)
public class UserExtensionResourceIntTest {

    private static final Sex DEFAULT_SEX = Sex.MALE;
    private static final Sex UPDATED_SEX = Sex.FEMALE;

    private static final LocalDate DEFAULT_BIRTHDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDATE = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_MEMBER_SINCE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_MEMBER_SINCE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_MEMBER_SINCE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_MEMBER_SINCE);

    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_ABOUT_ME = "AAAAA";
    private static final String UPDATED_ABOUT_ME = "BBBBB";

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    @Inject
    private UserExtensionRepository userExtensionRepository;

    @Inject
    private UserExtensionService userExtensionService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUserExtensionMockMvc;

    private UserExtension userExtension;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserExtensionResource userExtensionResource = new UserExtensionResource();
        ReflectionTestUtils.setField(userExtensionResource, "userExtensionService", userExtensionService);
        this.restUserExtensionMockMvc = MockMvcBuilders.standaloneSetup(userExtensionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserExtension createEntity(EntityManager em) {
        UserExtension userExtension = new UserExtension()
                .sex(DEFAULT_SEX)
                .birthdate(DEFAULT_BIRTHDATE)
                .memberSince(DEFAULT_MEMBER_SINCE)
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .phoneNumber(DEFAULT_PHONE_NUMBER)
                .image(DEFAULT_IMAGE)
                .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
                .aboutMe(DEFAULT_ABOUT_ME)
                .userId(DEFAULT_USER_ID);
        return userExtension;
    }

    @Before
    public void initTest() {
        userExtension = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserExtension() throws Exception {
        int databaseSizeBeforeCreate = userExtensionRepository.findAll().size();

        // Create the UserExtension

        restUserExtensionMockMvc.perform(post("/api/user-extensions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userExtension)))
                .andExpect(status().isCreated());

        // Validate the UserExtension in the database
        List<UserExtension> userExtensions = userExtensionRepository.findAll();
        assertThat(userExtensions).hasSize(databaseSizeBeforeCreate + 1);
        UserExtension testUserExtension = userExtensions.get(userExtensions.size() - 1);
        assertThat(testUserExtension.getSex()).isEqualTo(DEFAULT_SEX);
        assertThat(testUserExtension.getBirthdate()).isEqualTo(DEFAULT_BIRTHDATE);
        assertThat(testUserExtension.getMemberSince()).isEqualTo(DEFAULT_MEMBER_SINCE);
        assertThat(testUserExtension.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUserExtension.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUserExtension.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testUserExtension.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testUserExtension.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testUserExtension.getAboutMe()).isEqualTo(DEFAULT_ABOUT_ME);
        assertThat(testUserExtension.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    public void checkSexIsRequired() throws Exception {
        int databaseSizeBeforeTest = userExtensionRepository.findAll().size();
        // set the field null
        userExtension.setSex(null);

        // Create the UserExtension, which fails.

        restUserExtensionMockMvc.perform(post("/api/user-extensions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userExtension)))
                .andExpect(status().isBadRequest());

        List<UserExtension> userExtensions = userExtensionRepository.findAll();
        assertThat(userExtensions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBirthdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userExtensionRepository.findAll().size();
        // set the field null
        userExtension.setBirthdate(null);

        // Create the UserExtension, which fails.

        restUserExtensionMockMvc.perform(post("/api/user-extensions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userExtension)))
                .andExpect(status().isBadRequest());

        List<UserExtension> userExtensions = userExtensionRepository.findAll();
        assertThat(userExtensions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userExtensionRepository.findAll().size();
        // set the field null
        userExtension.setFirstName(null);

        // Create the UserExtension, which fails.

        restUserExtensionMockMvc.perform(post("/api/user-extensions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userExtension)))
                .andExpect(status().isBadRequest());

        List<UserExtension> userExtensions = userExtensionRepository.findAll();
        assertThat(userExtensions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userExtensionRepository.findAll().size();
        // set the field null
        userExtension.setLastName(null);

        // Create the UserExtension, which fails.

        restUserExtensionMockMvc.perform(post("/api/user-extensions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userExtension)))
                .andExpect(status().isBadRequest());

        List<UserExtension> userExtensions = userExtensionRepository.findAll();
        assertThat(userExtensions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserExtensions() throws Exception {
        // Initialize the database
        userExtensionRepository.saveAndFlush(userExtension);

        // Get all the userExtensions
        restUserExtensionMockMvc.perform(get("/api/user-extensions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userExtension.getId().intValue())))
                .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())))
                .andExpect(jsonPath("$.[*].birthdate").value(hasItem(DEFAULT_BIRTHDATE.toString())))
                .andExpect(jsonPath("$.[*].memberSince").value(hasItem(DEFAULT_MEMBER_SINCE_STR)))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
                .andExpect(jsonPath("$.[*].aboutMe").value(hasItem(DEFAULT_ABOUT_ME.toString())))
                .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    public void getUserExtension() throws Exception {
        // Initialize the database
        userExtensionRepository.saveAndFlush(userExtension);

        // Get the userExtension
        restUserExtensionMockMvc.perform(get("/api/user-extensions/{id}", userExtension.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userExtension.getId().intValue()))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX.toString()))
            .andExpect(jsonPath("$.birthdate").value(DEFAULT_BIRTHDATE.toString()))
            .andExpect(jsonPath("$.memberSince").value(DEFAULT_MEMBER_SINCE_STR))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.aboutMe").value(DEFAULT_ABOUT_ME.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserExtension() throws Exception {
        // Get the userExtension
        restUserExtensionMockMvc.perform(get("/api/user-extensions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserExtension() throws Exception {
        // Initialize the database
        userExtensionService.save(userExtension);

        int databaseSizeBeforeUpdate = userExtensionRepository.findAll().size();

        // Update the userExtension
        UserExtension updatedUserExtension = userExtensionRepository.findOne(userExtension.getId());
        updatedUserExtension
                .sex(UPDATED_SEX)
                .birthdate(UPDATED_BIRTHDATE)
                .memberSince(UPDATED_MEMBER_SINCE)
                .firstName(UPDATED_FIRST_NAME)
                .lastName(UPDATED_LAST_NAME)
                .phoneNumber(UPDATED_PHONE_NUMBER)
                .image(UPDATED_IMAGE)
                .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
                .aboutMe(UPDATED_ABOUT_ME)
                .userId(UPDATED_USER_ID);

        restUserExtensionMockMvc.perform(put("/api/user-extensions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUserExtension)))
                .andExpect(status().isOk());

        // Validate the UserExtension in the database
        List<UserExtension> userExtensions = userExtensionRepository.findAll();
        assertThat(userExtensions).hasSize(databaseSizeBeforeUpdate);
        UserExtension testUserExtension = userExtensions.get(userExtensions.size() - 1);
        assertThat(testUserExtension.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testUserExtension.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
        assertThat(testUserExtension.getMemberSince()).isEqualTo(UPDATED_MEMBER_SINCE);
        assertThat(testUserExtension.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUserExtension.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserExtension.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testUserExtension.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testUserExtension.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testUserExtension.getAboutMe()).isEqualTo(UPDATED_ABOUT_ME);
        assertThat(testUserExtension.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void deleteUserExtension() throws Exception {
        // Initialize the database
        userExtensionService.save(userExtension);

        int databaseSizeBeforeDelete = userExtensionRepository.findAll().size();

        // Get the userExtension
        restUserExtensionMockMvc.perform(delete("/api/user-extensions/{id}", userExtension.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserExtension> userExtensions = userExtensionRepository.findAll();
        assertThat(userExtensions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
