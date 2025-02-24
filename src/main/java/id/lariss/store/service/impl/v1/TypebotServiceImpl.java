package id.lariss.store.service.impl.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.store.service.CustomerService;
import id.lariss.store.service.dto.CustomerDTO;
import id.lariss.store.service.dto.ProductSearchDTO;
import id.lariss.store.service.dto.TypebotDTO;
import id.lariss.store.service.v1.AsstCartService;
import id.lariss.store.service.v1.AsstOrderService;
import id.lariss.store.service.v1.AsstProductService;
import id.lariss.store.service.v1.TypebotService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private final CustomerService customerService;

    public TypebotServiceImpl(
        OpenAiChatModel chatModel,
        AsstProductService productService,
        AsstCartService cartService,
        AsstOrderService orderService,
        CustomerService customerService
    ) {
        this.chatModel = chatModel;
        this.productService = productService;
        this.cartService = cartService;
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @Override
    public Object handleWebhook(TypebotDTO typebotDTO) {
        switch (typebotDTO.getEvent()) {
            case REGISTER -> {
                String fullName = String.valueOf(typebotDTO.getRequest().get("fullName"));
                String phoneNumber = String.valueOf(typebotDTO.getRequest().get("phoneNumber"));
                return register(fullName, phoneNumber);
            }
            case SEARCH_PRODUCT -> {
                String userInput = String.valueOf(typebotDTO.getRequest().get("userInput"));
                return searchProduct(userInput);
            }
            case VIEW_MY_CART -> {
                Long customerId = Long.valueOf(String.valueOf(typebotDTO.getRequest().getOrDefault("customerId", "0")));
                return cartService.getCart(customerId);
            }
            case VIEW_MY_ORDER -> {
                Long customerId = Long.valueOf(String.valueOf(typebotDTO.getRequest().getOrDefault("customerId", "0")));
                return orderService.getOrders(customerId);
            }
            default -> {
                return new HashSet<>();
            }
        }
    }

    private CustomerDTO register(String fullName, String phoneNumber) {
        Prompt prompt = new Prompt(fullNamePrompt(fullName));
        String content = chatModel.call(prompt).getResult().getOutput().getContent();
        Map<String, String> contentMap = contentMap(content);
        LOG.info("--> register, contentMap: {}", contentMap);
        String firstName = contentMap.getOrDefault("firstName", "");
        String lastName = contentMap.getOrDefault("lastName", "");
        return customerService.upsertByPhoneNumber(
            CustomerDTO.builder().firstName(firstName).lastName(lastName).phoneNumber(phoneNumber).build()
        );
    }

    private String fullNamePrompt(String fullName) {
        return """
        Convert the following unstructured full name into JSON format containing "firstName" and "lastName".
        If only one name is provided, assume it is the first name.
        Return only a JSON object. Do not include any markdown formatting, just raw JSON.
        Ensure the output is structured as follows:
        {
            "firstName": string,
            "lastName": string
        }
        Full name: "%s"
        """.formatted(fullName);
    }

    private List<ProductSearchDTO> searchProduct(String userInput) {
        Prompt prompt = new Prompt(searchProductPrompt().formatted(userInput));
        String content = chatModel.call(prompt).getResult().getOutput().getContent();
        Map<String, String> contentMap = contentMap(content);
        LOG.info("--> searchProduct, contentMap: {}", contentMap);

        String intent = contentMap.getOrDefault("intent", "");
        String productName = contentMap.getOrDefault("productName", "");

        if (StringUtils.isNotBlank(intent) && StringUtils.isNotBlank(productName)) {
            return switch (intent) {
                case "cheapest" -> productService.findCheapest(productName);
                case "most expensive" -> productService.findMostExpensive(productName);
                default -> new ArrayList<>();
            };
        } else if (StringUtils.isNotBlank(intent)) {
            return switch (intent) {
                case "cheapest" -> productService.findCheapest();
                case "most expensive" -> productService.findMostExpensive();
                default -> new ArrayList<>();
            };
        } else {
            return productService.searchProduct(productName);
        }
    }

    private String searchProductPrompt() {
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
