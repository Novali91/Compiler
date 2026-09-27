package ir_nodes.ir_instructions;

import ir_nodes.InstructionIRNode;
import ir_nodes.TemporaryIRNode;
import ir_visitors.MakeJVMVisitor;
import jvm_nodes.JVMNode;

public class IRAssignmentNode extends InstructionIRNode {
    public TemporaryIRNode temp;

    public IRAssignmentNode(TemporaryIRNode temp) {
        this.temp = temp;
    }

    public TemporaryIRNode getTemp() {
        return temp;
    }

    public JVMNode accept(MakeJVMVisitor visitor) {
        return visitor.visit(this);
    }

    public void outputIR() {
        super.outputIR();
    }
}