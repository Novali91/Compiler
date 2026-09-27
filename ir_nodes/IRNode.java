package ir_nodes;

import ir_visitors.MakeJVMVisitor;
import jvm_nodes.JVMNode;

public class IRNode {

    public JVMNode accept(MakeJVMVisitor visitor) {
        return visitor.visit(this);
    }

    public void outputIR() {
    }

}