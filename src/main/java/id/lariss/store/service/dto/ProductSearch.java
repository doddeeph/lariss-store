package id.lariss.store.service.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearch {

    private String intent;
    private String[] productNames;
    private Attributes attributes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Attributes {

        private String color;
        private String processor; // MacBook / iMac
        private String memory; // MacBook / iMac
        private String storage; // General
        private String screen; // iPad
        private String connectivity; // iPad / Apple Watch
        private String material; // Apple Watch
        private String caseSize; // Apple Watch
        private String strapColor; // Apple Watch
        private List<String> strapSize; // Apple Watch
    }
}
