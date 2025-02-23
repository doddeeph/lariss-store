package id.lariss.store.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSearchDTO implements Serializable {

    @JsonView({ Views.Public.class })
    private ProductDTO product;

    @JsonView({ Views.Public.class })
    private List<ProductVariantDTO> variants;
}
