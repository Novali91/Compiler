package nodes;

import java.util.List;

import ast_visitors.MakeIRVisitor;

import java.util.ArrayList;
import exceptions.*;
import symbol_table.*;
import symbols.*;
import ir_nodes.IRNode;

public class FuncBlockNode extends ASTNode {

    ArrayList<DeclNode> declList;
    ArrayList<ASTNode> stmtList;

    public FuncBlockNode(int line, int column, String s, ArrayList<ASTNode> declList, ArrayList<ASTNode> stmtList) {
        super(line, column, s);

        ArrayList<DeclNode> newDecls = new ArrayList<DeclNode>();

        for (ASTNode decl : declList) {
            newDecls.add((DeclNode) decl);
        }

        this.declList = newDecls;
        this.stmtList = stmtList;
    }

    public ArrayList<DeclNode> getDeclList() {
        return declList;
    }

    public ArrayList<ASTNode> getStmtList() {
        return stmtList;
    }

    public void print(int scope) {
        super.print(scope);
        System.out.print("{\n");

        int i = 0;

        while (declList.size() > i) {
            declList.get(i).print(scope+1);
            i++;
        }

        if (declList.size() > 0) {
            System.out.print("\n");
        }
        
        i = 0;
        while (stmtList.size() > i) {
            stmtList.get(i).print(scope+1);
            i++;
        }
        super.print(scope);
        System.out.print("}\n");
    }

    public void existenceUniquenessCheck(SymbolTable st) throws TypecheckException {
        for (ASTNode decl : declList) {
            decl.existenceUniquenessCheck(st);
        }

        for (ASTNode stmt : stmtList) {
            stmt.existenceUniquenessCheck(st);
        }

        return;
    }

    public Symbol typeCheck() throws TypecheckException {
        for (ASTNode stmt : stmtList) {
            stmt.typeCheck();
        }
        return null;
    }

    public IRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }
}