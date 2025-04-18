package id.lariss.store.service.mapper;

import id.lariss.store.domain.enumeration.CurrencyCode;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public interface FormattedPriceMapper<D, E> extends EntityMapper<D, E> {
    default String formattedPrice(BigDecimal price, CurrencyCode currencyCode) {
        Locale locale =
            switch (currencyCode) {
                case USD -> Locale.US;
                case EUR -> Locale.FRANCE;
                case JPY -> Locale.JAPAN;
                default -> new Locale("id", "ID"); //IDR
            };
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        currencyFormat.setCurrency(Currency.getInstance(currencyCode.name()));
        return currencyFormat.format(price.doubleValue());
    }
}
