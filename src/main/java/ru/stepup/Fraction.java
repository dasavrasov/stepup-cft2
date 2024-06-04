package ru.stepup;

public class Fraction implements Fractionable {
    private int num;
    private int denum;

    public Fraction() {
        this.num = 0;
        this.denum = 1;
    }

    public Fraction(int num, int denum) {
        this.num = num;
        this.denum = denum;
    }
    @Override
    @Mutator
    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public void setDenum(int denum) {
        this.denum = denum;
    }

    @Override
    @Cache
    public double doubleValue() {
        System.out.println("Invoke doubleValue()");
        return (double) num / denum;
    }

}
