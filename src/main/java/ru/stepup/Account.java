package ru.stepup;

import java.util.*;

public class Account {
    private String name;
    private Map<Currency, Integer> values;

    private final Deque<AccountHistory> history = new ArrayDeque<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть null или пустым");
        }
        saveToHistory();
        this.name = name;
    }

    public Map<Currency, Integer> getValues    () {
        return values;
    }

    public Account(String name) {
        this(name, new HashMap<>());
    }
    public Account(String name, Map<Currency, Integer> values) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть null или пустым");
        }
        this.name = name;
        this.values = values;
    }

    public void changeCurrencyAmount(Currency currency, int amount) {
        if (currency == null) {
            throw new IllegalArgumentException("Валюта не может быть null");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Количество не может быть отрицательным");
        }
        //если валюта есть в списке, то обновляем количество, если нет, добавляем
        saveToHistory(); //сохраняем предыдущее состояние
        values.put(currency, amount);
    }

    private void saveToHistory() {
        history.push(new AccountHistory(name, new HashMap<>(values)));
    }

    public void undo() {
        if (!history.isEmpty()) {
            AccountHistory hist = history.pop();
            this.name = hist.getName();
            this.values = new HashMap<>(hist.getValues());
        }
    }

    private static class AccountHistory {
        private final String name;
        private final Map<Currency, Integer> values;

        public AccountHistory(String name, Map<Currency, Integer> values) {
            this.name = name;
            this.values = values;
        }

        public String getName() {
            return name;
        }

        public Map<Currency, Integer> getValues() {
            return values;
        }
    }

    //3
    public AccountSnapshot saveToSnapshot() {
        return new AccountSnapshot(this.name, new HashMap<>(this.values));
    }

    public void restoreFromSnapshot(AccountSnapshot snapshot) {
        this.name = snapshot.getName();
        this.values = new HashMap<>(snapshot.getValues());
    }
}
