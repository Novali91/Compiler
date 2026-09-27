package nodes;

import ast_visitors.MakeIRVisitor;
import exceptions.*;
import symbol_table.*;
import symbols.Symbol;
import ir_nodes.IRNode;

public class ExprStmtNode extends ASTNode {

    ASTNode expr;

    public ExprStmtNode(int line, int column, String s, ASTNode expr) {
        super(line, column, s);
        this.expr = expr;
    }

    public ASTNode getExpr() {
        return expr;
    }

    public void print(int scope) {
        super.print(scope);
        expr.print(scope);
        System.out.print(";\n");
    }

    public void existenceUniquenessCheck(SymbolTable st) throws TypecheckException {
        expr.existenceUniquenessCheck(st);
    }

    public Symbol typeCheck() throws TypecheckException {
        expr.typeCheck();
        return null;
    }

    public IRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }
}