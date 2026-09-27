package ir_nodes.ir_instructions;

import ir_nodes.InstructionIRNode;
import ir_nodes.TemporaryIRNode;
import ir_visitors.MakeJVMVisitor;
import jvm_nodes.JVMNode;

public class PrintIRNode extends InstructionIRNode {

    TemporaryIRNode operand;
    boolean line;

    public PrintIRNode(TemporaryIRNode operand, boolean line) {
        this.operand = operand;
        this.line = line;
    }

    public TemporaryIRNode getOperand() {
        return operand;
    }

    public boolean getLine() {
        return line;
    }

    public JVMNode accept(MakeJVMVisitor visitor) {
        return visitor.visit(this);
    }
    
    public void outputIR() {
        super.outputIR();
        StringBuilder prologue = new StringBuilder("PRINT");

        if (line) {
            prologue.append("LN");
        }

        if (operand.getType().getType() == 'U') {
            System.out.print(String.format("%sU ", prologue));
            operand.outputIR();
            System.out.println();
        } else {
            System.out.print(String.format("%s%c ", prologue, operand.getType().getType()));
            operand.outputIR();
            System.out.println();
        }
    }
}