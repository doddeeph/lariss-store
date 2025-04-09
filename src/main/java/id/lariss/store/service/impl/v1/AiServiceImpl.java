package id.lariss.store.service.impl.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.store.service.CustomerService;
import id.lariss.store.service.dto.*;
import id.lariss.store.service.v1.AiService;
import id.lariss.store.service.v1.CartService;
import id.lariss.store.service.v1.OrderService;
import id.lariss.store.service.v1.ProductSearchService;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AiServiceImpl implements AiService {

    private static final Logger LOG = LoggerFactory.getLogger(AiServiceImpl.class);

    private final OpenAiChatModel chatModel;
    private final ProductSearchService productSearchService;
    private final CartService cartService;
    private final OrderService orderService;
    private final CustomerService customerService;

    public AiServiceImpl(
        OpenAiChatModel chatModel,
        ProductSearchService productSearchService,
        CartService cartService,
        OrderService orderService,
        CustomerService customerService
    ) {
        this.chatModel = chatModel;
        this.productSearchService = productSearchService;
        this.cartService = cartService;
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @Override
    public Object handleWebhook(WebhookDTO webhookDTO) {
        LOG.info("--> handleWebhook, request: {}", webhookDTO);
        switch (webhookDTO.getEvent()) {
            case VALIDATE_CUSTOMER -> {
                String phoneNumber = String.valueOf(webhookDTO.getRequest().get("phoneNumber"));
                return validateCustomer(phoneNumber);
            }
            case REGISTER_CUSTOMER -> {
                String fullName = String.valueOf(webhookDTO.getRequest().get("fullName"));
                String phoneNumber = String.valueOf(webhookDTO.getRequest().get("phoneNumber"));
                return registerCustomer(fullName, phoneNumber);
            }
            case SEARCH_PRODUCT -> {
                String userInput = String.valueOf(webhookDTO.getRequest().get("userInput"));
                return searchProduct(userInput);
            }
            case VIEW_MY_CART -> {
                Long customerId = Long.valueOf(String.valueOf(webhookDTO.getRequest().getOrDefault("customerId", "0")));
                return viewMyCart(customerId);
            }
            case VIEW_MY_ORDER -> {
                Long customerId = Long.valueOf(String.valueOf(webhookDTO.getRequest().getOrDefault("customerId", "0")));
                return viewMyOrder(customerId);
            }
            default -> {
                return new HashSet<>();
            }
        }
    }

    private CustomerDTO validateCustomer(String phoneNumber) {
        return customerService.findOneByPhoneNumber(phoneNumber).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    private CustomerDTO registerCustomer(String fullName, String phoneNumber) {
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
                case "cheapest" -> productSearchService.findCheapest(productName);
                case "most expensive" -> productSearchService.findMostExpensive(productName);
                default -> new ArrayList<>();
            };
        } else if (StringUtils.isNotBlank(intent)) {
            return switch (intent) {
                case "cheapest" -> productSearchService.findCheapest();
                case "most expensive" -> productSearchService.findMostExpensive();
                default -> new ArrayList<>();
            };
        } else {
            return productSearchService.searchProduct(productName);
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

    private CartDTO viewMyCart(Long customerId) {
        LOG.info("--> viewMyCart, customerId: {}", customerId);
        return cartService.getCart(customerId);
    }

    private Set<OrderDTO> viewMyOrder(Long customerId) {
        LOG.info("--> viewMyOrder, customerId: {}", customerId);
        return orderService.getOrders(customerId);
    }
}
