package ru.stepup;

import java.util.List;

public interface Checker<T> {
    List<T> check(List<T> inputList);
}
