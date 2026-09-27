package nodes;

import ast_visitors.MakeIRVisitor;
import exceptions.*;
import symbol_table.*;
import symbols.*;
import ir_nodes.IRNode;
import ir_nodes.TemporaryIRNode;

public class LitStringNode extends LitNode {

    String value;

    public LitStringNode(int line, int column, String s, String value) {
        super(line, column, s);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void print(int scope) {
        System.out.print(value);
    }

    public Symbol typeCheck() throws TypecheckException {
        return new Symbol("string");
    }

    public TemporaryIRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }
}