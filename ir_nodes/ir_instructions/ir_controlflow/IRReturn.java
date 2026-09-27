package ir_nodes.ir_instructions.ir_controlflow;

import ir_nodes.InstructionIRNode;
import ir_nodes.TemporaryIRNode;
import ir_visitors.MakeJVMVisitor;
import jvm_nodes.JVMNode;

public class IRReturn extends InstructionIRNode {
    TemporaryIRNode temp;

    public IRReturn(TemporaryIRNode temp) {
        this.temp = temp;
    }

    public IRReturn() {
        this.temp = null;
    }

    public TemporaryIRNode getTemp() {
        return temp;
    }

    public JVMNode accept(MakeJVMVisitor visitor) {
        return visitor.visit(this);
    }

    public void outputIR() {
        super.outputIR();
        if (temp == null) {
            System.out.println("RETURN;");
        } else {
            System.out.print("RETURN ");
            temp.outputIR();
            System.out.println(";");
        }
    }
}
