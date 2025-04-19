package id.lariss.store.service.impl;

import id.lariss.store.domain.Product;
import id.lariss.store.domain.ProductVariant;
import id.lariss.store.repository.ProductVariantRepository;
import id.lariss.store.service.ProductVariantService;
import id.lariss.store.service.dto.ProductSearch;
import id.lariss.store.service.dto.ProductVariantDTO;
import id.lariss.store.service.mapper.ProductVariantMapper;
import jakarta.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    public List<ProductVariantDTO> findAllByProductIdsAndSearchAttributes(List<Long> productIds, ProductSearch.Attributes attributes) {
        if (Objects.isNull(attributes)) {
            return productVariantRepository.findAllByProductIds(productIds).stream().map(productVariantMapper::toDto).toList();
        }
        return productVariantRepository
            .findAll(byProductSearchAttributes(productIds, attributes))
            .stream()
            .map(productVariantMapper::toDto)
            .toList();
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
    public List<ProductVariantDTO> findCheapestByProductIdsAndSearchAttributes(List<Long> productIds, ProductSearch.Attributes attributes) {
        if (Objects.isNull(attributes)) {
            return productVariantRepository.findCheapestByProductIds(productIds).stream().map(productVariantMapper::toDto).toList();
        }
        return productVariantRepository
            .findAll(byCheapestOrMostExpensivePriceAndAttributes(productIds, attributes, true))
            .stream()
            .map(productVariantMapper::toDto)
            .toList();
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
    public List<ProductVariantDTO> findMostExpensiveByProductIdsAndSearchAttributes(
        List<Long> productIds,
        ProductSearch.Attributes attributes
    ) {
        if (Objects.isNull(attributes)) {
            return productVariantRepository.findMostExpensiveByProductIds(productIds).stream().map(productVariantMapper::toDto).toList();
        }
        return productVariantRepository
            .findAll(byCheapestOrMostExpensivePriceAndAttributes(productIds, attributes, false))
            .stream()
            .map(productVariantMapper::toDto)
            .toList();
    }

    @Override
    public List<ProductVariantDTO> findAllByCategoryId(Long categoryId) {
        return productVariantRepository.findAllByCategoryId(categoryId).stream().map(productVariantMapper::toDto).toList();
    }

    @Override
    public List<ProductVariantDTO> findCheapestProductVariants() {
        return productVariantRepository.findCheapestProductVariants().stream().map(productVariantMapper::toDto).toList();
    }

    @Override
    public List<ProductVariantDTO> findMostExpensiveProductVariants() {
        return productVariantRepository.findMostExpensiveProductVariants().stream().map(productVariantMapper::toDto).toList();
    }

    private Specification<ProductVariant> byProductSearchAttributes(List<Long> productIds, ProductSearch.Attributes attrs) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(productIds)) {
                predicates.add(root.get("product").get("id").in(productIds));
            }
            if (Objects.nonNull(attrs)) {
                buildAttrsMap(attrs).forEach((field, value) -> {
                    if (StringUtils.isNotBlank(value)) {
                        if ("processor".equals(field) || "screen".equals(field)) {
                            String pattern = "%" + value.toLowerCase() + "%";
                            predicates.add(cb.like(cb.lower(root.get(field)), pattern));
                        } else {
                            predicates.add(cb.equal(cb.lower(root.get(field)), value.toLowerCase()));
                        }
                    }
                });
                if (CollectionUtils.isNotEmpty(attrs.getStrapSize())) {
                    predicates.add(root.get("strapSize").in(attrs.getStrapSize()));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Map<String, String> buildAttrsMap(ProductSearch.Attributes attrs) {
        Map<String, String> attrsMap = new HashMap<>();
        Optional.ofNullable(attrs.getColor()).ifPresent(s -> attrsMap.put("color", s));
        Optional.ofNullable(attrs.getProcessor()).ifPresent(s -> attrsMap.put("processor", s));
        Optional.ofNullable(attrs.getMemory()).ifPresent(s -> attrsMap.put("memory", s));
        Optional.ofNullable(attrs.getStorage()).ifPresent(s -> attrsMap.put("storage", s));
        Optional.ofNullable(attrs.getScreen()).ifPresent(s -> attrsMap.put("screen", s));
        Optional.ofNullable(attrs.getConnectivity()).ifPresent(s -> attrsMap.put("connectivity", s));
        Optional.ofNullable(attrs.getMaterial()).ifPresent(s -> attrsMap.put("material", s));
        Optional.ofNullable(attrs.getCaseSize()).ifPresent(s -> attrsMap.put("caseSize", s));
        Optional.ofNullable(attrs.getStrapColor()).ifPresent(s -> attrsMap.put("strapColor", s));
        return attrsMap;
    }

    private Specification<ProductVariant> byCheapestOrMostExpensivePriceAndAttributes(
        List<Long> productIds,
        ProductSearch.Attributes attrs,
        boolean cheapest
    ) {
        return (root, query, cb) -> {
            Subquery<BigDecimal> minOrMaxPriceSubquery = Objects.requireNonNull(query).subquery(BigDecimal.class);
            Root<ProductVariant> subRoot = minOrMaxPriceSubquery.from(ProductVariant.class);
            Join<ProductVariant, Product> subProductJoin = subRoot.join("product");
            Join<ProductVariant, Product> productJoin = root.join("product");

            Pair<List<Predicate>, List<Predicate>> attrsPredicates = buildAttrsPredicates(
                productIds,
                attrs,
                subRoot,
                root,
                subProductJoin,
                productJoin,
                cb
            );
            List<Predicate> subPredicates = attrsPredicates.getLeft();
            List<Predicate> rootPredicates = attrsPredicates.getRight();

            if (cheapest) {
                minOrMaxPriceSubquery.select(cb.min(subRoot.get("price")));
            } else {
                minOrMaxPriceSubquery.select(cb.max(subRoot.get("price")));
            }

            minOrMaxPriceSubquery.where(cb.and(subPredicates.toArray(new Predicate[0])));
            rootPredicates.add(cb.equal(root.get("price"), minOrMaxPriceSubquery));

            return cb.and(rootPredicates.toArray(new Predicate[0]));
        };
    }

    private Pair<List<Predicate>, List<Predicate>> buildAttrsPredicates(
        List<Long> productIds,
        ProductSearch.Attributes attrs,
        Root<ProductVariant> subRoot,
        Root<ProductVariant> root,
        Join<ProductVariant, Product> subProductJoin,
        Join<ProductVariant, Product> productJoin,
        CriteriaBuilder cb
    ) {
        List<Predicate> subPredicates = new ArrayList<>();
        List<Predicate> rootPredicates = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(productIds)) {
            subPredicates.add(subProductJoin.get("id").in(productIds));
            rootPredicates.add(productJoin.get("id").in(productIds));
        }
        if (Objects.nonNull(attrs)) {
            buildAttrsMap(attrs).forEach((field, value) -> {
                if (StringUtils.isNotBlank(value)) {
                    if ("processor".equals(field) || "screen".equals(field)) {
                        String pattern = "%" + value.toLowerCase() + "%";
                        rootPredicates.add(cb.like(cb.lower(root.get(field)), pattern));
                        subPredicates.add(cb.like(cb.lower(subRoot.get(field)), pattern));
                    } else {
                        subPredicates.add(cb.equal(cb.lower(subRoot.get(field)), value.toLowerCase()));
                        rootPredicates.add(cb.equal(cb.lower(root.get(field)), value.toLowerCase()));
                    }
                }
            });
            if (CollectionUtils.isNotEmpty(attrs.getStrapSize())) {
                subPredicates.add(subRoot.get("strapSize").in(attrs.getStrapSize()));
                rootPredicates.add(root.get("strapSize").in(attrs.getStrapSize()));
            }
        }
        return Pair.of(subPredicates, rootPredicates);
    }
}
