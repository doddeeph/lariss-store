package id.lariss.store.domain;

import static id.lariss.store.domain.CartItemTestSamples.*;
import static id.lariss.store.domain.CartTestSamples.*;
import static id.lariss.store.domain.CustomerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.store.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CartTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cart.class);
        Cart cart1 = getCartSample1();
        Cart cart2 = new Cart();
        assertThat(cart1).isNotEqualTo(cart2);

        cart2.setId(cart1.getId());
        assertThat(cart1).isEqualTo(cart2);

        cart2 = getCartSample2();
        assertThat(cart1).isNotEqualTo(cart2);
    }

    @Test
    void customerTest() {
        Cart cart = getCartRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        cart.setCustomer(customerBack);
        assertThat(cart.getCustomer()).isEqualTo(customerBack);

        cart.customer(null);
        assertThat(cart.getCustomer()).isNull();
    }

    @Test
    void cartItemTest() {
        Cart cart = getCartRandomSampleGenerator();
        CartItem cartItemBack = getCartItemRandomSampleGenerator();

        cart.addCartItem(cartItemBack);
        assertThat(cart.getCartItems()).containsOnly(cartItemBack);
        assertThat(cartItemBack.getCart()).isEqualTo(cart);

        cart.removeCartItem(cartItemBack);
        assertThat(cart.getCartItems()).doesNotContain(cartItemBack);
        assertThat(cartItemBack.getCart()).isNull();

        cart.cartItems(new HashSet<>(Set.of(cartItemBack)));
        assertThat(cart.getCartItems()).containsOnly(cartItemBack);
        assertThat(cartItemBack.getCart()).isEqualTo(cart);

        cart.setCartItems(new HashSet<>());
        assertThat(cart.getCartItems()).doesNotContain(cartItemBack);
        assertThat(cartItemBack.getCart()).isNull();
    }
}
