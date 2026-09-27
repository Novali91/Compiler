package ir_nodes.ir_instructions.ir_assignments;

import ir_nodes.TemporaryIRNode;
import ir_nodes.ir_instructions.IRAssignmentNode;
import ir_visitors.MakeJVMVisitor;
import jvm_nodes.JVMNode;

public class IRAssignmentOperand extends IRAssignmentNode{

    TemporaryIRNode operand;

    public IRAssignmentOperand(TemporaryIRNode temp, TemporaryIRNode operand) {
        super(temp);
        this.operand = operand;
    }

    public TemporaryIRNode getOperand() {
        return operand;
    }

    public JVMNode accept(MakeJVMVisitor visitor) {
        return visitor.visit(this);
    }
    
    public void outputIR() {
        super.outputIR();
        temp.outputIR();
        System.out.print(" := ");
        operand.outputIR();
        System.out.println(";");
    }
}