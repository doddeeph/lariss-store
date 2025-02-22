package id.lariss.store.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO for the {@link id.lariss.store.domain.Customer} entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerDTO implements Serializable {

    @JsonView(Views.Public.class)
    private Long id;

    @JsonView(Views.Public.class)
    private String firstName;

    @JsonView(Views.Public.class)
    private String lastName;

    @JsonView(Views.Internal.class)
    private String phoneNumber;

    @JsonView(Views.Internal.class)
    private String emailAddress;

    @JsonView(Views.Internal.class)
    private String deliveryAddress;
}
