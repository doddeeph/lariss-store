package id.lariss.store.service.impl.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.store.service.dto.TypebotDTO;
import id.lariss.store.service.v1.AsstCartService;
import id.lariss.store.service.v1.AsstOrderService;
import id.lariss.store.service.v1.AsstProductService;
import id.lariss.store.service.v1.TypebotService;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class TypebotServiceImpl implements TypebotService {

    private final OpenAiChatModel chatModel;
    private final AsstProductService productService;
    private final AsstCartService cartService;
    private final AsstOrderService orderService;

    public TypebotServiceImpl(
        OpenAiChatModel chatModel,
        AsstProductService productService,
        AsstCartService cartService,
        AsstOrderService orderService
    ) {
        this.chatModel = chatModel;
        this.productService = productService;
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @Override
    public Object handleWebhook(TypebotDTO typebotDTO) {
        switch (typebotDTO.getEvent()) {
            case SEARCH_PRODUCT -> {
                Prompt prompt = new Prompt(
                    """
                        Extract structured data from the following user input.
                        Return the result in JSON format with the following structure:
                        {
                            "productName": string
                        }
                        Remove this ```json```
                        User input: "%s"
                    """.formatted(typebotDTO.getUserInput())
                );
                String content = chatModel.call(prompt).getResult().getOutput().getContent();
                Map<String, Object> map;
                try {
                    map = new ObjectMapper().readValue(content, new TypeReference<>() {});
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                return productService.searchProduct(String.valueOf(map.get("productName")));
            }
            case VIEW_MY_CART -> {
                return cartService.getCart(typebotDTO.getCustomerId());
            }
            case VIEW_MY_ORDER -> {
                return orderService.getOrders(typebotDTO.getCustomerId());
            }
            default -> {
                return "";
            }
        }
    }
}
