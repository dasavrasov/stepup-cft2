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
        saveToHistory(this.name, null, 0); //изменилось только имя, валюту не будем сохранять в историю
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

        if (values.containsKey(currency))
            saveToHistory(null,currency,values.get(currency)); //сохраняем предыдущее состояние
        else
            saveToHistory(null,currency,0);
        values.put(currency, amount);
    }

    private void saveToHistory(String name, Currency currency, int amount) {
        history.push(new AccountHistory(name, currency, amount));
    }

    public void undo() {
        if (!history.isEmpty()) {
            AccountHistory hist = history.pop();
            if (hist.getName()!=null) //если имя изменилось, то восстанавливаем предыдущее состояние
                name = hist.getName();
            if (hist.getCurrency()!=null && hist.getAmount()!=0) //если имя не изменилось, то восстанавливаем количество валюты
                values.put(hist.getCurrency(), hist.getAmount());
            if (hist.getCurrency()!=null && hist.getAmount()==0) //если имя не изменилось, то удаляем валюту
                values.remove(hist.getCurrency());
        }
    }

    private static class AccountHistory {
        private final String name;
        private final Currency currency;
        private final int amount;

        public AccountHistory(String name, Currency currency, int amount) {
            this.name = name;
            this.currency = currency;
            this.amount = amount;
        }


        public String getName() {
            return name;
        }

        public Currency getCurrency() {
            return currency;
        }

        public int getAmount() {
            return amount;
        }
    }

    //3
    public AccountSnapshot saveToSnapshot() {
        return AccountSnapshot.createInstance(this);
    }

    public void restoreFromSnapshot(AccountSnapshot snapshot) {
        this.name = snapshot.getName();
        this.values = new HashMap<>(snapshot.getValues());
    }

}
