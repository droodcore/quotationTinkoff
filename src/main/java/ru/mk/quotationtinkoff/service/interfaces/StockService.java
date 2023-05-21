package ru.mk.quotationtinkoff.service.interfaces;

import ru.mk.quotationtinkoff.dto.StocksDto;
import ru.mk.quotationtinkoff.dto.TickersDto;
import ru.mk.quotationtinkoff.model.Stock;

public interface StockService {

    Stock getStockByTicker(String ticker);

    StocksDto getStocksByTickers(TickersDto tickersDto);
}
