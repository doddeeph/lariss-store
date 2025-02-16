package id.lariss.store.domain;

import static id.lariss.store.domain.CategoryTestSamples.*;
import static id.lariss.store.domain.ProductTestSamples.*;
import static id.lariss.store.domain.ProductVariantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.store.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void productVariantTest() {
        Product product = getProductRandomSampleGenerator();
        ProductVariant productVariantBack = getProductVariantRandomSampleGenerator();

        product.addProductVariant(productVariantBack);
        assertThat(product.getProductVariants()).containsOnly(productVariantBack);
        assertThat(productVariantBack.getProduct()).isEqualTo(product);

        product.removeProductVariant(productVariantBack);
        assertThat(product.getProductVariants()).doesNotContain(productVariantBack);
        assertThat(productVariantBack.getProduct()).isNull();

        product.productVariants(new HashSet<>(Set.of(productVariantBack)));
        assertThat(product.getProductVariants()).containsOnly(productVariantBack);
        assertThat(productVariantBack.getProduct()).isEqualTo(product);

        product.setProductVariants(new HashSet<>());
        assertThat(product.getProductVariants()).doesNotContain(productVariantBack);
        assertThat(productVariantBack.getProduct()).isNull();
    }

    @Test
    void categoryTest() {
        Product product = getProductRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        product.setCategory(categoryBack);
        assertThat(product.getCategory()).isEqualTo(categoryBack);

        product.category(null);
        assertThat(product.getCategory()).isNull();
    }
}
