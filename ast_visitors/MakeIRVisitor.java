package ast_visitors;

import nodes.*;
import ir_nodes.*;
import ir_nodes.ir_instructions.IRAssignmentNode;
import ir_nodes.ir_instructions.PrintIRNode;
import ir_nodes.ir_instructions.ir_assignments.IRAssignmentBinary;
import ir_nodes.ir_instructions.ir_assignments.IRAssignmentConstant;
import ir_nodes.ir_instructions.ir_assignments.IRAssignmentFromArray;
import ir_nodes.ir_instructions.ir_assignments.IRAssignmentNewArray;
import ir_nodes.ir_instructions.ir_assignments.IRAssignmentOperand;
import ir_nodes.ir_instructions.ir_assignments.IRAssignmentToArray;
import ir_nodes.ir_instructions.ir_assignments.IRAssignmentTypecast;
import ir_nodes.ir_instructions.ir_controlflow.IRCall;
import ir_nodes.ir_instructions.ir_controlflow.IRGoto;
import ir_nodes.ir_instructions.ir_controlflow.IRIfGoto;
import ir_nodes.ir_instructions.ir_controlflow.IRLabelInst;
import ir_nodes.ir_instructions.ir_controlflow.IRReturn;
import ir_types.*;
import ir_ops.*;
import ir_constants.*;
import ir_labels.*;
import java.util.HashMap;
import java.util.ArrayList;
import symbols.*;

public class MakeIRVisitor {
    
    HashMap<String, TemporaryIRNode> localsParamsMapping;
    ArrayList<TemporaryIRNode> temporaries;
    ArrayList<InstructionIRNode> instructions;
    ArrayList<IRLabel> labels;
    FuncSymbol curFunc;

    public MakeIRVisitor() {
        return;
    }

    public IRNode visit(ASTNode node) {
        return new IRNode();
    }

    public ProgramIRNode visit(ProgramNode program) {

        ArrayList<FuncIRNode> funcs = new ArrayList<FuncIRNode>();

        for (FuncNode func : program.getFuncs()) {
            funcs.add(func.accept(this));
        }
        
        return new ProgramIRNode("program", funcs);
    }

    public FuncIRNode visit(FuncNode func) {
        localsParamsMapping = new HashMap<String, TemporaryIRNode>();
        temporaries = new ArrayList<TemporaryIRNode>();
        instructions = new ArrayList<InstructionIRNode>();
        labels = new ArrayList<IRLabel>();
        curFunc = func.getSymbol();

        ArrayList<IRType> paramTypes = new ArrayList<IRType>();
        for (ParamNode param : func.getParams()) {
            TemporaryIRNode paramNode = param.accept(this);
            paramTypes.add(paramNode.getType());
        }

        func.getFuncBlock().accept(this);

        IRType returnType = createType(func.getType());

        IRFuncType funcType = new IRFuncType(returnType, paramTypes);

        return new FuncIRNode(func.getIdent().getIdent(), funcType, temporaries, instructions);
    }

    public TemporaryIRNode visit(ParamNode param) {

        TemporaryIRNode paramIR = createTemp(createType(param.getType()), true, false);

        localsParamsMapping.put(param.getIdent().getIdent(), paramIR);

        return paramIR;
    }

    public IRNode visit(FuncBlockNode funcBlock) {
        for (DeclNode decl : funcBlock.getDeclList()) {
            decl.accept(this);
        }

        for (ASTNode stmt : funcBlock.getStmtList()) {
            stmt.accept(this);
        }

        return null;
    }

    public IRNode visit(DeclNode decl) {
        
        TemporaryIRNode temp = createTemp(createType(decl.getType()), false, true);

        localsParamsMapping.put(decl.getIdent().getIdent(), temp);

        if (decl.getType().getSize() != -1) {
            instructions.add(new IRAssignmentNewArray(temp, decl.getType().getSize()));
        }

        return null;
    }

