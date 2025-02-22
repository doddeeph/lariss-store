package id.lariss.store.web.rest.v1;

import com.fasterxml.jackson.annotation.JsonView;
import id.lariss.store.service.dto.TypebotDTO;
import id.lariss.store.service.dto.Views;
import id.lariss.store.service.v1.TypebotService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/typebot")
public class TypebotResource {

    private final TypebotService typebotService;

    public TypebotResource(TypebotService typebotService) {
        this.typebotService = typebotService;
    }

    @PostMapping("/webhook")
    @JsonView(Views.Public.class)
    public ResponseEntity<Object> handleWebhook(@Valid @RequestBody TypebotDTO typebotDTO) {
        Object response = typebotService.handleWebhook(typebotDTO);
        return ResponseEntity.ok().body(response);
    }
}
