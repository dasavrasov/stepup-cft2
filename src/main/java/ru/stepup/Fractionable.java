package ru.stepup;

public interface Fractionable {
    @Cache
    double doubleValue();
    @Mutator
    void setNum(int num);
    void setDenum(int denum);
}
