package id.lariss.store.service.impl.v1;

import id.lariss.store.service.CategoryService;
import id.lariss.store.service.ProductService;
import id.lariss.store.service.ProductVariantService;
import id.lariss.store.service.dto.CategoryDTO;
import id.lariss.store.service.dto.ProductDTO;
import id.lariss.store.service.dto.ProductVariantDTO;
import id.lariss.store.service.v1.AsstProductService;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AsstProductServiceImpl implements AsstProductService {

    private static final Logger LOG = LoggerFactory.getLogger(AsstProductServiceImpl.class);

    private final CategoryService categoryService;
    private final ProductService productService;
    private final ProductVariantService productVariantService;

    public AsstProductServiceImpl(
        CategoryService categoryService,
        ProductService productService,
        ProductVariantService productVariantService
    ) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.productVariantService = productVariantService;
    }

    @Override
    public Set<ProductDTO> searchProduct(String productName) {
        return productService.searchProduct(productName);
    }

    @Override
    public List<ProductVariantDTO> findCheapest() {
        return productVariantService.findCheapestByCategoryIds(getCategoryIds());
    }

    @Override
    public List<ProductVariantDTO> findMostExpensive() {
        return productVariantService.findMostExpensiveByCategoryIds(getCategoryIds());
    }

    @Override
    public List<ProductVariantDTO> findCheapest(String productName) {
        return productVariantService.findCheapestByProductIds(getProductIds(productName));
    }

    @Override
    public List<ProductVariantDTO> findMostExpensive(String productName) {
        return productVariantService.findMostExpensiveByProductIds(getProductIds(productName));
    }

    private List<Long> getProductIds(String productName) {
        return searchProduct(productName).stream().map(ProductDTO::getId).toList();
    }

    private List<Long> getCategoryIds() {
        return categoryService.findAll().stream().map(CategoryDTO::getId).toList();
    }
}
