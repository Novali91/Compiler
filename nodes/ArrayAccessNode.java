package nodes;

import ast_visitors.MakeIRVisitor;
import exceptions.*;
import symbol_table.*;
import symbols.Symbol;
import ir_nodes.TemporaryIRNode;

public class ArrayAccessNode extends ASTNode {

    IdentNode ident;
    ASTNode index;

    public ArrayAccessNode(int line, int column, String s, ASTNode ident, ASTNode index) {
        super(line, column, s);
        this.ident = (IdentNode) ident;
        this.index = index;
    }

    public IdentNode getIdent() {
        return ident;
    }

    public ASTNode getIndex() {
        return index;
    }

    public void print(int scope) {
        ident.print(scope);
        System.out.print("[");
        index.print(scope);
        System.out.print("]");
    }

    public void existenceUniquenessCheck(SymbolTable st) throws TypecheckException {
        ident.existenceUniquenessCheck(st);
        index.existenceUniquenessCheck(st);
    }

    public Symbol typeCheck() throws TypecheckException {
        if (ident.symbol.size == -1) {
            throw new TypecheckException(String.format("Line:Column %d:%d: Attempted to index %s but %s is not an array type", line, column, ident.ident, ident.ident));
        }
        Symbol indexType = index.typeCheck();

        if (!indexType.type.equals("int")) {
            throw new TypecheckException(String.format("Line:Column %d:%d: Index must evaluate to type int, not type %s", index.line, index.column, indexType.type));
        }

        return new Symbol(ident.symbol.type, -1);
    }

    public TemporaryIRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }
}