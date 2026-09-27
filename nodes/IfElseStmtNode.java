package nodes;

import ast_visitors.MakeIRVisitor;
import exceptions.*;
import symbol_table.*;
import symbols.*;
import ir_nodes.IRNode;

public class IfElseStmtNode extends IfStmtNode {

    BlockNode blockTwo;

    public IfElseStmtNode(int line, int column, String s, ASTNode expr, ASTNode blockOne, ASTNode blockTwo) {
        super(line, column, s, expr, blockOne);
        this.blockTwo = (BlockNode) blockTwo;
    }

    public BlockNode getBlockTwo() {
        return blockTwo;
    }

    public void print(int scope) {
        for (int i = 0; i < scope; i++) {
            System.out.print("    ");
        }

        System.out.print("if");
        System.out.print(" (");
        expr.print(scope);
        System.out.print(")\n");
        block.print(scope);

        for (int i = 0; i < scope; i++) {
            System.out.print("    ");
        }
        System.out.print("else");
        System.out.print("\n");
        blockTwo.print(scope);
    }

    public void existenceUniquenessCheck(SymbolTable st) throws TypecheckException {
        super.existenceUniquenessCheck(st);
        blockTwo.existenceUniquenessCheck(st);
    }

    public Symbol typeCheck() throws TypecheckException {
        super.typeCheck();
        blockTwo.typeCheck();

        return null;
    }

    public IRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }
}