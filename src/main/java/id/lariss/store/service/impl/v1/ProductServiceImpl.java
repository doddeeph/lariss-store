package id.lariss.store.service.impl.v1;

import id.lariss.store.service.CategoryService;
import id.lariss.store.service.ProductVariantService;
import id.lariss.store.service.dto.*;
import id.lariss.store.service.v1.ProductService;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("ProductServiceImplV1")
public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final CategoryService categoryService;
    private final id.lariss.store.service.ProductService productService;
    private final ProductVariantService productVariantService;

    public ProductServiceImpl(
        CategoryService categoryService,
        id.lariss.store.service.ProductService productService,
        ProductVariantService productVariantService
    ) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.productVariantService = productVariantService;
    }

    @Override
    public List<ProductSearchDTO> searchProduct(ProductSearch productSearch) {
        String intent = productSearch.getIntent();
        if (StringUtils.isNotBlank(intent)) {
            return switch (intent) {
                case "cheapest" -> findCheapest(productSearch);
                case "most expensive" -> findMostExpensive(productSearch);
                case "search" -> doSearchProduct(productSearch);
                default -> new ArrayList<>();
            };
        }
        return doSearchProduct(productSearch);
    }

    @Override
    public List<ProductSearchDTO> findAllByCategoryId(Long categoryId) {
        return productVariantService.findAllByCategoryId(categoryId).stream().map(this::mapToProductSearchDTO).toList();
    }

    private List<ProductSearchDTO> findCheapest() {
        return productVariantService.findCheapestByCategoryIds(getCategoryIds()).stream().map(this::mapToProductSearchDTO).toList();
    }

    private List<ProductSearchDTO> findMostExpensive() {
        return productVariantService.findMostExpensiveByCategoryIds(getCategoryIds()).stream().map(this::mapToProductSearchDTO).toList();
    }

    private List<ProductSearchDTO> findCheapest(ProductSearch productSearch) {
        String[] productNames = productSearch.getProductNames();
        if (ArrayUtils.isEmpty(productNames)) {
            return findCheapest();
        }
        return productVariantService
            .findCheapestByProductIdsAndSearchAttributes(getProductIds(productNames), productSearch.getAttributes())
            .stream()
            .map(this::mapToProductSearchDTO)
            .toList();
    }

    private List<ProductSearchDTO> findMostExpensive(ProductSearch productSearch) {
        String[] productNames = productSearch.getProductNames();
        if (ArrayUtils.isEmpty(productNames)) {
            return findMostExpensive();
        }
        return productVariantService
            .findMostExpensiveByProductIdsAndSearchAttributes(getProductIds(productNames), productSearch.getAttributes())
            .stream()
            .map(this::mapToProductSearchDTO)
            .toList();
    }

    private List<ProductSearchDTO> doSearchProduct(ProductSearch productSearch) {
        List<ProductDTO> productDTOs = productService.searchProduct(productSearch.getProductNames());
        List<Long> productIds = productDTOs.stream().map(ProductDTO::getId).toList();
        return productVariantService
            .findAllByProductIdsAndSearchAttributes(productIds, productSearch.getAttributes())
            .stream()
            .map(this::mapToProductSearchDTO)
            .toList();
    }

    private List<Long> getProductIds(String[] productNames) {
        return productService.searchProduct(productNames).stream().map(ProductDTO::getId).toList();
    }

    private List<Long> getCategoryIds() {
        return categoryService.findAll().stream().map(CategoryDTO::getId).toList();
    }

    private ProductSearchDTO mapToProductSearchDTO(ProductVariantDTO dto) {
        return ProductSearchDTO.builder()
            .productId(dto.getProduct().getId())
            .variantId(dto.getId())
            .title(dto.getProduct().getProductName())
            .summary(dto.getSummary())
            .image(dto.getImageUrl())
            .build();
    }
}
