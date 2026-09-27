package ir_nodes.ir_instructions.ir_controlflow;

import ir_nodes.FuncIRNode;
import ir_nodes.InstructionIRNode;
import ir_nodes.TemporaryIRNode;
import ir_visitors.MakeJVMVisitor;
import java.util.ArrayList;
import jvm_nodes.JVMNode;

public class IRCall extends InstructionIRNode {

    String funcName;
    TemporaryIRNode temp;
    ArrayList<TemporaryIRNode> params;

    public IRCall(String funcName, TemporaryIRNode temp, ArrayList<TemporaryIRNode> params) {
        this.funcName = funcName;
        this.temp = temp;
        this.params = params;
    }

    public String getFuncName() {
        return funcName;
    }

    public TemporaryIRNode getTemp() {
        return temp;
    }

    public ArrayList<TemporaryIRNode> getParams() {
        return params;
    }

    public JVMNode accept(MakeJVMVisitor visitor) {
        return visitor.visit(this);
    }

    public void outputIR() {
        super.outputIR();
        if (temp != null) {
            temp.outputIR();
            System.out.print(" := ");
        }
        System.out.print(String.format("CALL %s (", funcName));
        for (TemporaryIRNode arg : params) {
            arg.outputIR();
        }
        System.out.println(");");
    }
}