package ir_nodes;

import ir_visitors.MakeJVMVisitor;
import jvm_nodes.JVMNode;

public class InstructionIRNode extends IRNode {

    public JVMNode accept(MakeJVMVisitor visitor) {
        return visitor.visit(this);
    }
    
    public void outputIR() {
        System.out.print("    ");
    }
    
}