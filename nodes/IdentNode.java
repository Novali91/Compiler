package nodes;

import symbols.*;
import ast_visitors.MakeIRVisitor;
import exceptions.*;
import symbol_table.*;
import ir_nodes.TemporaryIRNode;

public class IdentNode extends ASTNode {

    String ident;
    Symbol symbol;

    public IdentNode(int line, int column, String s, String ident) {
        super(line, column, s);
        this.ident = ident;
    }

    public String getIdent() {
        return ident;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void print(int scope) {
        System.out.print(ident);
    }

    public void existenceUniquenessCheck(SymbolTable st) throws TypecheckException {

        Symbol newSymbol = st.lookup(ident);

        if (newSymbol == null) {
            throw new TypecheckException(String.format("Line:Column %d:%d id %s: Variable not previously declared in scope", line, column, ident));
        }

        symbol = newSymbol;

        return;
    }

    public Symbol typeCheck() throws TypecheckException {
        return symbol;
    }

    public TemporaryIRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }
}