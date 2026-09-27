package ir_constants;

public class IRFloat extends IRConstant {
    float value;

    public IRFloat(float value) {
        this.value = value;
    }

    public void outputIR() {
        System.out.print(value);
    }

    public float getVal() {
        return value;
    }
}