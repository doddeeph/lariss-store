package id.lariss.store.service.dto;

import id.lariss.store.service.enumeration.TypebotEvent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypebotDTO {

    @NotNull
    private TypebotEvent event;

    private String userInput;

    private Long customerId;
}
