package ir_visitors;

import ir_nodes.*;
import ir_nodes.ir_instructions.*;
import ir_nodes.ir_instructions.ir_assignments.*;
import ir_nodes.ir_instructions.ir_controlflow.*;
import ir_ops.*;
import ir_types.IRFuncType;
import ir_constants.*;
import ir_labels.IRLabel;

import java.util.ArrayList;
import java.util.HashMap;
import jvm_nodes.*;
import jvm_nodes.jvm_instructions.*;
import jvm_nodes.jvm_instructions.jvm_binops.*;
import jvm_nodes.jvm_instructions.jvm_if.*;
import jvm_nodes.jvm_constants.*;

public class MakeJVMVisitor {

    int numStack;
    int largestStack;
    int numLabels;
    String className;
    ArrayList<JVMInstruction> instructions;
    HashMap<IRLabel, JVMLabel> labelMap;
    HashMap<String, IRFuncType> funcMap = new HashMap<String, IRFuncType>();
    boolean terminated = false;


    public MakeJVMVisitor() {
        return;
    }

    // Look at what is needed for .j files (filename etc?) - program level

    // Function level: .limit stack (done with counter that incs/decs as we visit the func?)
    // .limit locals to temporaries size

    // iload_1 vs iload 1

    public JVMNode visit(IRNode node) {
        return null;
    }

    public JVMProg visit(ProgramIRNode node) {

        className = node.getClassName();

        ArrayList<JVMFunc> funcs = new ArrayList<JVMFunc>();

        for (FuncIRNode func : node.getFuncs()) {
            funcMap.put(func.getName(), func.getType());
        }

        for (FuncIRNode func : node.getFuncs()) {
            JVMFunc newFunc = func.accept(this);
            funcs.add(newFunc);
        }

        return new JVMProg(node.getClassName(), funcs);
    }

    public JVMFunc visit(FuncIRNode func) {
        numStack = 0;
        largestStack = 0;
        numLabels = 0;
        instructions = new ArrayList<JVMInstruction>();
        labelMap = new HashMap<IRLabel, JVMLabel>();

        for (InstructionIRNode inst : func.getInstructions()) {
            inst.accept(this);
        }

        int numLocals = 0;

        for (TemporaryIRNode temp : func.getTemporaries()) {
            if (temp.getLocal() || temp.getParam()) {
                numLocals++;
            }
        }

        return new JVMFunc(func.getName(), func.getType(), largestStack, numLocals, instructions);
    }

