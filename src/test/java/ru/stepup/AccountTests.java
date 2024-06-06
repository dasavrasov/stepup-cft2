package ru.stepup;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountTests {

    //Задание 1 - создание
    @Test
    @DisplayName("Тест создания счета")
    public void TestAccountCreate() {
        Account account = new Account("Иванов Иван Иванович");
        assert (account.getName().equals("Иванов Иван Иванович"));
        //Имя не может быть null или пустым
        assertThrows(IllegalArgumentException.class, () -> new Account(null));
        assertThrows(IllegalArgumentException.class, () -> new Account(""));
    }

    //Задание 1 - изменение количества валюты
    @Test
    @DisplayName("Тест изменения количества валюта на счета")
    public void TestСhangeCurrencyAmount() {
        Account account = new Account("Иванов Иван Иванович");
        //Валюта не может быть null
        assertThrows(IllegalArgumentException.class, () -> account.changeCurrencyAmount(null, 100));
        account.changeCurrencyAmount(Currency.USD, 100);
        assertEquals (100,account.getValues().get(Currency.USD)) ;
        //Количество не может быть отрицательным
        assertThrows(IllegalArgumentException.class, () -> account.changeCurrencyAmount(Currency.USD, -100));
        //Изменение количества USD
        account.changeCurrencyAmount(Currency.USD, 200);
        assertEquals (200,account.getValues().get(Currency.USD));
        //Если такой валюты еще нет - добавляем в список
        account.changeCurrencyAmount(Currency.EUR, 300);
        assertEquals (300,account.getValues().get(Currency.EUR)) ;
    }

    //Задание 2 - Отмена
    @Test
    @DisplayName("Тест undo Имени")
    public void TestUndoName() {
        Account account = new Account("Иванов Иван Иванович");
        //добавили 100 рублей
        account.changeCurrencyAmount(Currency.RUB, 100);
        assertEquals (100,account.getValues().get(Currency.RUB));
        //меняем имя на Василий Иванов
        account.setName("Василий Иванов");
        assertEquals ("Василий Иванов",account.getName());
        account.setName("Василий Сидоров");
        assertEquals ("Василий Сидоров",account.getName());
        account.undo();
        assertEquals ("Василий Иванов",account.getName());
        account.undo();
        assertEquals ("Иванов Иван Иванович",account.getName());
    }
    @Test
    @DisplayName("Тест undo изменения количества валюты")
    public void TestUndoChangeCurrency() {
        Account account = new Account("Иванов Иван Иванович");
        //добавили 100 рублей
        account.changeCurrencyAmount(Currency.RUB, 100);
        assertEquals (100,account.getValues().get(Currency.RUB));
        //установили количество рублей 300
        account.changeCurrencyAmount(Currency.RUB, 300);
        assertEquals (300,account.getValues().get(Currency.RUB));

        account.undo(); //изменение суммы
        assertEquals(100,account.getValues().get(Currency.RUB));

        account.undo(); //удаление валюты
        assert (account.getValues().get(Currency.RUB) == null);
    }

    //Задание 3 - сохранение снапшота
    @Test
    @DisplayName("Тест сохранения снапшота")
    public void TestSaveToSnapshot(){
        Account account = new Account("Иванов Иван Иванович");
        //добавили 100 рублей
        account.changeCurrencyAmount(Currency.RUB, 100);
        Save snapshot1 = account.save(); //сохранили снапшот
        //пробуем изменить shapshot1 - проверка на иммутабельность
        account.changeCurrencyAmount(Currency.RUB, 200); //изменение оригинального объекта не влияет на сохранение
        snapshot1.load();
        assertEquals (100,account.getValues().get(Currency.RUB));

//        shapshot1.getValues().put(Currency.RUB, 200);
//        assertEquals (100,shapshot1.getValues().get(Currency.RUB));
//        //Изменения оригинального объекта Account также не оказывают влияния на Сохранение
//        account.changeCurrencyAmount(Currency.RUB, 300);
//        assert (account.getValues().get(Currency.RUB) == 300);
//        assert (shapshot1.getValues().get(Currency.RUB) == 100);
//
//        //Объектов Сохранений может быть сколько угодно для каждого из Account
//        AccountSnapshot shapshot2 = account.saveToSnapshot(); //сохранили снапшот2
//        assert(shapshot2!=shapshot1);
//
//        //Любое Сохранение может быть использовано для приведения соответствующего ему объекта Account в состояние соответствующее моменту создания сохранения
//        account.restoreFromSnapshot(shapshot2);
//        assert (account.getValues().get(Currency.RUB) == 300);
//        account.restoreFromSnapshot(shapshot1);
//        assert (account.getValues().get(Currency.RUB) == 100);
    }
}
