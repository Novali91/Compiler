package ir_nodes.ir_instructions.ir_assignments;

import ir_nodes.TemporaryIRNode;
import ir_nodes.ir_instructions.IRAssignmentNode;
import ir_visitors.MakeJVMVisitor;
import jvm_nodes.JVMNode;

public class IRAssignmentNewArray extends IRAssignmentNode{
    
    int size;
    // For printing type, just use temp type

    public IRAssignmentNewArray(TemporaryIRNode temp, int size) {
        super(temp);
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public JVMNode accept(MakeJVMVisitor visitor) {
        return visitor.visit(this);
    }

    public void outputIR() {
        super.outputIR();
        temp.outputIR();
        System.out.print(" := NEWARRAY ");
        System.out.print(temp.getType().getType());
        System.out.println(String.format(" %d;", size));
    }
}