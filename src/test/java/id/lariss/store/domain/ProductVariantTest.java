package id.lariss.store.domain;

import static id.lariss.store.domain.CartItemTestSamples.*;
import static id.lariss.store.domain.OrderItemTestSamples.*;
import static id.lariss.store.domain.ProductTestSamples.*;
import static id.lariss.store.domain.ProductVariantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.store.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductVariantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductVariant.class);
        ProductVariant productVariant1 = getProductVariantSample1();
        ProductVariant productVariant2 = new ProductVariant();
        assertThat(productVariant1).isNotEqualTo(productVariant2);

        productVariant2.setId(productVariant1.getId());
        assertThat(productVariant1).isEqualTo(productVariant2);

        productVariant2 = getProductVariantSample2();
        assertThat(productVariant1).isNotEqualTo(productVariant2);
    }

    @Test
    void orderItemTest() {
        ProductVariant productVariant = getProductVariantRandomSampleGenerator();
        OrderItem orderItemBack = getOrderItemRandomSampleGenerator();

        productVariant.addOrderItem(orderItemBack);
        assertThat(productVariant.getOrderItems()).containsOnly(orderItemBack);
        assertThat(orderItemBack.getProductVariant()).isEqualTo(productVariant);

        productVariant.removeOrderItem(orderItemBack);
        assertThat(productVariant.getOrderItems()).doesNotContain(orderItemBack);
        assertThat(orderItemBack.getProductVariant()).isNull();

        productVariant.orderItems(new HashSet<>(Set.of(orderItemBack)));
        assertThat(productVariant.getOrderItems()).containsOnly(orderItemBack);
        assertThat(orderItemBack.getProductVariant()).isEqualTo(productVariant);

        productVariant.setOrderItems(new HashSet<>());
        assertThat(productVariant.getOrderItems()).doesNotContain(orderItemBack);
        assertThat(orderItemBack.getProductVariant()).isNull();
    }

    @Test
    void cartItemTest() {
        ProductVariant productVariant = getProductVariantRandomSampleGenerator();
        CartItem cartItemBack = getCartItemRandomSampleGenerator();

        productVariant.setCartItem(cartItemBack);
        assertThat(productVariant.getCartItem()).isEqualTo(cartItemBack);
        assertThat(cartItemBack.getProductVariant()).isEqualTo(productVariant);

        productVariant.cartItem(null);
        assertThat(productVariant.getCartItem()).isNull();
        assertThat(cartItemBack.getProductVariant()).isNull();
    }

    @Test
    void productTest() {
        ProductVariant productVariant = getProductVariantRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        productVariant.setProduct(productBack);
        assertThat(productVariant.getProduct()).isEqualTo(productBack);

        productVariant.product(null);
        assertThat(productVariant.getProduct()).isNull();
    }
}
