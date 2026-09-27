package nodes;

import ast_visitors.MakeIRVisitor;
import exceptions.*;
import symbol_table.*;
import symbols.Symbol;
import type_equiv.TypeEquiv;
import ir_nodes.IRNode;

public class ArrayAssignmentStmtNode extends AssignmentStmtNode {

    ASTNode index;
    ArrayAccessNode value;

    public ArrayAssignmentStmtNode(int line, int column, String s, ASTNode ident, ASTNode expr, ASTNode index) {
        super(line, column, s, ident, expr);
        this.index = index;
        this.value = new ArrayAccessNode(line, column, "arracy access", ident, index);
    }

    public ASTNode getIndex() {
        return index;
    }

    public ArrayAccessNode getValue() {
        return value;
    }

    public void print(int scope) {
        for (int i = 0; i < scope; i++) {
            System.out.print("    ");
        }
        ident.print(scope);
        System.out.print("[");
        index.print(scope);
        System.out.print("]");
        System.out.print("=");
        expr.print(scope);
        System.out.print(";\n");
    }

    public void existenceUniquenessCheck(SymbolTable st) throws TypecheckException {
        super.existenceUniquenessCheck(st);
        index.existenceUniquenessCheck(st);
    }

    public Symbol typeCheck() throws TypecheckException {
        
        Symbol type = value.typeCheck();

        Symbol exprType = expr.typeCheck();

        if (!(TypeEquiv.subtype(type, exprType))) {
            typeCheckError(type, exprType);
        }

        Symbol indexType = index.typeCheck();

        if (!indexType.type.equals("int")) {
            throw new TypecheckException(String.format("Line:Column %d:%d: Index must evaluate to type int, not type %s", index.line, index.column, indexType.type));
        }

        return null;
    }

    public IRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }
}