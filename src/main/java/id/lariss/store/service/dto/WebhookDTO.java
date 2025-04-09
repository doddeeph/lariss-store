package id.lariss.store.service.dto;

import id.lariss.store.service.enumeration.WebhookEvent;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebhookDTO {

    @NotNull
    private WebhookEvent event;

    private Map<Object, Object> request;
}
