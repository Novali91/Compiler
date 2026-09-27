package nodes;

import java.util.List;

import ast_visitors.MakeIRVisitor;

import java.util.ArrayList;
import exceptions.*;
import symbol_table.*;
import symbols.Symbol;
import ir_nodes.*;

public class BlockNode extends ASTNode {

    ArrayList<ASTNode> stmts;

    public BlockNode(int line, int column, String s, ArrayList<ASTNode> stmts) {
        super(line, column, s);
        this.stmts = stmts;
    }

    public ArrayList<ASTNode> getStmts() {
        return stmts;
    }

    public void print(int scope) {
        super.print(scope);
        System.out.print("{\n");

        int i = 0;
        while (stmts.size() > i) {
            stmts.get(i).print(scope+1);
            i++;
        }
        super.print(scope);
        System.out.print("}\n");
    }

    public void existenceUniquenessCheck(SymbolTable st) throws TypecheckException {
        for (ASTNode stmt : stmts) {
            stmt.existenceUniquenessCheck(st);
        }
    }

    public Symbol typeCheck() throws TypecheckException {
        for (ASTNode stmt : stmts) {
            stmt.typeCheck();
        }
        return null;
    }

    public IRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }
}