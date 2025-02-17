package id.lariss.store.web.rest.v1;

import id.lariss.store.service.dto.ProductDTO;
import id.lariss.store.service.dto.ProductVariantDTO;
import id.lariss.store.service.v1.AsstProductService;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class AsstProductResource {

    private static final Logger LOG = LoggerFactory.getLogger(AsstProductResource.class);

    private final AsstProductService productService;

    public AsstProductResource(AsstProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Set<ProductDTO>> searchProduct(@RequestParam(value = "productName", required = false) String productName) {
        Set<ProductDTO> productDTOs = productService.searchProduct(productName);
        return ResponseEntity.ok().body(productDTOs);
    }

    @GetMapping("/cheapest")
    public ResponseEntity<List<ProductVariantDTO>> getCheapestProduct(
        @RequestParam(value = "productName", required = false) String productName
    ) {
        List<ProductVariantDTO> productVariantDTOs = Objects.isNull(productName)
            ? productService.findCheapest()
            : productService.findCheapest(productName);
        return ResponseEntity.ok().body(productVariantDTOs);
    }

    @GetMapping("/most-expensive")
    public ResponseEntity<List<ProductVariantDTO>> getMostExpensiveProduct(
        @RequestParam(value = "productName", required = false) String productName
    ) {
        List<ProductVariantDTO> productVariantDTOs = Objects.isNull(productName)
            ? productService.findMostExpensive()
            : productService.findMostExpensive(productName);
        return ResponseEntity.ok().body(productVariantDTOs);
    }
}
