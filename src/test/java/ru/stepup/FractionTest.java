package ru.stepup;

public class FractionTest implements Fractionable{
    private int num;
    private int denum;

    static int count=0;

    public FractionTest(int num, int denum) {
        this.num = num;
        this.denum = denum;
        count=0;
    }

    @Override @Mutator
    public void setNum(int num) {
        this.num = num;
    }

    @Override @Mutator
    public void setDenum(int denum) {
        this.denum = denum;
    }

    @Override @Cache
    public double doubleValue() {
        count++;
        System.out.println("Invoke doubleValue()");
        return (double) num / denum;
    }

}
