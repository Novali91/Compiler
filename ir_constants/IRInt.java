package ir_constants;

public class IRInt extends IRConstant {
    int value;

    public IRInt(int value) {
        this.value = value;
    }

    public void outputIR() {
        System.out.print(value);
    }

    public int getVal() {
        return value;
    }
}