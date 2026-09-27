package nodes;

import ast_visitors.MakeIRVisitor;
import exceptions.*;
import symbol_table.*;
import symbols.*;
import ir_nodes.*;

public class DeclNode extends ASTNode {

    IdentNode ident;
    TypeNode type;
    
    public DeclNode(int line, int column, String s, ASTNode ident, ASTNode type) {
        super(line, column, s);
        this.ident = (IdentNode) ident;
        this.type = (TypeNode) type;
    }

    public IdentNode getIdent() {
        return ident;
    }

    public TypeNode getType() {
        return type;
    }

    public void print(int scope) {
        super.print(scope);

        type.print(scope);
        System.out.print(" ");
        ident.print(scope);
        System.out.print(";\n");
    }

    public void existenceUniquenessCheck(SymbolTable st) throws TypecheckException {
        if (type.type.equals("void")) {
            throw new TypecheckException(String.format("Line:Column %d:%d, id %s: Local variables can not have a type of void", line, column, ident.ident));
        }

        VarSymbol symbol = new VarSymbol(type.type, type.size);

        if (st.lookup(ident.ident) != null) {
            if (st.lookup(ident.ident) instanceof FuncSymbol) {
                throw new TypecheckException(String.format("Line:Column %d:%d: Local variables can not shadow functions.%nIdentifier %s already in use", line, column, ident.ident));
            } else if (st.lookup(ident.ident) instanceof ParamSymbol) {
                throw new TypecheckException(String.format("Line:Column %d:%d: Local variables can not shadow parameters.%nIdentifier %s already in use", line, column, ident.ident));
            } else {
                throw new TypecheckException(String.format("Line:Column %d:%d: Local variables can not shadow other local variables.%nIdentifier %s already in use", line, column, ident.ident));
            }
        }

        st.insert(ident.ident, symbol);

        ident.symbol = symbol;

        return;
    }

    public IRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }
}