    public TemporaryIRNode visit(LitIntNode lit) {
        TemporaryIRNode temp = getTemp(new IRType('I'));
        instructions.add(new IRAssignmentConstant(temp, new IRInt(lit.getValue())));
        return temp;
    }

    public TemporaryIRNode visit(LitFloatNode lit) {
        TemporaryIRNode temp = getTemp(new IRType('F'));
        instructions.add(new IRAssignmentConstant(temp, new IRFloat(lit.getValue())));
        return temp;
    }

    public TemporaryIRNode visit(LitCharNode lit) {
        TemporaryIRNode temp = getTemp(new IRType('C'));
        instructions.add(new IRAssignmentConstant(temp, new IRChar(lit.getValue())));
        return temp;
    }

    public TemporaryIRNode visit(LitStringNode lit) {
        TemporaryIRNode temp = getTemp(new IRType('U'));
        instructions.add(new IRAssignmentConstant(temp, new IRString(lit.getValue())));
        return temp;
    }

    public TemporaryIRNode visit(LitBooleanNode lit) {
        TemporaryIRNode temp = getTemp(new IRType('Z'));
        instructions.add(new IRAssignmentConstant(temp, new IRBool(lit.getValue())));
        return temp;
    }

    public TemporaryIRNode visit(OperatorNode op) {

        IRNode lhs = op.getLeft().accept(this);
        IRNode rhs = op.getRight().accept(this);

        TemporaryIRNode lhsTypecast = (TemporaryIRNode) lhs;
        TemporaryIRNode rhsTypecast = (TemporaryIRNode) rhs;

        popTemp(lhsTypecast);
        popTemp(rhsTypecast);

        IRBinaryOperator operator = getBinOp(op.getOperator());

        IRType type = new IRType(getBinOpType(operator, lhsTypecast.getType().getType(), rhsTypecast.getType().getType()));

        TemporaryIRNode temp = getTemp(type);

        IRAssignmentBinary inst = new IRAssignmentBinary(temp, lhsTypecast, rhsTypecast, operator);
        instructions.add(inst);

        return temp;
    }

    public TemporaryIRNode visit(ArrayAccessNode acc) {
        TemporaryIRNode ident = localsParamsMapping.get(acc.getIdent().getIdent());

        IRNode index = acc.getIndex().accept(this);
        TemporaryIRNode indexTypecast = (TemporaryIRNode) index;
        popTemp(indexTypecast);
        TemporaryIRNode temp = getTemp(new IRType(getTypeChar(acc.getIdent().getSymbol().type))); // This is such an ugly line

        IRAssignmentFromArray inst = new IRAssignmentFromArray(temp, ident, indexTypecast);
        instructions.add(inst);

        return temp;
    }

    public TemporaryIRNode visit(FuncCallNode node) {
        // Need to typecast unary op the params

        Symbol nodeSymbol = node.getIdent().getSymbol();
        IRType type;

        if (nodeSymbol.size == -1) {
            type = new IRType(getTypeChar(nodeSymbol.type));
        } else {
            type = new IRType(getTypeChar(nodeSymbol.type), true);
        }

        ArrayList<TemporaryIRNode> argList = new ArrayList<TemporaryIRNode>();

        ArrayList<ASTNode> args = node.getArgs();

        for (int i = 0; i < args.size(); i++) {
            ASTNode arg = args.get(i);

            IRNode tmp = arg.accept(this);
            TemporaryIRNode tmpTypecast = (TemporaryIRNode) tmp;

            Symbol funcType = node.getIdent().getSymbol();
            FuncSymbol funcTypeTypecast = (FuncSymbol) funcType;
            Symbol argType = funcTypeTypecast.params.get(i);

            if (getTypeChar(argType.type) == 'F' && tmpTypecast.getType().getType() == 'I') {
                popTemp(tmpTypecast);
                TemporaryIRNode newTemp = getTemp(new IRType('F'));
                IRAssignmentTypecast typecast = new IRAssignmentTypecast(newTemp, new IRType('I'), new IRType('F'), tmpTypecast);
                instructions.add(typecast);
                argList.add(newTemp);
            } else {
                argList.add(tmpTypecast);
            }
        }

        for (TemporaryIRNode arg : argList) {
            popTemp(arg);
        }

        TemporaryIRNode lhs = null;

        

        if (type.getType() == 'V') {
            IRCall inst = new IRCall(node.getIdent().getIdent(), lhs, argList);
            instructions.add(inst);
        } else {
            lhs = getTemp(type);
            IRCall inst = new IRCall(node.getIdent().getIdent(), lhs, argList);
            instructions.add(inst);
        }

        return lhs;
    }

