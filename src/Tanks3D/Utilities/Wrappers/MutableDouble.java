package Tanks3D.Utilities.Wrappers;

//A wrapper class for the double primitive
public class MutableDouble {
    private double value;

    public MutableDouble(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }
    public void add(double addValue) {
        this.value += addValue;
    }
    public void subtract(double subtractValue) {
        this.value += subtractValue;
    }
}
