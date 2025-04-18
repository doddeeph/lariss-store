package id.lariss.store.domain;

import static id.lariss.store.domain.CartItemTestSamples.*;
import static id.lariss.store.domain.CartTestSamples.*;
import static id.lariss.store.domain.ProductVariantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.store.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CartItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CartItem.class);
        CartItem cartItem1 = getCartItemSample1();
        CartItem cartItem2 = new CartItem();
        assertThat(cartItem1).isNotEqualTo(cartItem2);

        cartItem2.setId(cartItem1.getId());
        assertThat(cartItem1).isEqualTo(cartItem2);

        cartItem2 = getCartItemSample2();
        assertThat(cartItem1).isNotEqualTo(cartItem2);
    }

    @Test
    void productVariantTest() {
        CartItem cartItem = getCartItemRandomSampleGenerator();
        ProductVariant productVariantBack = getProductVariantRandomSampleGenerator();

        cartItem.setProductVariant(productVariantBack);
        assertThat(cartItem.getProductVariant()).isEqualTo(productVariantBack);

        cartItem.productVariant(null);
        assertThat(cartItem.getProductVariant()).isNull();
    }

    @Test
    void cartTest() {
        CartItem cartItem = getCartItemRandomSampleGenerator();
        Cart cartBack = getCartRandomSampleGenerator();

        cartItem.setCart(cartBack);
        assertThat(cartItem.getCart()).isEqualTo(cartBack);

        cartItem.cart(null);
        assertThat(cartItem.getCart()).isNull();
    }
}
