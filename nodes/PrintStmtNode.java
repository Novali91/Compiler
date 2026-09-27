package nodes;

import ast_visitors.MakeIRVisitor;
import exceptions.*;
import symbol_table.*;
import symbols.*;
import ir_nodes.IRNode;

public class PrintStmtNode extends ASTNode {

    ASTNode expr;
    boolean lnFlag;

    public PrintStmtNode(int line, int column, String s, ASTNode expr, boolean lnFlag) {
        super(line, column, s);
        this.expr = expr;
        this.lnFlag = lnFlag;
    }

    public ASTNode getExpr() {
        return expr;
    }

    public boolean getLnFlag() {
        return lnFlag;
    }

    public void print(int scope) {
        super.print(scope);
        System.out.print("print");
        if (lnFlag) {
            System.out.print("ln");
        }
        System.out.print(" ");
        expr.print(scope);
        System.out.print(";\n");
    }

    public void existenceUniquenessCheck(SymbolTable st) throws TypecheckException {
        expr.existenceUniquenessCheck(st);
    }

    public Symbol typeCheck() throws TypecheckException {
        Symbol exprType = expr.typeCheck();

        if (exprType.size != -1) {
            throw new TypecheckException(String.format("Line:Column %d:%d: Can not print an expression that evaluates to an array type", line, column));
        } else if (exprType.type.equals("void")) {
            throw new TypecheckException(String.format("Line:Column %d:%d: Can not print an expression that evaluates to void", line, column));
        }

        return null;
    }

    public IRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }
}