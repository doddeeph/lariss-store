package id.lariss.store.service.impl;

import id.lariss.store.domain.ProductVariant;
import id.lariss.store.repository.ProductVariantRepository;
import id.lariss.store.service.ProductVariantService;
import id.lariss.store.service.dto.ProductVariantDTO;
import id.lariss.store.service.mapper.ProductVariantMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link id.lariss.store.domain.ProductVariant}.
 */
@Service
@Transactional
public class ProductVariantServiceImpl implements ProductVariantService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductVariantServiceImpl.class);

    private final ProductVariantRepository productVariantRepository;

    private final ProductVariantMapper productVariantMapper;

    public ProductVariantServiceImpl(ProductVariantRepository productVariantRepository, ProductVariantMapper productVariantMapper) {
        this.productVariantRepository = productVariantRepository;
        this.productVariantMapper = productVariantMapper;
    }

    @Override
    public ProductVariantDTO save(ProductVariantDTO productVariantDTO) {
        LOG.debug("Request to save ProductVariant : {}", productVariantDTO);
        ProductVariant productVariant = productVariantMapper.toEntity(productVariantDTO);
        productVariant = productVariantRepository.save(productVariant);
        return productVariantMapper.toDto(productVariant);
    }

    @Override
    public ProductVariantDTO update(ProductVariantDTO productVariantDTO) {
        LOG.debug("Request to update ProductVariant : {}", productVariantDTO);
        ProductVariant productVariant = productVariantMapper.toEntity(productVariantDTO);
        productVariant = productVariantRepository.save(productVariant);
        return productVariantMapper.toDto(productVariant);
    }

    @Override
    public Optional<ProductVariantDTO> partialUpdate(ProductVariantDTO productVariantDTO) {
        LOG.debug("Request to partially update ProductVariant : {}", productVariantDTO);

        return productVariantRepository
            .findById(productVariantDTO.getId())
            .map(existingProductVariant -> {
                productVariantMapper.partialUpdate(existingProductVariant, productVariantDTO);

                return existingProductVariant;
            })
            .map(productVariantRepository::save)
            .map(productVariantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductVariantDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ProductVariants");
        return productVariantRepository.findAll(pageable).map(productVariantMapper::toDto);
    }

    public Page<ProductVariantDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productVariantRepository.findAllWithEagerRelationships(pageable).map(productVariantMapper::toDto);
    }

    /**
     *  Get all the productVariants where CartItem is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProductVariantDTO> findAllWhereCartItemIsNull() {
        LOG.debug("Request to get all productVariants where CartItem is null");
        return StreamSupport.stream(productVariantRepository.findAll().spliterator(), false)
            .filter(productVariant -> productVariant.getCartItem() == null)
            .map(productVariantMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the productVariants where OrderItem is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProductVariantDTO> findAllWhereOrderItemIsNull() {
        LOG.debug("Request to get all productVariants where OrderItem is null");
        return StreamSupport.stream(productVariantRepository.findAll().spliterator(), false)
            .filter(productVariant -> productVariant.getOrderItem() == null)
            .map(productVariantMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductVariantDTO> findOne(Long id) {
        LOG.debug("Request to get ProductVariant : {}", id);
        return productVariantRepository.findOneWithEagerRelationships(id).map(productVariantMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProductVariant : {}", id);
        productVariantRepository.deleteById(id);
    }

    @Override
    public List<ProductVariantDTO> findCheapestByCategoryIds(List<Long> categoryIds) {
        return categoryIds
            .stream()
            .flatMap(catId -> productVariantRepository.findCheapestByCategoryId(catId).stream())
            .map(productVariantMapper::toDto)
            .toList();
    }

    @Override
    public List<ProductVariantDTO> findCheapestByProductIds(List<Long> productIds) {
        return productVariantRepository.findCheapestByProductIds(productIds).stream().map(productVariantMapper::toDto).toList();
    }

    @Override
    public List<ProductVariantDTO> findMostExpensiveByCategoryIds(List<Long> categoryIds) {
        return categoryIds
            .stream()
            .flatMap(catId -> productVariantRepository.findMostExpensiveByCategoryId(catId).stream())
            .map(productVariantMapper::toDto)
            .toList();
    }

    @Override
    public List<ProductVariantDTO> findMostExpensiveByProductIds(List<Long> productIds) {
        return productVariantRepository.findMostExpensiveByProductIds(productIds).stream().map(productVariantMapper::toDto).toList();
    }
}
