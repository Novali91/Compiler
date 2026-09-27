package ir_ops;

public enum IRBinaryOperator {
    ADD,
    SUB,
    MULT,
    DIV,
    REM, 
    LESSTHAN,
    LESSTHANEQ,
    EQ,
    NEQ,
    GREATERTHANEQ,
    GREATERTHAN;

    // For print, use switch
    public void outputIR() {
        switch (this) {
            case ADD:
                System.out.print("+");
                return;
            case SUB:
                System.out.print("-");
                return;
            case MULT:
                System.out.print("*");
                return;
            case DIV:
                System.out.print("/");
                return;
            case REM:
                System.out.print("rem");
                return;
            case LESSTHAN:
                System.out.print("<");
                return;
            case LESSTHANEQ:
                System.out.print("<=");
                return;
            case EQ:
                System.out.print("==");
                return;
            case NEQ:
                System.out.print("!=");
                return;
            case GREATERTHANEQ:
                System.out.print(">=");
                return;
            case GREATERTHAN:
                System.out.print(">");
                return;
        }
    }
}