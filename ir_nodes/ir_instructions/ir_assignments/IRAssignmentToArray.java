package ir_nodes.ir_instructions.ir_assignments;

import ir_nodes.TemporaryIRNode;
import ir_nodes.ir_instructions.IRAssignmentNode;
import ir_visitors.MakeJVMVisitor;
import jvm_nodes.JVMNode;

public class IRAssignmentToArray extends IRAssignmentNode{

    TemporaryIRNode value;
    TemporaryIRNode index;

    public IRAssignmentToArray(TemporaryIRNode temp, TemporaryIRNode value, TemporaryIRNode index) {
        super(temp);
        this.value = value;
        this.index = index;
    }

    public TemporaryIRNode getValue() {
        return value;
    }

    public TemporaryIRNode getIndex() {
        return index;
    }

    public JVMNode accept(MakeJVMVisitor visitor) {
        return visitor.visit(this);
    }

    public void outputIR() {
        super.outputIR();
        temp.outputIR();
        System.out.print(" [");
        index.outputIR();
        System.out.print("] := ");
        value.outputIR();
        System.out.println(";");
    }
}