package ru.stepup;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        assert (account.getValues().get(Currency.USD) == 100);
        //Количество не может быть отрицательным
        assertThrows(IllegalArgumentException.class, () -> account.changeCurrencyAmount(Currency.USD, -100));
        //Изменение количества USD
        account.changeCurrencyAmount(Currency.USD, 200);
        assert (account.getValues().get(Currency.USD) == 200);
        //Если такой валюты еще нет - добавляем в список
        account.changeCurrencyAmount(Currency.EUR, 300);
        assert (account.getValues().get(Currency.EUR) == 300);
    }

    //Задание 2 - Отмена
    @Test
    @DisplayName("Тест undo")
    public void TestUndo() {
        Account account = new Account("Иванов Иван Иванович");
        //добавили 100 рублей
        account.changeCurrencyAmount(Currency.RUB, 100);
        assert (account.getValues().get(Currency.RUB) == 100);
        //меняем имя на Василий Иванов
        account.setName("Василий Иванов");
        assert (account.getName().equals("Василий Иванов"));
        //установили количество рублей 300
        account.changeCurrencyAmount(Currency.RUB, 300);
        assert (account.getValues().get(Currency.RUB) == 300);

        account.undo();
        assert (account.getValues().get(Currency.RUB) == 100);
        account.undo();
        assert (account.getName().equals("Иванов Иван Иванович"));
        account.undo();
        assert (account.getValues().get(Currency.RUB) == null);
    }

    //Задание 3 - сохранение снапшота
    @Test
    @DisplayName("Тест сохранения снапшота")
    public void TestSaveToSnapshot(){
        Account account = new Account("Иванов Иван Иванович");
        //добавили 100 рублей
        account.changeCurrencyAmount(Currency.RUB, 100);
        AccountSnapshot shapshot1 = account.saveToSnapshot(); //сохранили снапшот
        //пробуем изменить shapshot1 - проверка на иммутабельность
        shapshot1.getValues().put(Currency.RUB, 200);
        assert (shapshot1.getValues().get(Currency.RUB) == 100);
        //Изменения оригинального объекта Account также не оказывают влияния на Сохранение
        account.changeCurrencyAmount(Currency.RUB, 300);
        assert (account.getValues().get(Currency.RUB) == 300);
        assert (shapshot1.getValues().get(Currency.RUB) == 100);

        //Объектов Сохранений может быть сколько угодно для каждого из Account
        AccountSnapshot shapshot2 = account.saveToSnapshot(); //сохранили снапшот2
        assert(shapshot2!=shapshot1);

        //Любое Сохранение может быть использовано для приведения соответствующего ему объекта Account в состояние соответствующее моменту создания сохранения
        account.restoreFromSnapshot(shapshot2);
        assert (account.getValues().get(Currency.RUB) == 300);
        account.restoreFromSnapshot(shapshot1);
        assert (account.getValues().get(Currency.RUB) == 100);
    }
}
