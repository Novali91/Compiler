package nodes;

import java.util.List;

import ast_visitors.MakeIRVisitor;

import java.util.ArrayList;

import exceptions.*;
import symbol_table.*;
import symbols.*;
import ir_nodes.FuncIRNode;

public class FuncNode extends ASTNode {

    TypeNode type;
    IdentNode ident;
    ArrayList<ParamNode> params;
    FuncBlockNode funcBlock;
    FuncSymbol symbol;

    public FuncNode(int line, int column, String s, ASTNode type, ASTNode ident, ArrayList<ASTNode> params, ASTNode funcBlock) {
        super(line, column, s);
        this.type = (TypeNode) type;
        this.ident = (IdentNode) ident;

        ArrayList<ParamNode> new_params = new ArrayList<ParamNode>();

        for (ASTNode param : params) {
            new_params.add((ParamNode) param);
        }

        this.params = new_params;
        this.funcBlock = (FuncBlockNode) funcBlock;
    }

    public IdentNode getIdent() {
        return ident;
    }

    public ArrayList<ParamNode> getParams() {
        return params;
    }

    public TypeNode getType() {
        return type;
    }

    public FuncBlockNode getFuncBlock() {
        return funcBlock;
    }

    public FuncSymbol getSymbol() {
        return symbol;
    }

    public void print(int scope) {
        super.print(scope);
        type.print(scope);
        System.out.print(" ");
        ident.print(scope);
        if (params.size() != 0) {
            System.out.print(" ");
        }
        System.out.print("(");

        int i = 0;

        while (params.size() > i) {
            params.get(i).print(scope);
            i++;
            if (i != params.size()) {
                System.out.print(", ");
            }
        }

        System.out.print(")");
        System.out.print("\n");
        funcBlock.print(scope);
    }
    
    public void existenceUniquenessCheck(SymbolTable st) throws TypecheckException {

        for (ASTNode param : params) {
            param.existenceUniquenessCheck(st);
        }

        funcBlock.existenceUniquenessCheck(st);
    }

    public Symbol typeCheck() throws TypecheckException {
        funcBlock.typeCheck();

        return null;
    }

    public FuncIRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }
}