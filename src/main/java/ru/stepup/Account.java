package ru.stepup;

import java.util.*;

public class Account {
    private String name;
    private Map<Currency, Integer> values=new HashMap<>();

    private final Deque<Command> saves = new ArrayDeque<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть null или пустым");
        }
        String tmp = Account.this.name; //предыдущее имя
        saves.push(()->Account.this.name=tmp); //сохраняем действие сохранения предыдущего имени
        this.name = name;
    }

    public Map<Currency, Integer> getValues    () {
        return new HashMap<>(values);
    }

    public Account(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть null или пустым");
        }
        this.name = name;
    }

    public void changeCurrencyAmount(Currency currency, int amount) {
        if (currency == null) {
            throw new IllegalArgumentException("Валюта не может быть null");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Количество не может быть отрицательным");
        }
        //если валюта есть в списке, то обновляем количество, если нет, добавляем

        Integer tmp=Account.this.values.get(currency);
        if (values.containsKey(currency))
            saves.push(()->values.put(currency, tmp)); //сохраняем действие обновления количества
        else
            saves.push(()->Account.this.values.remove(currency)); //сохраняем действие удаления валюты
        values.put(currency, amount);
    }


    public void undo() {
        saves.pop().make();
    }

    interface Command {
        void make();
    }


    //3
    public Save save()   {
        return new AccSave();
    }

    private class AccSave implements Save {
        private String name = Account.this.name;
        private final Map<Currency, Integer> values = new HashMap<>(Account.this.values);
        public void load() {
            Account.this.name = name;
            Account.this.values.clear();
            Account.this.values.putAll(values);
        }
    }


}
