package nodes;

import ast_visitors.MakeIRVisitor;
import exceptions.*;
import symbol_table.*;
import ir_nodes.IRNode;

public class ArrayTypeNode extends TypeNode {
    
    public ArrayTypeNode(int line, int column, String s, String type, int size) {
        super(line, column, s, type);
        this.size = size;
    }

    public void print(int scope) {
        System.out.print(type);
        System.out.print("[");
        System.out.print(size);
        System.out.print("]");
    }

    public IRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }
}