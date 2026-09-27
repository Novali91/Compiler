package ir_nodes.ir_instructions.ir_assignments;

import ir_nodes.TemporaryIRNode;
import ir_nodes.ir_instructions.IRAssignmentNode;
import ir_types.IRType;
import ir_visitors.MakeJVMVisitor;
import jvm_nodes.JVMNode;

public class IRAssignmentTypecast extends IRAssignmentNode {
    IRType fromType;
    IRType toType;
    TemporaryIRNode rhs;

    public IRAssignmentTypecast(TemporaryIRNode temp, IRType fromType, IRType toType, TemporaryIRNode rhs) {
        super(temp);
        this.fromType = fromType;
        this.toType = toType;
        this.rhs = rhs;
    }

    public IRType getFromType() {
        return fromType;
    }

    public IRType getToType() {
        return toType;
    }

    public TemporaryIRNode getRhs() {
        return rhs;
    }

    public JVMNode accept(MakeJVMVisitor visitor) {
        return visitor.visit(this);
    }

    public void outputIR() {
        super.outputIR();

        temp.outputIR();
        System.out.print(String.format(" := %c2%c ", fromType.getType(), toType.getType()));
        rhs.outputIR();
        System.out.println(";");

    }

}