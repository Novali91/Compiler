package ir_ops;

public enum IRUnaryOperator {
    NEG,
    INVERT;

    public void outputIR() {
        switch (this) {
            case NEG:
                System.out.print("-");
                return;
            case INVERT:
                System.out.print("!");
                return;
        }
    }
}