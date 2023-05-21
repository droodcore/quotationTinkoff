package ru.mk.quotationtinkoff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.mk.quotationtinkoff.FigisDto;
import ru.mk.quotationtinkoff.dto.StockPricesDto;
import ru.mk.quotationtinkoff.dto.StocksDto;
import ru.mk.quotationtinkoff.dto.TickersDto;
import ru.mk.quotationtinkoff.model.Stock;
import ru.mk.quotationtinkoff.service.interfaces.StockService;

@RestController
@RequestMapping("/api/1.0/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping("/{ticker}")
    public Stock getStock(@PathVariable String ticker) {
        return stockService.getStockByTicker(ticker);
    }

    @PostMapping("/stocks")
    public StocksDto getStocksByTickers(@RequestBody TickersDto tickersDto) {
       return stockService.getStocksByTickers(tickersDto);
    }

    @PostMapping("/prices")
    public StockPricesDto getPrices(@RequestBody FigisDto figisDto) {
        return stockService.getStockPrices(figisDto);
    }
}