    public JVMNode visit(IRAssignmentBinary node) {

        /*if (node.getLhs().getLocal()) {
            loadLocal(node.getLhs());
        }

        if (node.getRhs().getLocal()) {
            loadLocal(node.getRhs());
        }*/
        if (node.getLhs().getType().getType() == 'U') {
            JVMLabel labelTrue;
            JVMLabel labelExit;
            switch (node.getOp()) {
                case IRBinaryOperator.ADD:
                    return null;
                case IRBinaryOperator.EQ:
                    instructions.add(new JVMInvoke("java/lang/String/equals(Ljava/lang/Object;)Z"));
                    decStack();
                    break;
                case IRBinaryOperator.NEQ:
                    instructions.add(new JVMInvoke("java/lang/String/equals(Ljava/lang/Object;)Z"));
                    decStack();
                    labelTrue = getLabel(null);
                    labelExit = getLabel(null);
                    instructions.add(new JVMIfEQ(labelTrue));
                    decStack();
                    insertBool(labelTrue, labelExit);
                    break;
                case IRBinaryOperator.GREATERTHAN:
                    instructions.add(new JVMInvoke("java/lang/String/compareTo(Ljava/lang/String;)I"));
                    decStack();
                    labelTrue = getLabel(null);
                    labelExit = getLabel(null);
                    instructions.add(new JVMIfGT(labelTrue));
                    decStack();
                    insertBool(labelTrue, labelExit);
                    break;
                case IRBinaryOperator.GREATERTHANEQ:
                    instructions.add(new JVMInvoke("java/lang/String/compareTo(Ljava/lang/String;)I"));
                    decStack();
                    labelTrue = getLabel(null);
                    labelExit = getLabel(null);
                    instructions.add(new JVMIfGE(labelTrue));
                    decStack();
                    insertBool(labelTrue, labelExit);
                    break;
                case IRBinaryOperator.LESSTHAN:
                    instructions.add(new JVMInvoke("java/lang/String/compareTo(Ljava/lang/String;)I"));
                    decStack();
                    labelTrue = getLabel(null);
                    labelExit = getLabel(null);
                    instructions.add(new JVMIfLT(labelTrue));
                    decStack();
                    insertBool(labelTrue, labelExit);
                    break;
                case IRBinaryOperator.LESSTHANEQ:
                    instructions.add(new JVMInvoke("java/lang/String/compareTo(Ljava/lang/String;)I"));
                    decStack();
                    labelTrue = getLabel(null);
                    labelExit = getLabel(null);
                    instructions.add(new JVMIfLE(labelTrue));
                    decStack();
                    insertBool(labelTrue, labelExit);
                    break;

            }
        
        } else if (node.getLhs().getType().getType() == 'F') {
            JVMLabel labelTrue;
            JVMLabel labelExit;
            switch (node.getOp()) {
                case IRBinaryOperator.ADD:
                    instructions.add(new JVMAdd('F'));
                    decStack();
                    break;
                case IRBinaryOperator.MULT:
                    instructions.add(new JVMMul('F'));
                    decStack();
                    break;
                case IRBinaryOperator.SUB:
                    instructions.add(new JVMSub('F'));
                    decStack();
                    break;
                case IRBinaryOperator.DIV:
                    instructions.add(new JVMDiv('F'));
                    decStack();
                    break;
                case IRBinaryOperator.REM:
                    instructions.add(new JVMRem('F'));
                    decStack();
                    break;
                case IRBinaryOperator.EQ:
                    instructions.add(new JVMFlCmp());
                    decStack();
                    labelTrue = getLabel(null);
                    labelExit = getLabel(null);
                    instructions.add(new JVMIfEQ(labelTrue));
                    decStack();
                    insertBool(labelTrue, labelExit);
                    break;
                case IRBinaryOperator.NEQ:
                    instructions.add(new JVMFlCmp());
                    decStack();
                    labelTrue = getLabel(null);
                    labelExit = getLabel(null);
                    instructions.add(new JVMIfGT(labelTrue));
                    decStack();
                    insertBool(labelTrue, labelExit);
                    break;
                case IRBinaryOperator.GREATERTHAN:
                    instructions.add(new JVMFlCmp());
                    decStack();
                    labelTrue = getLabel(null);
                    labelExit = getLabel(null);
                    instructions.add(new JVMIfGT(labelTrue));
                    decStack();
                    insertBool(labelTrue, labelExit);
                    break;
                case IRBinaryOperator.GREATERTHANEQ:
                    instructions.add(new JVMFlCmp());
                    decStack();
                    labelTrue = getLabel(null);
                    labelExit = getLabel(null);
                    instructions.add(new JVMIfGE(labelTrue));
                    decStack();
                    insertBool(labelTrue, labelExit);
                    break;
                case IRBinaryOperator.LESSTHAN:
                    instructions.add(new JVMFlCmp());
                    decStack();
                    labelTrue = getLabel(null);
                    labelExit = getLabel(null);
                    instructions.add(new JVMIfLT(labelTrue));
                    decStack();
                    insertBool(labelTrue, labelExit);
                    break;
                case IRBinaryOperator.LESSTHANEQ:
                    instructions.add(new JVMFlCmp());
                    decStack();
                    labelTrue = getLabel(null);
                    labelExit = getLabel(null);
                    instructions.add(new JVMIfLE(labelTrue));
                    decStack();
                    insertBool(labelTrue, labelExit);
                    break;
                    
            }

        } else {
            JVMLabel labelTrue;
            JVMLabel labelExit;
            switch (node.getOp()) {
                case IRBinaryOperator.ADD:
                    instructions.add(new JVMAdd(node.getTemp().getType().getType()));
                    decStack();
                    break;
                case IRBinaryOperator.MULT:
                    instructions.add(new JVMMul(node.getTemp().getType().getType()));
                    decStack();
                    break;
                case IRBinaryOperator.SUB:
                    instructions.add(new JVMSub(node.getTemp().getType().getType()));
                    decStack();
                    break;
                case IRBinaryOperator.DIV:
                    instructions.add(new JVMDiv(node.getTemp().getType().getType()));
                    decStack();
                    break;
                case IRBinaryOperator.REM:
                    instructions.add(new JVMAdd(node.getTemp().getType().getType()));
                    decStack();
                    break;
                case IRBinaryOperator.EQ:
                    instructions.add(new JVMSub(node.getTemp().getType().getType()));
                    decStack();
                    labelTrue = getLabel(null);
                    labelExit = getLabel(null);
                    instructions.add(new JVMIfEQ(labelTrue));
                    decStack();
                    insertBool(labelTrue, labelExit);
                    break;
                case IRBinaryOperator.NEQ:
                    instructions.add(new JVMSub(node.getTemp().getType().getType()));
                    decStack();
                    labelTrue = getLabel(null);
                    labelExit = getLabel(null);
                    instructions.add(new JVMIfNE(labelTrue));
                    decStack();
                    insertBool(labelTrue, labelExit);
                    break;
                case IRBinaryOperator.GREATERTHAN:
                    instructions.add(new JVMSub(node.getTemp().getType().getType()));
                    decStack();
                    labelTrue = getLabel(null);
                    labelExit = getLabel(null);
                    instructions.add(new JVMIfGT(labelTrue));
                    decStack();
                    insertBool(labelTrue, labelExit);
                    break;
                case IRBinaryOperator.GREATERTHANEQ:
                    instructions.add(new JVMSub(node.getTemp().getType().getType()));
                    decStack();
                    labelTrue = getLabel(null);
                    labelExit = getLabel(null);
                    instructions.add(new JVMIfGE(labelTrue));
                    decStack();
                    insertBool(labelTrue, labelExit);
                    break;
                case IRBinaryOperator.LESSTHAN:
                    instructions.add(new JVMSub(node.getTemp().getType().getType()));
                    decStack();
                    labelTrue = getLabel(null);
                    labelExit = getLabel(null);
                    instructions.add(new JVMIfLT(labelTrue));
                    decStack();
                    insertBool(labelTrue, labelExit);
                    break;
                case IRBinaryOperator.LESSTHANEQ:
                    instructions.add(new JVMSub(node.getTemp().getType().getType()));
                    decStack();
                    labelTrue = getLabel(null);
                    labelExit = getLabel(null);
                    instructions.add(new JVMIfLE(labelTrue));
                    decStack();
                    insertBool(labelTrue, labelExit);
                    break;
            }
        }

        if (node.getTemp().getLocal() || node.getTemp().getParam()) {
            storeLocal(node.getTemp());
            decStack();
        }

        return null;

    }

