package ir_constants;

public class IRBool extends IRConstant {
    boolean value;

    public IRBool(boolean value) {
        this.value = value;
    }

    public void outputIR() {
        System.out.print(value);
    }

    public boolean getVal() {
        return value;
    }

}