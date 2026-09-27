package ir_nodes.ir_instructions.ir_controlflow;

import ir_labels.*;
import ir_nodes.InstructionIRNode;
import ir_nodes.TemporaryIRNode;
import ir_visitors.MakeJVMVisitor;
import jvm_nodes.JVMNode;

public class IRIfGoto extends InstructionIRNode {

    IRLabel label;
    TemporaryIRNode temp;

    public IRIfGoto(IRLabel label, TemporaryIRNode temp) {
        this.label = label;
        this.temp = temp;
    }

    public IRLabel getLabel() {
        return label;
    }

    public TemporaryIRNode getTemp() {
        return temp;
    }

    public JVMNode accept(MakeJVMVisitor visitor) {
        return visitor.visit(this);
    }

    public void outputIR() {
        super.outputIR();
        System.out.print("IF ");
        temp.outputIR();
        System.out.print(" GOTO ");
        label.outputIR();
        System.out.println(";");
    }
}