    public JVMNode visit(IRAssignmentConstant node) {
        IRConstant value = node.getConstant();

        if (value instanceof IRInt) {
            IRInt valueTypecast = (IRInt) value;
            instructions.add(new JVMldc(new JVMInt(valueTypecast.getVal())));
            incStack();
        } else if (value instanceof IRBool) {
            IRBool valueTypecast = (IRBool) value;
            if (valueTypecast.getVal() == true) {
                instructions.add(new JVMldc(new JVMInt(1)));
                incStack();
            } else {
                instructions.add(new JVMldc(new JVMInt(0)));
                incStack();
            }
        } else if (value instanceof IRChar) {
            IRChar valueTypecast = (IRChar) value;
            instructions.add(new JVMldc(new JVMChar(valueTypecast.getVal())));
            incStack();
        } else if (value instanceof IRFloat) {
            IRFloat valueTypecast = (IRFloat) value;
            instructions.add(new JVMldc(new JVMFloat(valueTypecast.getVal())));
            incStack();
        } else {
            IRString valueTypecast = (IRString) value;
            instructions.add(new JVMldc(new JVMString(valueTypecast.getVal())));
            incStack();
        }

        if (node.getTemp().getLocal() || node.getTemp().getParam()) {
            storeLocal(node.getTemp());
            decStack();
        }


        return null;

    }

    public JVMNode visit(IRAssignmentFromArray node) {

        instructions.add(new JVMLoad(node.getArray().getNum(), 'a'));
        incStack();
        instructions.add(new JVMSwap());
        instructions.add(new JVMArrayLoad(node.getArray().getType().getType()));
        incStack();

        if (node.getTemp().getLocal()  || node.getTemp().getParam()) {
            storeLocal(node.getTemp());
            decStack();
        }

        return null;
    }

    public JVMNode visit(IRAssignmentNewArray node) {

        instructions.add(new JVMldc(new JVMInt(node.getSize())));
        incStack();
        instructions.add(new JVMNewArray(node.getTemp().getType().getType()));
        
        if (node.getTemp().getLocal()  || node.getTemp().getParam()) {
            instructions.add(new JVMStore(node.getTemp().getNum(), 'a'));
            decStack();
        }

        return null;

    }

    public JVMNode visit(IRAssignmentToArray node) {

        instructions.add(new JVMLoad(node.getTemp().getNum(), 'a'));
        incStack();
        instructions.add(new JVMDup(2));
        incStack();
        instructions.add(new JVMPop());
        decStack();
        instructions.add(new JVMArrayStore(node.getTemp().getType().getType()));
        decStack();

        return null;
    }

    public JVMNode visit(IRAssignmentOperand node) {

        if (node.getOperand().getLocal()  || node.getOperand().getParam()) {
            loadLocal(node.getOperand());
            incStack();
        }

        if (node.getTemp().getLocal()  || node.getTemp().getParam()) {
            storeLocal(node.getTemp());
            decStack();
        }

        return null;

    }

