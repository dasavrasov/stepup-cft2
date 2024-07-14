package ru.stepup.model;

public enum State {
    CLOSED(0, "Закрыт"),
    OPEN(1, "Открыт"),
    RESERVED(2, "Зарезервирован");

    private final int code;
    private final String description;

    State(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}