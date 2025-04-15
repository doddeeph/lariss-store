package id.lariss.store.service.v1;

import id.lariss.store.service.dto.ProductSearch;
import id.lariss.store.service.dto.ProductSearchDTO;
import java.util.List;

public interface ProductService {
    List<ProductSearchDTO> searchProduct(ProductSearch productSearch);
    List<ProductSearchDTO> findAllByCategoryId(Long categoryId);
}