    public JVMNode visit(IRAssignmentUnary node) {
        /*if (node.getOperand().getLocal()) {
            loadLocal(node.getTemp());
        }*/

        

        if (node.getTemp().getLocal()  || node.getTemp().getParam()) {
            storeLocal(node.getTemp());
            decStack();
        }

        return null;
    }

    public JVMNode visit(IRAssignmentTypecast node) {
        /*if (node.getRhs().getLocal()) {
            loadLocal(node.getRhs());
        }*/

        instructions.add(new JVMTypecast(convertType(node.getFromType().getType()), convertType(node.getToType().getType())));

        if (node.getTemp().getLocal()  || node.getTemp().getParam()) {
            storeLocal(node.getTemp());
            decStack();
        }

        return null;
    }

    public JVMNode visit(IRIfGoto node) {
        /*if (node.getTemp().getLocal()) {
            loadLocal(node.getTemp());
        }*/

        JVMLabel label = getLabel(node.getLabel());

        instructions.add(new JVMIfNE(label));
        decStack();

        return null;
    }

    public JVMNode visit(IRCall node) {

        instructions.add(new JVMCall(funcMap.get(node.getFuncName()), className, node.getFuncName()));

        for (TemporaryIRNode temp : node.getParams()) {
            decStack();
        }

        if (funcMap.get(node.getFuncName()).getType().getType() != 'V') {
            incStack();
        }
        

        if (node.getTemp() != null && (node.getTemp().getLocal()  || node.getTemp().getParam())) {
            storeLocal(node.getTemp());
            decStack();
        }

        return null;
    }

    public JVMNode visit(IRGoto node) {

        if (terminated) {
            return null;
        }

        JVMLabel label = getLabel(node.getLabel());
        
        instructions.add(new JVMGoto(label));

        return null;
    }

    public JVMNode visit(IRLabelInst node) {

        JVMLabel label = getLabel(node.getLabel());
        
        instructions.add(new JVMLabelInst(label));

        terminated = false;

        return null;
    }

    public JVMNode visit(IRReturn node) {
        if (node.getTemp() != null && (node.getTemp().getLocal()  || node.getTemp().getParam())) {
            loadLocal(node.getTemp());
        }

        if (node.getTemp() != null) {
            instructions.add(new JVMReturn(convertType(node.getTemp().getType().getType())));
        } else {
            instructions.add(new JVMReturn('v'));
        }

        terminated = true;

        return null;
    }

    public JVMNode visit(PrintIRNode node) {

        if (node.getOperand().getLocal() || node.getOperand().getParam()) {
            loadLocal(node.getOperand());
            incStack();
        }

        instructions.add(new JVMGetStatic("java/lang/System/out Ljava/io/PrintStream;"));
        incStack();
        instructions.add(new JVMSwap());

        instructions.add(new JVMPrint(node.getLine(), node.getOperand().getType().getType()));
        decStack();
        decStack();

        return null;
    }

    public char convertType(char type) {
        switch (type) {
            case 'U':
                return 'a';
            case 'F':
                return 'f';
            case 'V':
                return 'v';
            default:
                return 'i';
        }
    }

    public void loadLocal(TemporaryIRNode temp) {
        instructions.add(new JVMLoad(temp.getNum(), convertType(temp.getType().getType())));
    }

    public void storeLocal(TemporaryIRNode temp)  {
        instructions.add(new JVMStore(temp.getNum(), convertType(temp.getType().getType())));
    }

    public JVMLabel getLabel(IRLabel oldLabel) {
        if (oldLabel == null) {
            JVMLabel label = new JVMLabel(numLabels);
            numLabels++;
            return label;
        }

        if (labelMap.get(oldLabel) == null) {
            JVMLabel label = new JVMLabel(numLabels);
            numLabels++;
            labelMap.put(oldLabel, label);
            return label;
        } else {
            return labelMap.get(oldLabel);
        }
    }

    public void insertBool(JVMLabel labelTrue, JVMLabel labelExit) {
        instructions.add(new JVMldc(new JVMInt(0)));
        instructions.add(new JVMGoto(labelExit));
        instructions.add(new JVMLabelInst(labelTrue));
        instructions.add(new JVMldc(new JVMInt(1)));
        instructions.add(new JVMLabelInst(labelExit));
        incStack();
    }

    public void decStack() {

        numStack--;

    }

    public void incStack() {

        numStack++;

        if (numStack > largestStack) {
            largestStack = numStack;
        }

    }

}