    public TemporaryIRNode visit(IdentNode ident) {
        TemporaryIRNode newTemp = getTemp(createType(new TypeNode(0, 0, null, ident.getSymbol().type)));
        instructions.add(new IRAssignmentOperand(newTemp, localsParamsMapping.get(ident.getIdent())));
        return newTemp;
    }

    public IRNode visit(ExprStmtNode expr) {
        IRNode temp = expr.getExpr().accept(this);
        if (temp == null) {
            return null;
        }
        TemporaryIRNode tempTypecast = (TemporaryIRNode) temp;
        popTemp(tempTypecast);
        return null;
    }

    public IRNode visit(PrintStmtNode print) {
        TemporaryIRNode temp = (TemporaryIRNode) print.getExpr().accept(this);
        instructions.add(new PrintIRNode(temp, print.getLnFlag()));

        popTemp(temp);
        return null;
    }

    public IRNode visit(ReturnStmtNode ret) {

        if (ret.getExpr() == null) {
            instructions.add(new IRReturn());
            return null;
        }

        TemporaryIRNode temp = (TemporaryIRNode) ret.getExpr().accept(this);
        if (getTypeChar(curFunc.type) == 'F' && temp.getType().getType() == 'I') {
            popTemp(temp);
            TemporaryIRNode newTemp = getTemp(new IRType('F'));
            instructions.add(new IRAssignmentTypecast(newTemp, new IRType('I'), new IRType('F'), temp));
            instructions.add(new IRReturn(newTemp));
            popTemp(newTemp);
        } else {
            instructions.add(new IRReturn(temp));
            popTemp(temp);
        }

        return null;
    }

    public IRNode visit(IfStmtNode stmt) {
        IRNode conditional = stmt.getExpr().accept(this);
        TemporaryIRNode conditionalTypecast = (TemporaryIRNode) conditional;

        IRLabel ifLabel = getLabel();
        IRLabel afterLabel = getLabel();
        IRIfGoto ifStmt = new IRIfGoto(ifLabel, conditionalTypecast);

        popTemp(conditionalTypecast);

        instructions.add(ifStmt);
        instructions.add(new IRGoto(afterLabel));
        instructions.add(new IRLabelInst(ifLabel));

        stmt.getBlock().accept(this);

        instructions.add(new IRLabelInst(afterLabel));

        return null;
    }

    public IRNode visit(IfElseStmtNode stmt) {
        IRNode conditional = stmt.getExpr().accept(this);
        TemporaryIRNode conditionalTypecast = (TemporaryIRNode) conditional;

        IRLabel ifLabel = getLabel();
        IRLabel afterLabel = getLabel();
        IRIfGoto ifStmt = new IRIfGoto(ifLabel, conditionalTypecast);

        popTemp(conditionalTypecast);

        instructions.add(ifStmt);

        stmt.getBlockTwo().accept(this);

        instructions.add(new IRGoto(afterLabel));
        instructions.add(new IRLabelInst(ifLabel));

        stmt.getBlock().accept(this);

        instructions.add(new IRLabelInst(afterLabel));

        return null;
    }

