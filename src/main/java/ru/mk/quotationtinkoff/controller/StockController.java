package ru.mk.quotationtinkoff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/getStocksByTicker")
    public StocksDto getStocksByTickers(@RequestBody TickersDto tickersDto) {
       return stockService.getStocksByTickers(tickersDto);
    }
}
