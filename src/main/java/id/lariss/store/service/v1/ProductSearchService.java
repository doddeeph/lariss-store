package id.lariss.store.service.v1;

import id.lariss.store.service.dto.ProductSearchDTO;
import java.util.List;

public interface ProductSearchService {
    List<ProductSearchDTO> searchProduct(String productName);

    List<ProductSearchDTO> findCheapest();

    List<ProductSearchDTO> findMostExpensive();

    List<ProductSearchDTO> findCheapest(String productName);

    List<ProductSearchDTO> findMostExpensive(String productName);
}
