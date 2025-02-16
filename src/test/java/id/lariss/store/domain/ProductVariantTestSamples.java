package id.lariss.store.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductVariantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProductVariant getProductVariantSample1() {
        return new ProductVariant()
            .id(1L)
            .imageUrl("imageUrl1")
            .color("color1")
            .processor("processor1")
            .memory("memory1")
            .storage("storage1")
            .screen("screen1")
            .connectivity("connectivity1")
            .material("material1")
            .caseSize("caseSize1")
            .strapColor("strapColor1")
            .strapSize("strapSize1");
    }

    public static ProductVariant getProductVariantSample2() {
        return new ProductVariant()
            .id(2L)
            .imageUrl("imageUrl2")
            .color("color2")
            .processor("processor2")
            .memory("memory2")
            .storage("storage2")
            .screen("screen2")
            .connectivity("connectivity2")
            .material("material2")
            .caseSize("caseSize2")
            .strapColor("strapColor2")
            .strapSize("strapSize2");
    }

    public static ProductVariant getProductVariantRandomSampleGenerator() {
        return new ProductVariant()
            .id(longCount.incrementAndGet())
            .imageUrl(UUID.randomUUID().toString())
            .color(UUID.randomUUID().toString())
            .processor(UUID.randomUUID().toString())
            .memory(UUID.randomUUID().toString())
            .storage(UUID.randomUUID().toString())
            .screen(UUID.randomUUID().toString())
            .connectivity(UUID.randomUUID().toString())
            .material(UUID.randomUUID().toString())
            .caseSize(UUID.randomUUID().toString())
            .strapColor(UUID.randomUUID().toString())
            .strapSize(UUID.randomUUID().toString());
    }
}
