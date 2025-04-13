package id.lariss.store.web.rest.v1;

import com.fasterxml.jackson.annotation.JsonView;
import id.lariss.store.service.dto.ChatbotDTO;
import id.lariss.store.service.dto.Views;
import id.lariss.store.service.v1.ChatbotService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/chatbot")
public class ChatbotResource {

    private final ChatbotService chatbotService;

    public ChatbotResource(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping("/webhook")
    @JsonView(Views.Public.class)
    public ResponseEntity<Object> webhook(@Valid @RequestBody ChatbotDTO chatbotDTO) {
        Object response = chatbotService.webhook(chatbotDTO);
        return ResponseEntity.ok().body(response);
    }
}