    public IRNode visit(WhileStmtNode stmt) {
        IRLabel loopLabel = getLabel();
        instructions.add(new IRLabelInst(loopLabel));

        IRNode conditional = stmt.getExpr().accept(this);

        TemporaryIRNode conditionalTypecast = (TemporaryIRNode) conditional;

        IRLabel ifLabel = getLabel();
        IRLabel afterLabel = getLabel();

        IRIfGoto ifStmt = new IRIfGoto(ifLabel, conditionalTypecast);

        instructions.add(ifStmt);
        instructions.add(new IRGoto(afterLabel));
        instructions.add(new IRLabelInst(ifLabel));
        
        stmt.getBlock().accept(this);

        instructions.add(new IRGoto(loopLabel));

        instructions.add(new IRLabelInst(afterLabel));

        return null;
    }

    public IRNode visit(BlockNode block) {
        for (ASTNode stmt : block.getStmts()) {
            stmt.accept(this);
        }
        return null;
    }

    public IRNode visit(AssignmentStmtNode assign) {

        ASTNode expr = assign.getExpr();
        String ident = assign.getIdent().getIdent();
        TemporaryIRNode temp = localsParamsMapping.get(ident);

        if (expr instanceof LitNode) {
            IRAssignmentNode inst;

            if (expr instanceof LitIntNode) {
                // Need to check if typecast here
                LitIntNode typecastExpr = (LitIntNode) expr;
                IRInt val = new IRInt(typecastExpr.getValue());

                if (temp.getType().getType() == 'F') {
                    TemporaryIRNode valTemp = getTemp(new IRType('I'));
                    instructions.add(new IRAssignmentConstant(valTemp, val));
                    popTemp(valTemp);
                    TemporaryIRNode valTypecast = getTemp(new IRType('F'));
                    instructions.add(new IRAssignmentTypecast(valTypecast, new IRType('I'), new IRType('F'), valTemp));
                    inst = new IRAssignmentOperand(temp, valTypecast);
                } else {
                    inst = new IRAssignmentConstant(temp, val);
                }
            } else if (expr instanceof LitCharNode) {
                LitCharNode typecastExpr = (LitCharNode) expr;
                IRChar val = new IRChar(typecastExpr.getValue());
                inst = new IRAssignmentConstant(temp, val);
            } else if (expr instanceof LitStringNode) {
                LitStringNode typecastExpr = (LitStringNode) expr;
                IRString val = new IRString(typecastExpr.getValue());
                inst = new IRAssignmentConstant(temp, val);
            } else if (expr instanceof LitFloatNode) {
                LitFloatNode typecastExpr = (LitFloatNode) expr;
                IRFloat val = new IRFloat(typecastExpr.getValue());
                inst = new IRAssignmentConstant(temp, val);
            } else {
                LitBooleanNode typecastExpr = (LitBooleanNode) expr;
                IRBool val = new IRBool(typecastExpr.getValue());
                inst = new IRAssignmentConstant(temp, val);
            }

            instructions.add(inst);
        } else {
            IRNode rhs = expr.accept(this);
            TemporaryIRNode rhsTypecast = (TemporaryIRNode) rhs;

            if (rhsTypecast.getType().getType()  == 'I' && temp.getType().getType() == 'F') {
                popTemp(rhsTypecast);
                TemporaryIRNode rhsTypecastTypecast = getTemp(new IRType('F'));
                instructions.add(new IRAssignmentTypecast(rhsTypecastTypecast, new IRType('I'), new IRType('F'), rhsTypecast));
                instructions.add(new IRAssignmentOperand(temp, rhsTypecastTypecast));
                popTemp(rhsTypecastTypecast);
            } else {
                IRAssignmentOperand inst = new IRAssignmentOperand(temp, rhsTypecast);
                instructions.add(inst);
                popTemp(rhsTypecast);
            }
            
        }
        // } else if (expr instanceof ArrayAccessNode) {
        //     ArrayAccessNode exprTypecast = (ArrayAccessNode) expr;
        //     IRNode index = exprTypecast.getIndex().accept(this);
        //     // This should be TemporaryIRNode
        //     TemporaryIRNode indexTypecast = (TemporaryIRNode) index;
        //     TemporaryIRNode rhsTemp = localsParamsMapping.get(exprTypecast.getIdent().getIdent());

        //     IRAssignmentFromArray inst = new IRAssignmentFromArray(temp, rhsTemp, indexTypecast);
        //     instructions.add(inst);
        // } else if (expr instanceof )
        // Maybe will be used for slightly more efficient local decls?

        return null;
    }

