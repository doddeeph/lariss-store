package id.lariss.store.web.rest;

import static id.lariss.store.domain.CategoryAsserts.*;
import static id.lariss.store.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.store.IntegrationTest;
import id.lariss.store.domain.Category;
import id.lariss.store.repository.CategoryRepository;
import id.lariss.store.service.dto.CategoryDTO;
import id.lariss.store.service.mapper.CategoryMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoryResourceIT {

    private static final String DEFAULT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoryMockMvc;

    private Category category;

    private Category insertedCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Category createEntity() {
        return new Category().categoryName(DEFAULT_CATEGORY_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Category createUpdatedEntity() {
        return new Category().categoryName(UPDATED_CATEGORY_NAME);
    }

    @BeforeEach
    public void initTest() {
        category = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCategory != null) {
            categoryRepository.delete(insertedCategory);
            insertedCategory = null;
        }
    }

    @Test
    @Transactional
    void createCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);
        var returnedCategoryDTO = om.readValue(
            restCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CategoryDTO.class
        );

        // Validate the Category in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCategory = categoryMapper.toEntity(returnedCategoryDTO);
        assertCategoryUpdatableFieldsEquals(returnedCategory, getPersistedCategory(returnedCategory));

        insertedCategory = returnedCategory;
    }

    @Test
    @Transactional
    void createCategoryWithExistingId() throws Exception {
        // Create the Category with an existing ID
        category.setId(1L);
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCategories() throws Exception {
        // Initialize the database
        insertedCategory = categoryRepository.saveAndFlush(category);

        // Get all the categoryList
        restCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(category.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)));
    }

    @Test
    @Transactional
    void getCategory() throws Exception {
        // Initialize the database
        insertedCategory = categoryRepository.saveAndFlush(category);

        // Get the category
        restCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, category.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(category.getId().intValue()))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCategory() throws Exception {
        // Get the category
        restCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCategory() throws Exception {
        // Initialize the database
        insertedCategory = categoryRepository.saveAndFlush(category);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the category
        Category updatedCategory = categoryRepository.findById(category.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCategory are not directly saved in db
        em.detach(updatedCategory);
        updatedCategory.categoryName(UPDATED_CATEGORY_NAME);
        CategoryDTO categoryDTO = categoryMapper.toDto(updatedCategory);

        restCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Category in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCategoryToMatchAllProperties(updatedCategory);
    }

    @Test
    @Transactional
    void putNonExistingCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        category.setId(longCount.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        category.setId(longCount.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        category.setId(longCount.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Category in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedCategory = categoryRepository.saveAndFlush(category);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the category using partial update
        Category partialUpdatedCategory = new Category();
        partialUpdatedCategory.setId(category.getId());

        partialUpdatedCategory.categoryName(UPDATED_CATEGORY_NAME);

        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCategory))
            )
            .andExpect(status().isOk());

        // Validate the Category in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCategoryUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCategory, category), getPersistedCategory(category));
    }

    @Test
    @Transactional
    void fullUpdateCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedCategory = categoryRepository.saveAndFlush(category);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the category using partial update
        Category partialUpdatedCategory = new Category();
        partialUpdatedCategory.setId(category.getId());

        partialUpdatedCategory.categoryName(UPDATED_CATEGORY_NAME);

        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCategory))
            )
            .andExpect(status().isOk());

        // Validate the Category in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCategoryUpdatableFieldsEquals(partialUpdatedCategory, getPersistedCategory(partialUpdatedCategory));
    }

    @Test
    @Transactional
    void patchNonExistingCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        category.setId(longCount.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(categoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        category.setId(longCount.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(categoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        category.setId(longCount.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(categoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Category in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategory() throws Exception {
        // Initialize the database
        insertedCategory = categoryRepository.saveAndFlush(category);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the category
        restCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, category.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return categoryRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Category getPersistedCategory(Category category) {
        return categoryRepository.findById(category.getId()).orElseThrow();
    }

    protected void assertPersistedCategoryToMatchAllProperties(Category expectedCategory) {
        assertCategoryAllPropertiesEquals(expectedCategory, getPersistedCategory(expectedCategory));
    }

    protected void assertPersistedCategoryToMatchUpdatableProperties(Category expectedCategory) {
        assertCategoryAllUpdatablePropertiesEquals(expectedCategory, getPersistedCategory(expectedCategory));
    }
}
