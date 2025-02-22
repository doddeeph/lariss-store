package id.lariss.store.service.v1;

import id.lariss.store.service.dto.TypebotDTO;

public interface TypebotService {
    Object handleWebhook(TypebotDTO typebotDTO);
}
