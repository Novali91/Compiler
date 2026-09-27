package nodes;

import ast_visitors.MakeIRVisitor;
import exceptions.*;
import symbol_table.*;
import symbols.*;
import ir_nodes.IRNode;
import ir_nodes.TemporaryIRNode;

public class LitBooleanNode extends LitNode {

    boolean value;

    public LitBooleanNode(int line, int column, String s, boolean value) {
        super(line, column, s);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void print(int scope) {
        System.out.print(value);
    }

    public Symbol typeCheck() throws TypecheckException {
        return new Symbol("boolean");
    }

    public TemporaryIRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }
}