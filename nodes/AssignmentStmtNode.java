package nodes;

import ast_visitors.MakeIRVisitor;
import exceptions.*;
import symbol_table.*;
import symbols.Symbol;
import type_equiv.*;
import ir_nodes.IRNode;

public class AssignmentStmtNode extends ASTNode {

    IdentNode ident;
    ASTNode expr;

    public AssignmentStmtNode(int line, int column, String s, ASTNode ident, ASTNode expr) {
        super(line, column, s);
        this.ident = (IdentNode) ident;
        this.expr = expr;
    }

    public IdentNode getIdent() {
        return ident;
    }

    public ASTNode getExpr() {
        return expr;
    }

    public void print(int scope) {
        super.print(scope);
        ident.print(scope);
        System.out.print("=");
        expr.print(scope);
        System.out.print(";\n");
    }

    public void existenceUniquenessCheck(SymbolTable st) throws TypecheckException {
        ident.existenceUniquenessCheck(st);
        expr.existenceUniquenessCheck(st);
    }

    public Symbol typeCheck() throws TypecheckException {
        Symbol exprType = expr.typeCheck();

        if (!(TypeEquiv.subtype(ident.symbol, exprType))) {
            typeCheckError(ident.symbol, exprType);
        }

        return null;
    }

    public void typeCheckError(Symbol identType, Symbol exprType) throws TypecheckException {
        
        if (identType.size == -1 && exprType.size == -1) {
            throw new TypecheckException(String.format("Line:Column %d:%d: The expression of an assignment statement must be a subtype of or equivalent to the id's type%nLeft side evaluates to type %s%nRight side evaluates to type %s", line, column, ident.symbol.type, exprType.type));
        } else if (identType.size != -1 && exprType.size == -1) {
            throw new TypecheckException(String.format("Line:Column %d:%d: The expression of an assignment statement must be a subtype of or equivalent to the id's type%nLeft side evaluates to type %s[%d]%nRight side evaluates to type %s", line, column, ident.symbol.type, ident.symbol.size, exprType.type));
        } else if (identType.size == -1 && exprType.size != -1) {
            throw new TypecheckException(String.format("Line:Column %d:%d: The expression of an assignment statement must be a subtype of or equivalent to the id's type%nLeft side evaluates to type %s%nRight side evaluates to type %s[%d]", line, column, ident.symbol.type, exprType.type, exprType.size));
        } else {
            throw new TypecheckException(String.format("Line:Column %d:%d: The expression of an assignment statement must be a subtype of or equivalent to the id's type%nLeft side evaluates to type %s[%d]%nRight side evaluates to type %s[%d]", line, column, ident.symbol.type, ident.symbol.size, exprType.type, exprType.size));
        }
    }

    public IRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }
}