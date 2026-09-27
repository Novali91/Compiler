package nodes;

import ast_visitors.MakeIRVisitor;
import exceptions.*;
import symbol_table.*;
import symbols.*;
import ir_nodes.IRNode;
import ir_nodes.TemporaryIRNode;

public class LitCharNode extends LitNode {

    char value;

    public LitCharNode(int line, int column, String s, char value) {
        super(line, column, s);
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public void print(int scope) {
        System.out.print('\'');
        System.out.print(value);
        System.out.print('\'');
    }

    public Symbol typeCheck() throws TypecheckException {
        return new Symbol("char");
    }

    public TemporaryIRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }
}