package nodes;

import ast_visitors.MakeIRVisitor;
import exceptions.*;
import symbol_table.*;
import symbols.*;
import ir_nodes.IRNode;

public class IfStmtNode extends ASTNode {

    ASTNode expr;
    BlockNode block;

    public IfStmtNode(int line, int column, String s, ASTNode expr, ASTNode block) {
        super(line, column, s);
        this.expr = expr;
        this.block = (BlockNode) block;
    }

    public ASTNode getExpr() {
        return expr;
    }

    public BlockNode getBlock() {
        return block;
    }

    public void print(int scope) {
        super.print(scope);
        System.out.print("if");
        System.out.print(" (");
        expr.print(scope);
        System.out.print(")\n");
        block.print(scope);
    }

    public void existenceUniquenessCheck(SymbolTable st) throws TypecheckException {
        expr.existenceUniquenessCheck(st);
        block.existenceUniquenessCheck(st);
    }

    public Symbol typeCheck() throws TypecheckException {
        Symbol conditionalType = expr.typeCheck();

        if (!(conditionalType.type.equals("boolean"))) {
            throw new TypecheckException(String.format("Line:Column %d:%d: Condition expressions in if statements must evaluate to a type of boolean", expr.line, expr.column));
        }

        block.typeCheck();

        return null;
    }

    public IRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }
}