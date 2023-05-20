package ru.mk.quotationtinkoff.service.interfaces;

import ru.mk.quotationtinkoff.model.Stock;

public interface StockService {

    Stock getStockByTicker(String ticker);
}
