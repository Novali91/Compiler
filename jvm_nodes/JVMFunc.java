package jvm_nodes;

import ir_types.*;
import java.util.ArrayList;

public class JVMFunc extends JVMNode {

    String name;
    IRFuncType type;
    int numStack;
    int numLocals;
    ArrayList<JVMInstruction> instructions;

    // In outputJVM: Should check if there is a return
    // If name is main, make it __main

    public JVMFunc(String name, IRFuncType type, int numStack, int numLocals, ArrayList<JVMInstruction> instructions) {
        this.name = name;
        this.type = type;
        this.numStack = numStack;
        this.numLocals = numLocals;
        this.instructions = instructions;
    }

    public void outputJVM(int scope) {
        StringBuilder conglomerateType = new StringBuilder();
        for (IRType var : type.getParams()) {
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

        if (type.getType().getArray()) {
            returnType.append('[');
        }

        if (type.getType().getType() == 'U') {
            returnType.append("Ljava/lang/String;");
        } else {
            returnType.append(type.getType().getType());
        }

        String returnString = returnType.toString();

        String actualName = name;

        if (name.equals("main")) {
            actualName = "__main";
        }

        System.out.println(String.format(".method public static %s(%s)%s", actualName, conglomerateString, returnString));
        System.out.println(String.format("    .limit locals %d", numLocals));
        System.out.println(String.format("    .limit stack %d", numStack));

        for (JVMInstruction inst : instructions) {
            inst.outputJVM(1);
        }

        if (type.getType().getType() == 'V') {
            System.out.println("    return");
        }

        System.out.println(".end method");
        System.out.println();
    }
}