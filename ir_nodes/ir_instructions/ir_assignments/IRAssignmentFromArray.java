package ir_nodes.ir_instructions.ir_assignments;

import ir_nodes.TemporaryIRNode;
import ir_nodes.ir_instructions.IRAssignmentNode;
import ir_visitors.MakeJVMVisitor;
import jvm_nodes.JVMNode;

public class IRAssignmentFromArray extends IRAssignmentNode{
    
    TemporaryIRNode array;
    TemporaryIRNode index;

    public IRAssignmentFromArray(TemporaryIRNode temp, TemporaryIRNode array, TemporaryIRNode index) {
        super(temp);
        this.array = array;
        this.index = index;
    }

    public TemporaryIRNode getArray() {
        return array;
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
        System.out.print(" := ");
        array.outputIR();
        System.out.print("[");
        index.outputIR();
        System.out.println("];");
    }

}