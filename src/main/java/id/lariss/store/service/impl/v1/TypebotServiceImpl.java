package id.lariss.store.service.impl.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.store.service.dto.TypebotDTO;
import id.lariss.store.service.v1.AsstCartService;
import id.lariss.store.service.v1.AsstOrderService;
import id.lariss.store.service.v1.AsstProductService;
import id.lariss.store.service.v1.TypebotService;
import java.util.HashSet;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class TypebotServiceImpl implements TypebotService {

    private static final Logger LOG = LoggerFactory.getLogger(TypebotServiceImpl.class);

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
                return searchProduct(typebotDTO);
            }
            case VIEW_MY_CART -> {
                return cartService.getCart(typebotDTO.getCustomerId());
            }
            case VIEW_MY_ORDER -> {
                return orderService.getOrders(typebotDTO.getCustomerId());
            }
            default -> {
                return new HashSet<>();
            }
        }
    }

    private Object searchProduct(TypebotDTO typebotDTO) {
        Prompt prompt = new Prompt(promptContent().formatted(typebotDTO.getUserInput()));
        String content = chatModel.call(prompt).getResult().getOutput().getContent();
        Map<String, String> contentMap = contentMap(content);
        LOG.info("--> searchProduct, contentMap: {}", contentMap);
        String intent = contentMap.getOrDefault("intent", "");
        String productName = contentMap.getOrDefault("productName", "");
        if (StringUtils.isNotBlank(intent) && StringUtils.isNotBlank(productName)) {
            return switch (intent) {
                case "cheapest" -> productService.findCheapest(productName);
                case "most expensive" -> productService.findMostExpensive(productName);
                default -> new HashSet<>();
            };
        } else if (StringUtils.isNotBlank(intent)) {
            return switch (intent) {
                case "cheapest" -> productService.findCheapest();
                case "most expensive" -> productService.findMostExpensive();
                default -> new HashSet<>();
            };
        } else {
            return productService.searchProduct(productName);
        }
    }

    private String promptContent() {
        return """
            Extract structured data from the following user input.
            Determine if they are searching for the 'cheapest' or 'most expensive' product.
            Return only a JSON object. Do not include any markdown formatting, just raw JSON.
            Ensure the output is structured as follows:
            {
                "intent": string with the 'cheapest' or 'most expensive' value,
                "productName": string
            }
            Remove this ```json ```
            User input: "%s"
        """;
    }

    private Map<String, String> contentMap(String content) {
        try {
            return new ObjectMapper().readValue(content, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
