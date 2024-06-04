package ru.stepup;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CacheHandlerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }
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
        assertEquals(0.6666666666666666, res); //0.6666666666666666
        assertEquals("Invoke doubleValue()\r\n", outContent.toString());
//        System.out.println("1 result "+res);
        outContent.reset();
        res=cachedFraction.doubleValue();
        assert(outContent.toString().isEmpty());
        assertEquals(0.6666666666666666, res); //0.6666666666666666
//        System.out.println("2 result "+res);
        cachedFraction.setNum(5);
        res=cachedFraction.doubleValue(); //Invoke doubleValue()
        assertEquals("Invoke doubleValue()\r\n", outContent.toString());
//        System.out.println("3 result "+res);
        assertEquals(1.6666666666666667, res); //0.6666666666666666
    }
}
