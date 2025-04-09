package id.lariss.store.service.impl.v1;

import id.lariss.store.service.CategoryService;
import id.lariss.store.service.ProductService;
import id.lariss.store.service.ProductVariantService;
import id.lariss.store.service.dto.CategoryDTO;
import id.lariss.store.service.dto.ProductDTO;
import id.lariss.store.service.dto.ProductSearchDTO;
import id.lariss.store.service.dto.ProductVariantDTO;
import id.lariss.store.service.v1.ProductSearchService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProductSearchServiceImpl implements ProductSearchService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductSearchServiceImpl.class);

    private final CategoryService categoryService;
    private final ProductService productService;
    private final ProductVariantService productVariantService;

    public ProductSearchServiceImpl(
        CategoryService categoryService,
        ProductService productService,
        ProductVariantService productVariantService
    ) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.productVariantService = productVariantService;
    }

    @Override
    public List<ProductSearchDTO> searchProduct(String productName) {
        List<ProductDTO> productDTOs = productService.searchProduct(productName);
        List<Long> productIds = productDTOs.stream().map(ProductDTO::getId).toList();
        Map<ProductDTO, List<ProductVariantDTO>> mapByProduct = productVariantService
            .findAllByProductIds(productIds)
            .stream()
            .collect(Collectors.groupingBy(ProductVariantDTO::getProduct));
        return mapToProductSearchDTO(mapByProduct);
    }

    @Override
    public List<ProductSearchDTO> findCheapest() {
        Map<ProductDTO, List<ProductVariantDTO>> mapByProduct = productVariantService
            .findCheapestByCategoryIds(getCategoryIds())
            .stream()
            .collect(Collectors.groupingBy(ProductVariantDTO::getProduct));
        return mapToProductSearchDTO(mapByProduct);
    }

    @Override
    public List<ProductSearchDTO> findMostExpensive() {
        Map<ProductDTO, List<ProductVariantDTO>> mapByProduct = productVariantService
            .findMostExpensiveByCategoryIds(getCategoryIds())
            .stream()
            .collect(Collectors.groupingBy(ProductVariantDTO::getProduct));
        return mapToProductSearchDTO(mapByProduct);
    }

    @Override
    public List<ProductSearchDTO> findCheapest(String productName) {
        Map<ProductDTO, List<ProductVariantDTO>> mapByProduct = productVariantService
            .findCheapestByProductIds(getProductIds(productName))
            .stream()
            .collect(Collectors.groupingBy(ProductVariantDTO::getProduct));
        return mapToProductSearchDTO(mapByProduct);
    }

    @Override
    public List<ProductSearchDTO> findMostExpensive(String productName) {
        Map<ProductDTO, List<ProductVariantDTO>> mapByProduct = productVariantService
            .findMostExpensiveByProductIds(getProductIds(productName))
            .stream()
            .collect(Collectors.groupingBy(ProductVariantDTO::getProduct));
        return mapToProductSearchDTO(mapByProduct);
    }

    @Override
    public List<ProductSearchDTO> findAllByCategoryId(Long categoryId) {
        Map<ProductDTO, List<ProductVariantDTO>> mapByProduct = productVariantService
            .findAllByCategoryId(categoryId)
            .stream()
            .collect(Collectors.groupingBy(ProductVariantDTO::getProduct));
        return mapToProductSearchDTO(mapByProduct);
    }

    private List<Long> getProductIds(String productName) {
        return productService.searchProduct(productName).stream().map(ProductDTO::getId).toList();
    }

    private List<Long> getCategoryIds() {
        return categoryService.findAll().stream().map(CategoryDTO::getId).toList();
    }

    private List<ProductSearchDTO> mapToProductSearchDTO(Map<ProductDTO, List<ProductVariantDTO>> mapByProduct) {
        return mapByProduct
            .entrySet()
            .stream()
            .map(entry -> ProductSearchDTO.builder().product(entry.getKey()).variants(entry.getValue()).build())
            .toList();
    }
}
