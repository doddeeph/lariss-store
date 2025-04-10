package id.lariss.store.web.rest.v1;

import com.fasterxml.jackson.annotation.JsonView;
import id.lariss.store.service.dto.ProductSearchDTO;
import id.lariss.store.service.dto.Views;
import id.lariss.store.service.v1.ProductService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("ProductResourceV1")
@RequestMapping("/api/v1/products")
public class ProductResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductResource.class);

    private final ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @JsonView(Views.Public.class)
    public ResponseEntity<List<ProductSearchDTO>> getAllProducts(@RequestParam(name = "categoryId") Long categoryId) {
        List<ProductSearchDTO> dtoList = productService.findAllByCategoryId(categoryId);
        return ResponseEntity.ok().body(dtoList);
    }
}
