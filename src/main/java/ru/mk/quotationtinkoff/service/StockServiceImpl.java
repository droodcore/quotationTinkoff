package ru.mk.quotationtinkoff.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.mk.quotationtinkoff.dto.StocksDto;
import ru.mk.quotationtinkoff.dto.TickersDto;
import ru.mk.quotationtinkoff.exception.StockNotFoundException;
import ru.mk.quotationtinkoff.model.Currency;
import ru.mk.quotationtinkoff.model.Stock;
import ru.mk.quotationtinkoff.service.interfaces.StockService;
import ru.tinkoff.invest.openapi.MarketContext;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrument;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrumentList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {
    private final OpenApi openApi;

    @Async
    public CompletableFuture<MarketInstrumentList> getMarketInstrumentByTicker(String ticker) {
        MarketContext marketContext = openApi.getMarketContext();
        return marketContext.searchMarketInstrumentsByTicker(ticker);
    }

    @Override
    public Stock getStockByTicker(String ticker) {
        CompletableFuture<MarketInstrumentList> listCompletableFuture = getMarketInstrumentByTicker(ticker);
        List<MarketInstrument> list = listCompletableFuture.join().getInstruments();
        if (list.isEmpty()) {
            throw new StockNotFoundException(String.format("Stock %S not found.", ticker));
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

    public StocksDto getStocksByTickers(TickersDto tickersDto) {
        List<CompletableFuture<MarketInstrumentList>> marketInstrument = new ArrayList<>();
        tickersDto.getTickers().forEach(ticker -> marketInstrument.add(getMarketInstrumentByTicker(ticker)));
        List<Stock> stocks = marketInstrument.stream()
                .map(CompletableFuture::join)
                .map(mi -> {
                    if(!mi.getInstruments().isEmpty())
                        return mi.getInstruments().get(0);
                    return null;
                })
                .filter(Objects::nonNull)
                .map(mi -> new Stock(
                        mi.getTicker(),
                        mi.getFigi(),
                        mi.getName(),
                        mi.getType().getValue(),
                        Currency.valueOf(mi.getCurrency().getValue()),
                        "TINKOFF INVEST API"))
                .toList();
        return new StocksDto(stocks);
    }
}
