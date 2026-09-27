package nodes;

import ast_visitors.MakeIRVisitor;
import exceptions.*;
import symbol_table.*;
import symbols.*;
import ir_nodes.IRNode;
import ir_nodes.TemporaryIRNode;

public class LitFloatNode extends LitNode {

    float value;

    public LitFloatNode(int line, int column, String s, float value) {
        super(line, column, s);
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void print(int scope) {
        System.out.print(value);
    }

    public Symbol typeCheck() throws TypecheckException {
        return new Symbol("float");
    }

    public TemporaryIRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }
}