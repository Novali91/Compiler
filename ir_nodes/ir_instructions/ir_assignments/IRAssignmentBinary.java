package ir_nodes.ir_instructions.ir_assignments;

import ir_nodes.TemporaryIRNode;
import ir_nodes.ir_instructions.IRAssignmentNode;
import ir_ops.IRBinaryOperator;
import ir_visitors.MakeJVMVisitor;
import jvm_nodes.JVMNode;

public class IRAssignmentBinary extends IRAssignmentNode{

    TemporaryIRNode lhs;
    TemporaryIRNode rhs;
    IRBinaryOperator op;

    public IRAssignmentBinary(TemporaryIRNode temp, TemporaryIRNode lhs, TemporaryIRNode rhs, IRBinaryOperator op) {
        super(temp);
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
    }

    public TemporaryIRNode getLhs() {
        return lhs;
    }

    public TemporaryIRNode getRhs() {
        return rhs;
    }

    public IRBinaryOperator getOp() {
        return op;
    }

    public JVMNode accept(MakeJVMVisitor visitor) {
        return visitor.visit(this);
    }

    public void outputIR() {
        super.outputIR();
        temp.outputIR();
        System.out.print(" := ");
        lhs.outputIR();
        System.out.print(" ");
        System.out.print(lhs.getType().getType());
        op.outputIR();
        System.out.print(" ");
        rhs.outputIR();
        System.out.println(";");
    }

}