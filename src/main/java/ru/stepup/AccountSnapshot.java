package ru.stepup;

import java.util.HashMap;
import java.util.Map;

public class AccountSnapshot {
    private final String name;
    private final Map<Currency, Integer> values;

    private AccountSnapshot(String name, Map<Currency, Integer> values) {
        this.name = name;
        this.values = new HashMap<>(values);
    }

    public static AccountSnapshot createInstance(Account account) {
        String name = account.getName();
        Map<Currency, Integer> values = new HashMap<>(account.getValues());
        return new AccountSnapshot(name, values);
    }
    public String getName() {
        return name;
    }

    public Map<Currency, Integer> getValues() {
        return new HashMap<>(values);
    }
}