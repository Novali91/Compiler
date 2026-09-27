package jvm_nodes.jvm_instructions;

import ir_types.IRFuncType;
import ir_types.IRType;
import jvm_nodes.JVMInstruction;

public class JVMCall extends JVMInstruction {

    IRFuncType funcType;
    String className;
    String funcName;

    public JVMCall(IRFuncType funcType, String className, String funcName) {
        this.funcType = funcType;
        this.className = className;
        this.funcName = funcName;
    }

    public void outputJVM(int scope) {
        super.outputJVM(scope);

        StringBuilder conglomerateType = new StringBuilder();
        for (IRType var : funcType.getParams()) {
            if (var.getArray()) {
                conglomerateType.append('[');
            }

            if (var.getType() == 'U') {
                conglomerateType.append("Ljava/lang/String;");
            } else {
                conglomerateType.append(var.getType());
            }
        }

        String conglomerateString = conglomerateType.toString();

        StringBuilder returnType = new StringBuilder();

        if (funcType.getType().getArray()) {
            returnType.append('[');
        }

        if (funcType.getType().getType() == 'U') {
            returnType.append("Ljava/lang/String;");
        } else {
            returnType.append(funcType.getType().getType());
        }

        String returnString = returnType.toString();

        System.out.println(String.format("invokestatic %s/%s(%s)%s", className, funcName, conglomerateString, returnString));
    }
}