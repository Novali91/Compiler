package ir_constants;

public class IRChar extends IRConstant {
    char value;

    public IRChar(char value) {
        this.value = value;
    }

    public void outputIR() {
        System.out.print("'");
        System.out.print(value);
        System.out.print("'");
    }

    public char getVal() {
        return value;
    }

}