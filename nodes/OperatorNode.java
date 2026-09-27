package nodes;

import ast_visitors.MakeIRVisitor;
import exceptions.*;
import symbol_table.*;
import symbols.Symbol;
import type_equiv.*;
import ir_nodes.TemporaryIRNode;

public class OperatorNode extends ASTNode {

    String operator;
    ASTNode left;
    ASTNode right;

    public OperatorNode(int line, int column, String s, String operator, ASTNode left, ASTNode right) {
        super(line, column, s);
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public String getOperator() {
        return operator;
    }

    public ASTNode getLeft() {
        return left;
    }

    public ASTNode getRight() {
        return right;
    }

    public void print(int scope) {
        if (left instanceof OperatorNode) {
            OperatorNode leftTypecasted = (OperatorNode) left;
            if (isHigherPrecedence(this.operator, leftTypecasted.operator)) {
                printParenExpression(left, scope);
            } else {
                left.print(scope);
            }
        } else {
            left.print(scope);
        }
        System.out.print(operator);
        if (right instanceof OperatorNode) {
            OperatorNode rightTypecasted = (OperatorNode) right;
            if (isHigherPrecedence(this.operator, rightTypecasted.operator)) {
                printParenExpression(right, scope);
            } else {
                right.print(scope);
            }
        } else {
            right.print(scope);
        }
    }

    private void printParenExpression(ASTNode node, int scope) {
        System.out.print("(");
        node.print(scope);
        System.out.print(")");
    }

    private boolean isHigherPrecedence(String op1, String op2) {
        return findOpNumber(op1) > findOpNumber(op2);
    }

    private int findOpNumber(String op) {
        switch(op)
        {
            case "*":
                return 4;
            case "+":
                return 3;
            case "-":
                return 3;
            case "<":
                return 2;
            case "==":
                return 1;
            default:
                return 0;
        }
    }

    public void existenceUniquenessCheck(SymbolTable st) throws TypecheckException {
        left.existenceUniquenessCheck(st);
        right.existenceUniquenessCheck(st);
    }

    public Symbol typeCheck() throws TypecheckException {
        Symbol leftType = left.typeCheck();
        Symbol rightType = right.typeCheck();

        if ((leftType.size != -1) || (rightType.size != -1)) {
            throw new TypecheckException(String.format("Line:Column %d:%d: Operands can not evaluate to an array type", line, column));
        }

        switch(operator) 
        {
            case "*":
                return typeCheckMult(leftType, rightType);
            case "+":
                return typeCheckAdd(leftType, rightType);
            case "-":
                return typeCheckSub(leftType, rightType);
            case "<":
                return typeCheckLessThan(leftType, rightType);
            case "==":
                return typeCheckEqual(leftType, rightType);
            default:
                return null;
        }
    }

    public Symbol typeCheckMult(Symbol leftType, Symbol rightType) throws TypecheckException {
        if (TypeEquiv.equiv(leftType, rightType)) {
            if (leftType.type.equals("int") || leftType.type.equals("float")) {
                return leftType;
            } else {
                throw new TypecheckException(String.format("Line:Column %d:%d: Can not multiply expressions of the type %s", line, column, leftType.type));
            }
        } else {
            throw new TypecheckException(String.format("Line:Column %d:%d: Operand type mismatch.%nLeft operand evaluates to type %s%nRight operand evaluates to type %s", line, column, leftType.type, rightType.type));
        }

    }

    public Symbol typeCheckAdd(Symbol leftType, Symbol rightType) throws TypecheckException {
        if (TypeEquiv.equiv(leftType, rightType)) {
            if (!(leftType.type.equals("boolean") || leftType.type.equals("void"))) {
                return leftType;
            } else {
                throw new TypecheckException(String.format("Line:Column %d:%d: Can not add expressions of the type %s", line, column, leftType.type));
            }
        } else {
            throw new TypecheckException(String.format("Line:Column %d:%d: Operand type mismatch.%nLeft operand evaluates to type %s%nRight operand evaluates to type %s", line, column, leftType.type, rightType.type));
        }

    }

    public Symbol typeCheckSub(Symbol leftType, Symbol rightType) throws TypecheckException {
        if (TypeEquiv.equiv(leftType, rightType)) {
            if (leftType.type.equals("int") || leftType.type.equals("float") || leftType.type.equals("char")) {
                return leftType;
            } else {
                throw new TypecheckException(String.format("Line:Column %d:%d: Can not subtract expressions of the type %s", line, column, leftType.type));
            }
        } else {
            throw new TypecheckException(String.format("Line:Column %d:%d: Operand type mismatch.%nLeft operand evaluates to type %s%nRight operand evaluates to type %s", line, column, leftType.type, rightType.type));
        }
        
    }

    public Symbol typeCheckEqual(Symbol leftType, Symbol rightType) throws TypecheckException {
        if (TypeEquiv.equiv(leftType, rightType)) {
            if (!(leftType.type.equals("void"))) {
                return new Symbol("boolean");
            } else {
                throw new TypecheckException(String.format("Line:Column %d:%d: Can not compare expressions of the type %s", line, column, leftType.type));
            }
        } else {
            throw new TypecheckException(String.format("Line:Column %d:%d: Operand type mismatch.%nLeft operand evaluates to type %s%nRight operand evaluates to type %s", line, column, leftType.type, rightType.type));
        }
    }

    public Symbol typeCheckLessThan(Symbol leftType, Symbol rightType) throws TypecheckException {
        if (TypeEquiv.equiv(leftType, rightType)) {
            if (!(leftType.type.equals("void"))) {
                return new Symbol("boolean");
            } else {
                throw new TypecheckException(String.format("Line:Column %d:%d: Can not compare expressions of the type %s", line, column, leftType.type));
            }
        } else {
            throw new TypecheckException(String.format("Line:Column %d:%d: Operand type mismatch.%nLeft operand evaluates to type %s%nRight operand evaluates to type %s", line, column, leftType.type, rightType.type));
        }
    }

    public TemporaryIRNode accept(MakeIRVisitor visitor) {
        return visitor.visit(this);
    }
}