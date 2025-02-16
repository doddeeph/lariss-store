package id.lariss.store.service.mapper;

import static id.lariss.store.domain.CartItemAsserts.*;
import static id.lariss.store.domain.CartItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CartItemMapperTest {

    private CartItemMapper cartItemMapper;

    @BeforeEach
    void setUp() {
        cartItemMapper = new CartItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCartItemSample1();
        var actual = cartItemMapper.toEntity(cartItemMapper.toDto(expected));
        assertCartItemAllPropertiesEquals(expected, actual);
    }
}
