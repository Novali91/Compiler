package ir_nodes.ir_instructions.ir_controlflow;

import ir_labels.*;
import ir_nodes.InstructionIRNode;
import ir_visitors.MakeJVMVisitor;
import jvm_nodes.JVMNode;

public class IRGoto extends InstructionIRNode {

    IRLabel label;

    public IRGoto(IRLabel label) {
        this.label = label;
    }

    public IRLabel getLabel() {
        return label;
    }

    public JVMNode accept(MakeJVMVisitor visitor) {
        return visitor.visit(this);
    }

    public void outputIR() {
        super.outputIR();
        System.out.print("GOTO ");
        label.outputIR();
        System.out.println(";");
    }
}