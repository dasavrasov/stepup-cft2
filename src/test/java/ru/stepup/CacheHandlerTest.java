package ru.stepup;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CacheHandlerTest{

    @Test // ПРоверяем значение
    void valueTest(){
        double res;
        FractionTest fr = new FractionTest(2,3);
        Fractionable cachedFraction = (Fractionable)Utils.cache(fr);

        res=cachedFraction.doubleValue(); //Invoke doubleValue()
        assertEquals(0.6666666666666666, res); //0.6666666666666666
//        System.out.println("1 result "+res);
        res=cachedFraction.doubleValue();
        assertEquals(0.6666666666666666, res); //0.6666666666666666
//        System.out.println("2 result "+res);
        cachedFraction.setNum(5);
        res=cachedFraction.doubleValue(); //Invoke doubleValue()
//        System.out.println("3 result "+res);
        assertEquals(1.6666666666666667, res); //0.6666666666666666
    }

    @Test // ПРоверяем что значение берется из кеша
    void cacheTest(){
        double res1, res2, res3;
        FractionTest fr = new FractionTest(2,3);
        Fractionable cachedFraction = (Fractionable)Utils.cache(fr);

        res1=cachedFraction.doubleValue(); //Invoke doubleValue()
        assertEquals(FractionTest.count,1);
        res2=cachedFraction.doubleValue();
        assertEquals(FractionTest.count,1);
        res3=cachedFraction.doubleValue(); //Invoke doubleValue()
        assertEquals(FractionTest.count,1);
        assertEquals(res1, res2, res3); //0.6666666666666666
    }

    @Test // ПРоверяем что кеш очищается
    void clearTest(){
        double res;
        FractionTest fr = new FractionTest(2,3);
        Fractionable cachedFraction = (Fractionable)Utils.cache(fr);

        res=cachedFraction.doubleValue(); //Invoke doubleValue()
        assertEquals(FractionTest.count,1);
        res=cachedFraction.doubleValue();
        assertEquals(FractionTest.count,1);
        cachedFraction.setNum(5);
        res=cachedFraction.doubleValue(); //Invoke doubleValue()
        assertEquals(FractionTest.count,2);
        res=cachedFraction.doubleValue();
        assertEquals(FractionTest.count,2);
        cachedFraction.setDenum(5);
        res=cachedFraction.doubleValue();
        assertEquals(FractionTest.count,3);
    }
}
