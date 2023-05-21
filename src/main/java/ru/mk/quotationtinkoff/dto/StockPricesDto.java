package ru.mk.quotationtinkoff.dto;

import lombok.AllArgsConstructor;
import lombok.Value;
import ru.mk.quotationtinkoff.model.StockPrice;

import java.util.List;

@AllArgsConstructor
@Value
public class StockPricesDto {
    List<StockPrice> prices;
}
