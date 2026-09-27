package nodes;

import exceptions.*;
import symbol_table.*;
import symbols.*;
import ast_visitors.*;
import ir_nodes.IRNode;

public class ASTNode {
    /* The instance variables can be changed as you think
     * is needed -- that is, deleted or added to or completely
     * replaced.
     *
     * You may also change the interface of this class as needed.
     */
    protected int line;
    protected int column;
    protected String some_text;


    public ASTNode(int line, int column, String s) {
        this.line = line;
        this.column = column;
        this.some_text = s;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }


    public void print(int scope) {
        for (int i = 0; i < scope; i++) {
            System.out.print("    ");
        }
    }


    public void dump() {
        System.out.println("DUMP: " + some_text 
            + " " + line + " " + column);
    }

    public void existenceUniquenessCheck(SymbolTable symbolTable) throws TypecheckException {
        return;
    }

    public Symbol typeCheck() throws TypecheckException {
        return null;
    }

    public IRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }
}
