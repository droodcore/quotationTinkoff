package ru.mk.quotationtinkoff.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mk.quotationtinkoff.model.Stock;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StocksDto {
    List<Stock> stocks;
}
