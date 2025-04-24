package id.lariss.store.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OrderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Order getOrderSample1() {
        return new Order().id(1L).shippingAddress("shippingAddress1");
    }

    public static Order getOrderSample2() {
        return new Order().id(2L).shippingAddress("shippingAddress2");
    }

    public static Order getOrderRandomSampleGenerator() {
        return new Order().id(longCount.incrementAndGet()).shippingAddress(UUID.randomUUID().toString());
    }
}
