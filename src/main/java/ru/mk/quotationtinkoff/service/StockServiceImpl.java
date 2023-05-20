package ru.mk.quotationtinkoff.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mk.quotationtinkoff.exception.StockNotFoundException;
import ru.mk.quotationtinkoff.model.Currency;
import ru.mk.quotationtinkoff.model.Stock;
import ru.mk.quotationtinkoff.service.interfaces.StockService;
import ru.tinkoff.invest.openapi.MarketContext;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrument;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrumentList;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {
    private final OpenApi openApi;
    @Override
    public Stock getStockByTicker(String ticker) {
        MarketContext marketContext = openApi.getMarketContext();
        CompletableFuture<MarketInstrumentList> listCompletableFuture = marketContext.searchMarketInstrumentsByTicker(ticker);
        List<MarketInstrument> list = listCompletableFuture.join().getInstruments();
        if (list.isEmpty()) {
            throw  new StockNotFoundException(String.format("Stock %S not found.", ticker));
        }

        MarketInstrument item = list.get(0);
        return new Stock(
                item.getTicker(),
                item.getFigi(),
                item.getName(),
                item.getType().getValue(),
                Currency.valueOf(item.getCurrency().getValue()),
                "TINKOFF INVEST API");

    }

}
