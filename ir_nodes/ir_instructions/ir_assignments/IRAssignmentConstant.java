package ir_nodes.ir_instructions.ir_assignments;

import ir_constants.*;
import ir_nodes.TemporaryIRNode;
import ir_nodes.ir_instructions.IRAssignmentNode;
import ir_visitors.MakeJVMVisitor;
import jvm_nodes.JVMNode;

public class IRAssignmentConstant extends IRAssignmentNode{
    IRConstant constant;

    public IRAssignmentConstant(TemporaryIRNode temp, IRConstant constant) {
        super(temp);
        this.constant = constant;
    }

    public IRConstant getConstant() {
        return constant;
    }

    public JVMNode accept(MakeJVMVisitor visitor) {
        return visitor.visit(this);
    }

    public void outputIR() {
        super.outputIR();
        temp.outputIR();
        System.out.print(" := ");
        constant.outputIR();
        System.out.println(";");
    }
}