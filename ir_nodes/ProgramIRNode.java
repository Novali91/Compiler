package ir_nodes;

import ir_visitors.MakeJVMVisitor;
import java.util.ArrayList;
import jvm_nodes.JVMProg;

public class ProgramIRNode extends IRNode {
    String className;
    ArrayList<FuncIRNode> funcs;

    public ProgramIRNode(String className, ArrayList<FuncIRNode> funcs) {
        this.className = className;
        this.funcs = funcs;
    }

    public String getClassName() {
        return className;
    }

    public ArrayList<FuncIRNode> getFuncs() {
        return funcs;
    }
    
    public JVMProg accept(MakeJVMVisitor visitor) {
        return visitor.visit(this);
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void outputIR() {
        for (FuncIRNode func : funcs) {
            func.outputIR();
        }
    }
}