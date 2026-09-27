package nodes;

import java.util.List;
import ir_nodes.TemporaryIRNode;

import ast_visitors.MakeIRVisitor;

import java.util.ArrayList;
import symbols.*;
import exceptions.*;
import symbol_table.*;
import type_equiv.*;

public class FuncCallNode extends ASTNode {

    IdentNode ident;
    ArrayList<ASTNode> args;

    public FuncCallNode(int line, int column, String s, ASTNode ident, ArrayList<ASTNode> args) {
        super(line, column, s);
        this.ident = (IdentNode) ident;
        this.args = args;
    }

    public IdentNode getIdent() {
        return ident;
    }

    public ArrayList<ASTNode> getArgs() {
        return args;
    }

    public void print(int scope) {
        ident.print(scope);
        System.out.print("(");
        int i = 0;

        while (args.size() > i) {
            args.get(i).print(scope);
            i++;
            if (i != args.size()) {
                System.out.print(", ");
            }
        }

        System.out.print(")");
    }

    public void existenceUniquenessCheck(SymbolTable st) throws TypecheckException {
        ident.existenceUniquenessCheck(st);

        if (!(ident.symbol instanceof FuncSymbol)) {
            throw new TypecheckException(String.format("Line:Column %d:%d, id %s: Can not call variables", line, column, ident.ident));
        }

        for (ASTNode arg : args) {
            arg.existenceUniquenessCheck(st);
        }

    }

    public Symbol typeCheck() throws TypecheckException {

        FuncSymbol funcSym = (FuncSymbol) ident.symbol;

        ArrayList<VarSymbol> paramList = funcSym.params;
        int numParams = paramList.size();

        if (numParams != args.size()) {
            throw new TypecheckException(String.format("Line:Column %d:%d: Must call %s with %d arguments. Received %d arguments", line, column, ident.ident, numParams, args.size()));
        }

        for (int i = 0; i < numParams; i++) {
            Symbol argType = args.get(i).typeCheck();
            Symbol param = paramList.get(i);
            if (!TypeEquiv.subtype(param, argType)) {
                typeCheckError(param, argType, i);
            }
        }

        return new Symbol(funcSym.type, funcSym.size);
    }

    private void typeCheckError(Symbol param, Symbol arg, int i) throws TypecheckException {
        if (param.size == -1 && arg.size == -1) {
            throw new TypecheckException(String.format("Line:Column %d:%d: Argument of type %s expected for arg %d. Received argument of type %s instead", line, column, param.type, i+1, arg.type));
        } else if (param.size != -1 && arg.size == -1) {
            throw new TypecheckException(String.format("Line:Column %d:%d: Argument of type %s[%d] expected for arg %d. Received argument of type %s instead", line, column, param.type, param.size, i+1, arg.type));
        } else if (param.size == -1 && arg.size != -1) {
            throw new TypecheckException(String.format("Line:Column %d:%d: Argument of type %s expected for arg %d. Received argument of type %s[%d] instead", line, column, param.type, i+1, arg.type, arg.size));
        } else {
            throw new TypecheckException(String.format("Line:Column %d:%d: Argument of type %s[%d] expected for arg %d. Received argument of type %s[%d] instead", line, column, param.type, param.size, i+1, arg.type, arg.size));
        }
    }

    public TemporaryIRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }
}