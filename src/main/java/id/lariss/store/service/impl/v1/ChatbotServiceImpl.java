package id.lariss.store.service.impl.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.store.service.CustomerService;
import id.lariss.store.service.dto.*;
import id.lariss.store.service.v1.CartService;
import id.lariss.store.service.v1.ChatbotService;
import id.lariss.store.service.v1.OrderService;
import id.lariss.store.service.v1.ProductService;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ChatbotServiceImpl implements ChatbotService {

    private static final Logger LOG = LoggerFactory.getLogger(ChatbotServiceImpl.class);

    private final OpenAiChatModel chatModel;
    private final ProductService productService;
    private final CartService cartService;
    private final OrderService orderService;
    private final CustomerService customerService;

    public ChatbotServiceImpl(
        OpenAiChatModel chatModel,
        ProductService productService,
        CartService cartService,
        OrderService orderService,
        CustomerService customerService
    ) {
        this.chatModel = chatModel;
        this.productService = productService;
        this.cartService = cartService;
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @Override
    public Object webhook(ChatbotDTO chatbotDTO) {
        LOG.info("--> webhook request: {}", chatbotDTO);
        switch (chatbotDTO.getEvent()) {
            case VALIDATE_CUSTOMER -> {
                String phoneNumber = String.valueOf(chatbotDTO.getRequest().get("phoneNumber"));
                return validateCustomer(phoneNumber);
            }
            case REGISTER_CUSTOMER -> {
                String fullName = String.valueOf(chatbotDTO.getRequest().get("fullName"));
                String phoneNumber = String.valueOf(chatbotDTO.getRequest().get("phoneNumber"));
                return registerCustomer(fullName, phoneNumber);
            }
            case SEARCH_PRODUCT -> {
                String userInput = String.valueOf(chatbotDTO.getRequest().get("userInput"));
                return searchProduct(userInput);
            }
            case VIEW_MY_CART -> {
                Long customerId = Long.valueOf(String.valueOf(chatbotDTO.getRequest().getOrDefault("customerId", "0")));
                return viewMyCart(customerId);
            }
            case VIEW_MY_ORDER -> {
                Long customerId = Long.valueOf(String.valueOf(chatbotDTO.getRequest().getOrDefault("customerId", "0")));
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
        Prompt prompt = new Prompt(searchProductPrompt(userInput));
        String content = chatModel.call(prompt).getResult().getOutput().getContent();
        return productService.searchProduct(productSearchContentMap(content));
    }

    private String searchProductPrompt(String userInput) {
        return """
            Extract structured data from the following user input.
            Determine if the user is searching for the 'cheapest', 'most expensive', or simply wants to 'search' for a product.
            Support Apple products including: MacBook, iMac, iPad, iPhone, Apple Watch, and AirPods.

            Always return `productNames` as an array of strings, even if the user mentions only one product.
            If the user mentions a product with a specific model name (e.g., "iPhone 15", "MacBook Air", "MacBook Pro"), include the full name as a product name (e.g., `"iPhone 15"`, `"MacBook Air"`, `"MacBook Pro"`), not just the base category like "iPhone" or "MacBook".

            Extract product-specific attributes as follows:
            - MacBook & iMac:
              - color
              - processor
              - memory (RAM, e.g., '48 GB')
              - storage (e.g., '256 GB', '1 TB')
            - iPad:
              - color
              - storage
              - screen (e.g., 'standard glass', 'nano-texture glass')
              - connectivity (e.g., 'Wi-Fi', 'Wi-Fi + Cellular')
            - iPhone:
              - color
              - storage
            - Apple Watch:
              - color
              - connectivity (e.g., 'GPS')
              - material (e.g., 'aluminum', 'titanium')
              - caseSize (e.g., '40 mm', '49 mm')
              - strapColor
              - strapSize (e.g., 'Medium', 'S/M', 'M/L')

            All extracted attribute values must be returned in English. Translate or normalize non-English terms or ambiguous expressions into standard English terms.

            If the product is Apple Watch and the user mentions a strap size, apply the following transformations to the `strapSize` field:
            - If user mentions "small", set `"strapSize"` to `["Small", "S/M"]`
            - If user mentions "medium", set `"strapSize"` to `["Medium", "S/M", "M/L"]`
            - If user mentions "large", set `"strapSize"` to `["Large", "M/L"]`

            Special rule for Apple Watch:
            - If the user provides only the color and does not mention strapColor, fill only the "color" field and leave "strapColor" undefined.
            - If the user mentions a color value such as "Blue Titanium", "Storm Blue", or "Space Grey", include the full color (e.g., `"Blue Titanium"`, `"Storm Blue"`, `"Space Grey"`), not just the base color like "Blue" or "Grey".

            Return only a raw JSON object. Do not include any markdown formatting, triple backticks, or code block indicators. Just return plain JSON text.

            Ensure the output is structured as follows:
            {
                "intent": string with one of these values: 'cheapest', 'most expensive', or 'search',
                "productNames": array of strings,
                "attributes": {
                    "color": string (optional),
                    "processor": string (optional, for MacBook/iMac),
                    "memory": string (optional, for MacBook/iMac),
                    "storage": string (optional),
                    "screen": string (optional, for iPad),
                    "connectivity": string (optional, for iPad/Apple Watch),
                    "material": string (optional, for Apple Watch),
                    "caseSize": string (optional, for Apple Watch),
                    "strapColor": string (optional, for Apple Watch),
                    "strapSize": array of strings (optional, for Apple Watch, according to rules above)
                }
            }

            User input: "%s"
        """.formatted(userInput);
    }

    private ProductSearch productSearchContentMap(String content) {
        try {
            return new ObjectMapper().readValue(content, ProductSearch.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
