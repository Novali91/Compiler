package ir_nodes.ir_instructions.ir_assignments;

import ir_nodes.TemporaryIRNode;
import ir_nodes.ir_instructions.IRAssignmentNode;
import ir_ops.IRUnaryOperator;
import ir_visitors.MakeJVMVisitor;
import jvm_nodes.JVMNode;

public class IRAssignmentUnary extends IRAssignmentNode{
    
    IRUnaryOperator op;
    TemporaryIRNode operand;

    public IRAssignmentUnary(TemporaryIRNode temp, IRUnaryOperator op, TemporaryIRNode operand) {
        super(temp);
        this.op = op;
        this.operand = operand;
    }

    public IRUnaryOperator getOp() {
        return op;
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
        System.out.print(operand.getType().getType());
        op.outputIR();
        System.out.print(" ");
        operand.outputIR();
        System.out.println(";");
    }
}