package ru.mk.quotationtinkoff.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.mk.quotationtinkoff.FigisDto;
import ru.mk.quotationtinkoff.dto.StockPricesDto;
import ru.mk.quotationtinkoff.dto.StocksDto;
import ru.mk.quotationtinkoff.dto.TickersDto;
import ru.mk.quotationtinkoff.exception.StockNotFoundException;
import ru.mk.quotationtinkoff.model.Currency;
import ru.mk.quotationtinkoff.model.Stock;
import ru.mk.quotationtinkoff.model.StockPrice;
import ru.mk.quotationtinkoff.service.interfaces.StockService;
import ru.tinkoff.invest.openapi.MarketContext;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrument;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrumentList;
import ru.tinkoff.invest.openapi.model.rest.Orderbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
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

    public CompletableFuture<Optional<Orderbook>> getPrice(String figi) {
        CompletableFuture<Optional<Orderbook>> cfOptionalOrderBook = openApi.getMarketContext().getMarketOrderbook(figi, 0);
        log.info("Getting price {} from TINKOFF INVEST API", figi);
        return cfOptionalOrderBook;
    }

    public StockPricesDto getStockPrices(FigisDto figisDto) {
        Long startTime = System.currentTimeMillis();
        List<CompletableFuture<Optional<Orderbook>>> list = new ArrayList<>();
        figisDto.getFigis().forEach(figi -> list.add(getPrice(figi)));
        List<StockPrice> stockPrices = list.stream()
                .map(CompletableFuture::join)
                .map(ob -> ob.orElseThrow(() -> new StockNotFoundException("Stock not found")))
                .map(orderbook -> new StockPrice(
                        orderbook.getFigi(),
                        orderbook.getLastPrice().doubleValue()
                ))
                .toList();
        log.info("time {}", System.currentTimeMillis()-startTime);
        return new StockPricesDto(stockPrices);


    }
}
