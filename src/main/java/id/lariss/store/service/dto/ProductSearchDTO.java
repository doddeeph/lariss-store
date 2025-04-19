package id.lariss.store.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import java.io.Serializable;
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
    private Long productId;

    @JsonView({ Views.Public.class })
    private Long variantId;

    @JsonView({ Views.Public.class })
    private String title;

    @JsonView({ Views.Public.class })
    private String summary;

    @JsonView({ Views.Public.class })
    private String image;
}
