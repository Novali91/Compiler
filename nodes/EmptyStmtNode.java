package nodes;

import ast_visitors.MakeIRVisitor;
import exceptions.*;
import symbol_table.*;

public class EmptyStmtNode extends ASTNode {


    public EmptyStmtNode(int line, int column, String s) {
        super(line, column, s);
    }

    public void print(int scope) {
        super.print(scope);
        System.out.print(";\n");
    }

}