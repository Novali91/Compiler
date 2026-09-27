package nodes;

import symbols.*;
import ast_visitors.MakeIRVisitor;
import exceptions.*;
import symbol_table.*;
import ir_nodes.TemporaryIRNode;

public class ParamNode extends ASTNode {

    IdentNode ident;
    TypeNode type;
    VarSymbol symbol;

    public ParamNode(int line, int column, String s, ASTNode ident, ASTNode type) {
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

    public VarSymbol getSymbol() {
        return symbol;
    }

    public void print(int scope) {
        type.print(scope);
        System.out.print(" ");
        ident.print(scope);
    }

    public void existenceUniquenessCheck(SymbolTable st) throws TypecheckException {
        IdentNode paramIdent = (IdentNode) ident;
        TypeNode paramType = (TypeNode) type;

        if (paramType.type.equals("void")) {
            throw new TypecheckException(String.format("Line:Column %d:%d, id %s: Parameter type can not be void", line, column, ident.ident));
        }

        if (st.insert(paramIdent.ident, symbol) == false) {
            if (st.lookup(paramIdent.ident) instanceof FuncSymbol) {
                throw new TypecheckException(String.format("Line:Column %d:%d, id %s: Parameter names can not shadow function names", line, column, ident.ident));
            } else {
                throw new TypecheckException(String.format("Line:Column %d:%d, id %s: No duplicate parameter names", line, column, ident.ident));
            }
        }
    }

    public TemporaryIRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }

}