    public IRNode visit(ArrayAssignmentStmtNode stmt) {
        TemporaryIRNode temp = localsParamsMapping.get(stmt.getIdent().getIdent());

        IRNode index = stmt.getIndex().accept(this);
        IRNode val = stmt.getExpr().accept(this);

        TemporaryIRNode indexTypecast = (TemporaryIRNode) index;
        TemporaryIRNode valTypecast = (TemporaryIRNode) val;

        popTemp(indexTypecast);
        popTemp(valTypecast);

        IRAssignmentToArray inst = new IRAssignmentToArray(temp, valTypecast, indexTypecast);
        instructions.add(inst);

        return null;
    }



    public TemporaryIRNode getTemp(IRType type) {

        for (TemporaryIRNode temp : temporaries) {
            if (!temp.in_use && temp.getType().getType() == type.getType()) {
                temp.in_use = true;
                return temp;
            }
        }

        TemporaryIRNode newTemp = createTemp(type, false, false);

        return newTemp;
    }

    public TemporaryIRNode createTemp(IRType type, boolean param, boolean local) {

        TemporaryIRNode temp = new TemporaryIRNode(type, temporaries.size(), true, param, local);

        temporaries.add(temp);

        return temp;
    }

    public void popTemp(TemporaryIRNode temp) {
        if (temp.local || temp.param) {
            return;
        }
        temp.in_use = false;
        return;
    }

    public IRType createType(TypeNode type) {
        boolean array = type.getSize() != -1;
        char typeCharIR = getTypeChar(type.getType());

        return new IRType(typeCharIR, array);
    }

    public char getTypeChar(String ogType) {
        switch (ogType) {
            case "string":
                return 'U';
            case "boolean":
                return 'Z';
            case "int":
                return 'I';
            case "char":
                return 'C';
            case "float":
                return 'F';
            default:
                return 'V';
        }
    }

    public IRBinaryOperator getBinOp(String operator) {
        switch (operator) {
            case "+":
                return IRBinaryOperator.ADD;
            case "*":
                return IRBinaryOperator.MULT;
            case "<":
                return IRBinaryOperator.LESSTHAN;
            case "-":
                return IRBinaryOperator.SUB;
            case "==":
                return IRBinaryOperator.EQ;
        }
        return null;
    }

    public char getBinOpType(IRBinaryOperator op, char lhs, char rhs) {
        // Maybe add typechecking?
        switch (op) {
            case IRBinaryOperator.ADD:
                return lhs;
            case IRBinaryOperator.SUB:
                return lhs;
            case IRBinaryOperator.MULT:
                return lhs;
            case IRBinaryOperator.DIV:
                return lhs;
            case IRBinaryOperator.REM:
                return lhs;
            case IRBinaryOperator.LESSTHAN:
                return 'Z';
            case IRBinaryOperator.LESSTHANEQ:
                return 'Z';
            case IRBinaryOperator.EQ:
                return 'Z';
            case IRBinaryOperator.NEQ:
                return 'Z';
            case IRBinaryOperator.GREATERTHANEQ:
                return 'Z';
            case IRBinaryOperator.GREATERTHAN:
                return 'Z';
        }


        return 'a';
    }

    public IRLabel getLabel() {
        IRLabel newLabel = new IRLabel(labels.size()+1);
        labels.add(newLabel);
        return newLabel;
    }
}