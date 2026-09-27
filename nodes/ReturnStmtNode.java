package nodes;

import ast_visitors.MakeIRVisitor;
import exceptions.*;
import symbol_table.*;
import symbols.*;
import type_equiv.*;
import ir_nodes.IRNode;

public class ReturnStmtNode extends ASTNode {

    ASTNode expr;
    FuncSymbol curFunc;

    public ReturnStmtNode(int line, int column, String s, ASTNode expr) {
        super(line, column, s);
        this.expr = expr;
    }

    public ASTNode getExpr() {
        return expr;
    }

    public FuncSymbol getCurFunc() {
        return curFunc;
    }

    public void print(int scope) {
        super.print(scope);
        System.out.print("return");
        if (expr != null) {
            System.out.print(" ");
            expr.print(scope);
        }
        System.out.print(";\n");
    }

    public void existenceUniquenessCheck(SymbolTable st) throws TypecheckException {
        curFunc = st.curFunc;

        if (expr == null) {
            return;
        }
        
        expr.existenceUniquenessCheck(st);
    }

    public Symbol typeCheck() throws TypecheckException {
        if (expr == null) {
            if (curFunc.type.equals("void")) {
                return null;
            } else {
                if (curFunc.size == -1) {
                    throw new TypecheckException(String.format("Line:Column %d:%d: Must return a value of type %s", line, column, curFunc.type));
                } else {
                    throw new TypecheckException(String.format("Line:Column %d:%d: Must return a value of type %s[%d]", line, column, curFunc.type, curFunc.size));
                }
            }
        }
        
        Symbol exprType = expr.typeCheck();

        if (!TypeEquiv.subtype(curFunc, exprType)) {
            throw new TypecheckException(String.format("Line:Column %d:%d: Return type must be a subtype of or the same type as the function type", line, expr.column));
        }

        return null;
    }

    public IRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }
}