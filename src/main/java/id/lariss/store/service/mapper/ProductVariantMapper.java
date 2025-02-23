package id.lariss.store.service.mapper;

import id.lariss.store.domain.Product;
import id.lariss.store.domain.ProductVariant;
import id.lariss.store.service.dto.ProductDTO;
import id.lariss.store.service.dto.ProductVariantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductVariant} and its DTO {@link ProductVariantDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductVariantMapper extends FormattedPriceMapper<ProductVariantDTO, ProductVariant> {
    @Mapping(target = "formattedPrice", expression = "java(formattedPrice(s.getPrice(), s.getProduct().getCurrencyCode()))")
    @Mapping(target = "product", source = "product", qualifiedByName = "productProductName")
    ProductVariantDTO toDto(ProductVariant s);

    @Named("productProductName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "productName", source = "productName")
    ProductDTO toDtoProductProductName(Product product);
}
