package nodes;

import ast_visitors.MakeIRVisitor;
import exceptions.*;
import symbol_table.*;
import symbols.*;
import ir_nodes.IRNode;
import ir_nodes.TemporaryIRNode;

public class LitIntNode extends LitNode {

    int value;

    public LitIntNode(int line, int column, String s, int value) {
        super(line, column, s);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void print(int scope) {
        System.out.print(value);
    }

    public Symbol typeCheck() throws TypecheckException {
        return new Symbol("int");
    }

    public TemporaryIRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }
}