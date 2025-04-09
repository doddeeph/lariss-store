package id.lariss.store.service.v1;

import id.lariss.store.service.dto.WebhookDTO;

public interface AiService {
    Object handleWebhook(WebhookDTO webhookDTO);
}
