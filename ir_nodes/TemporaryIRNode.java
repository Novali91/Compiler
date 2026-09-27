package ir_nodes;

import ir_types.*;

public class TemporaryIRNode extends IRNode {

    IRType type;
    int num;
    public boolean param;
    public boolean local;
    public boolean in_use;

    public TemporaryIRNode(IRType type, int num, boolean in_use) {
        this.type = type;
        this.num = num;
        this.in_use = in_use;
        this.param = false;
        this.local = false;
    }

    public TemporaryIRNode(IRType type, int num, boolean in_use, boolean param, boolean local) {
        this.type = type;
        this.num = num;
        this.in_use = in_use;
        this.param = param;
        this.local = local;
    }

    public IRType getType() {
        return type;
    }

    public boolean getParam() {
        return param;
    }

    public boolean getLocal() {
        return local;
    }

    public boolean getInUse() {
        return in_use;
    }

    public int getNum() {
        return num;
    }

    public void outputIR() {
        if (param) {
            System.out.print(String.format("P%d", num));
        } else if (local) {
            System.out.print(String.format("L%d", num));
        } else {
            System.out.print(String.format("T%d", num));
        }
    }

}