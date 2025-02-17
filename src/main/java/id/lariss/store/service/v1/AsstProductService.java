package id.lariss.store.service.v1;

import id.lariss.store.service.dto.ProductDTO;
import id.lariss.store.service.dto.ProductVariantDTO;
import java.util.List;
import java.util.Set;

public interface AsstProductService {
    Set<ProductDTO> searchProduct(String productName);

    List<ProductVariantDTO> findCheapest();

    List<ProductVariantDTO> findMostExpensive();

    List<ProductVariantDTO> findCheapest(String productName);

    List<ProductVariantDTO> findMostExpensive(String productName);
}
