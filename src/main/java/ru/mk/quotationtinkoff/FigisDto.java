package ru.mk.quotationtinkoff;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.aop.target.LazyInitTargetSource;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class FigisDto {
    List<String> figis;
}
