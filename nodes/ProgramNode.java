// Feel free to modify this as needed.
package nodes;

import java.util.List;
import ir_nodes.IRNode;
import ir_nodes.ProgramIRNode;

import ast_visitors.MakeIRVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import exceptions.*;
import symbol_table.*;
import symbols.*;

public class ProgramNode extends ASTNode {

    ArrayList<FuncNode> funcs;

    public ProgramNode(int line, int column, String s, ArrayList<ASTNode> funcs) {
        super(line, column, s);
        
        ArrayList<FuncNode> newFuncs = new ArrayList<FuncNode>();
        for (ASTNode func : funcs) {
            newFuncs.add((FuncNode) func);
        }
        this.funcs = newFuncs;
    }

    public ArrayList<FuncNode> getFuncs() {
        return funcs;
    }

    public void print(int scope) {
        super.print(scope);
        
        int i = 0;

        while (funcs.size() > i) {
            funcs.get(i).print(scope);
            i++;
            if (i != funcs.size()) {
                System.out.print("\n");
            }
        }

        return;
    }

    public void existenceUniquenessCheck(SymbolTable symbolTable) throws TypecheckException {
        int numFuncs = funcs.size();

        if (numFuncs == 0) {
            throw new TypecheckException(String.format("Must contain at least one function"));
        }

        HashMap<String, FuncSymbol> globals = new HashMap<String, FuncSymbol>();

        boolean mainExists = false;

        for (FuncNode func : funcs) {
            IdentNode ident = func.ident;
            TypeNode funcType = func.type;

            if (globals.containsKey(ident.ident)) {
                throw new TypecheckException(String.format("Line:Column %d:%d, id %s: No two functions may have the same name", func.line, func.column, func.ident.ident));
            }
            
            if (ident.ident.equals("main")) {
                mainExists = true;

                if ((func.params.size() != 0)) {
                    throw new TypecheckException(String.format("Line:Column %d:%d: Main function must have no parameters", func.line, func.column));
                }
                if ((!funcType.type.equals("void")) || (funcType instanceof ArrayTypeNode)) {
                    throw new TypecheckException(String.format("Line:Column %d:%d: Main function must have return type of void", func.line, func.column));
                }
            }

            ArrayList<VarSymbol> params = new ArrayList<VarSymbol>();
            for (ParamNode param : func.params) {
                VarSymbol paramSym;
                TypeNode type = param.type;

                if (type instanceof ArrayTypeNode) {
                    paramSym = new VarSymbol(type.type, type.size);
                } else {
                    paramSym = new VarSymbol(type.type);
                }

                params.add(paramSym);

                param.symbol = paramSym;
            }

            FuncSymbol funcSym;

            if (funcType instanceof ArrayTypeNode) {
                funcSym = new FuncSymbol(funcType.type, funcType.size, params);
            } else {
                funcSym = new FuncSymbol(funcType.type, params);
            }

            globals.put(ident.ident, funcSym);
            func.symbol = funcSym;
        }

        if (!mainExists) {
            throw new TypecheckException(String.format("Program must contain a main function"));
        }

        for (FuncNode func : funcs) {
            SymbolTable st = new SymbolTable(globals, new HashMap<String, VarSymbol>(), func.symbol);
            func.existenceUniquenessCheck(st);
        }

        return;
    }

    public Symbol typeCheck() throws TypecheckException {
        for (FuncNode func : funcs) {
            func.typeCheck();
        }
        return null;
    }

    public ProgramIRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }
}