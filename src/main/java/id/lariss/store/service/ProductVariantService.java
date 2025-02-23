package id.lariss.store.service;

import id.lariss.store.service.dto.ProductVariantDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link id.lariss.store.domain.ProductVariant}.
 */
public interface ProductVariantService {
    /**
     * Save a productVariant.
     *
     * @param productVariantDTO the entity to save.
     * @return the persisted entity.
     */
    ProductVariantDTO save(ProductVariantDTO productVariantDTO);

    /**
     * Updates a productVariant.
     *
     * @param productVariantDTO the entity to update.
     * @return the persisted entity.
     */
    ProductVariantDTO update(ProductVariantDTO productVariantDTO);

    /**
     * Partially updates a productVariant.
     *
     * @param productVariantDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductVariantDTO> partialUpdate(ProductVariantDTO productVariantDTO);

    /**
     * Get all the productVariants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductVariantDTO> findAll(Pageable pageable);

    /**
     * Get all the ProductVariantDTO where CartItem is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<ProductVariantDTO> findAllWhereCartItemIsNull();

    /**
     * Get all the productVariants with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductVariantDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" productVariant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductVariantDTO> findOne(Long id);

    /**
     * Delete the "id" productVariant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<ProductVariantDTO> findAllByProductIds(List<Long> productIds);

    List<ProductVariantDTO> findCheapestByCategoryIds(List<Long> categoryIds);

    List<ProductVariantDTO> findCheapestByProductIds(List<Long> productIds);

    List<ProductVariantDTO> findMostExpensiveByCategoryIds(List<Long> categoryIds);

    List<ProductVariantDTO> findMostExpensiveByProductIds(List<Long> productIds);
}
