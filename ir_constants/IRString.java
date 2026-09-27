package ir_constants;

public class IRString extends IRConstant {
    String value;

    public IRString(String value) {
        this.value = value;
    }

    public void outputIR() {
        System.out.print(value);
    }

    public String getVal() {
        return value;
    }
}