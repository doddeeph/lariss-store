package id.lariss.store.service.mapper;

import id.lariss.store.domain.enumeration.CurrencyCode;
import id.lariss.store.service.helper.CommonHelper;
import java.math.BigDecimal;

public interface FormattedPriceMapper<D, E> extends EntityMapper<D, E> {
    default String formattedPrice(BigDecimal price, CurrencyCode currencyCode) {
        return CommonHelper.formattedPrice(price, currencyCode);
    }
}
