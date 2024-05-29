package ru.stepup;

import java.util.HashMap;
import java.util.Map;

public class AccountSnapshot {
    private String name;
    private Map<Currency, Integer> values;

    public AccountSnapshot(String name, Map<Currency, Integer> values) {
        this.name = name;
        this.values = new HashMap<>(values);
    }

    public String getName() {
        return name;
    }

    public Map<Currency, Integer> getValues() {
        return new HashMap<>(values);
    }
}
