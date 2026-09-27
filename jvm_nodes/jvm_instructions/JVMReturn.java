package jvm_nodes.jvm_instructions;

import jvm_nodes.JVMInstruction;

public class JVMReturn extends JVMInstruction {

    char type;

    public JVMReturn(char type) {
        this.type = type;
    }

    public void outputJVM(int scope) {
        super.outputJVM(scope);
        if (type == 'v') {
            System.out.println("return");
        } else {
            System.out.println(String.format("%creturn", type));
        }
    }
}