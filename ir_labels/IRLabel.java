package ir_labels;

public class IRLabel {

    int num;

    public IRLabel(int num) {
        this.num = num;
    }
    
    public void outputIR() {
        System.out.print(String.format("L%d", num));
    }
}