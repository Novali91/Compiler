package nodes;

import ast_visitors.MakeIRVisitor;
import exceptions.*;
import symbol_table.*;
import ir_nodes.IRNode;

public class TypeNode extends ASTNode {

    String type;
    int size;
    
    public TypeNode(int line, int column, String s, String type) {
        super(line, column, s);
        this.size = -1;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public void print(int scope) {
        System.out.print(type);
    }

    public IRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }
}