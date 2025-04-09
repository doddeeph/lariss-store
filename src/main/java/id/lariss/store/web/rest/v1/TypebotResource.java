package id.lariss.store.web.rest.v1;

import com.fasterxml.jackson.annotation.JsonView;
import id.lariss.store.service.dto.Views;
import id.lariss.store.service.dto.WebhookDTO;
import id.lariss.store.service.v1.AiService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/typebot")
public class TypebotResource {

    private final AiService aiService;

    public TypebotResource(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/webhook")
    @JsonView(Views.Public.class)
    public ResponseEntity<Object> handleWebhook(@Valid @RequestBody WebhookDTO webhookDTO) {
        Object response = aiService.handleWebhook(webhookDTO);
        return ResponseEntity.ok().body(response);
    }
}
