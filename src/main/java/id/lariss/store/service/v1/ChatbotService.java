package id.lariss.store.service.v1;

import id.lariss.store.service.dto.ChatbotDTO;

public interface ChatbotService {
    Object webhook(ChatbotDTO chatbotDTO);
}
