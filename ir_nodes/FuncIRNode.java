package ir_nodes;

import ir_types.*;
import ir_visitors.MakeJVMVisitor;
import java.util.ArrayList;
import jvm_nodes.JVMFunc;

public class FuncIRNode extends IRNode {
    String name;
    IRFuncType type;
    ArrayList<TemporaryIRNode> temporaries;
    ArrayList<InstructionIRNode> instructions;

    public FuncIRNode(String name, IRFuncType type, ArrayList<TemporaryIRNode> temporaries, ArrayList<InstructionIRNode> instructions) {
        this.name = name;
        this.type = type;
        this.temporaries = temporaries;
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public IRFuncType getType() {
        return type;
    }

    public ArrayList<TemporaryIRNode> getTemporaries() {
        return temporaries;
    }

    public ArrayList<InstructionIRNode> getInstructions() {
        return instructions;
    }

    public JVMFunc accept(MakeJVMVisitor visitor) {
        return visitor.visit(this);
    }

    public void outputIR() {
        StringBuilder conglomerateType = new StringBuilder();
        for (IRType paramType : type.getParams()) {
            if (paramType.getArray()) {
                conglomerateType.append('A');
            }
            conglomerateType.append(paramType.getType());
        }
        System.out.println(String.format("FUNC %s(%s)%c", name, conglomerateType.toString(), type.getType().getType()));
        System.out.println("{");

        for (TemporaryIRNode temp : temporaries) {
            if (temp.getType().getArray()) {
                System.out.println(String.format("    TEMP %d:A%c;", temp.getNum(), temp.getType().getType()));
            } else {
                System.out.println(String.format("    TEMP %d:%c;", temp.getNum(), temp.getType().getType()));
            }
        }

        for (InstructionIRNode inst : instructions) {
            inst.outputIR();
        }
        System.out.println("}");
    }

}