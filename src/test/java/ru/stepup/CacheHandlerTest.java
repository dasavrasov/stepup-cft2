package ru.stepup;

import org.junit.jupiter.api.Test;

public class CacheHandlerTest {
    @Test
    void cacheTest() {
        //как в тесте проверить, что значение возвращается из кэша
        //друстимо ли ставить аннотации КЭШ и МУТАТОР на интерфейсе Fractionable
        //если нет, то как проверить что объекта стоит аннотация КЭШ
        //или МУТАТОР если в метод передается интерфейсная ссыока
        double res;
        Fraction fr = new Fraction(2,3);
        Fractionable cachedFraction = (Fractionable)Utils.cache(fr);

        res=cachedFraction.doubleValue(); //Invoke doubleValue()
        System.out.println("1 result "+res);
        res=cachedFraction.doubleValue();
        System.out.println("2 result "+res);
        cachedFraction.setNum(5);
        res=cachedFraction.doubleValue(); //Invoke doubleValue()
        System.out.println("3 result "+res);
    }
}
