package jvm_nodes.jvm_instructions;

import jvm_nodes.JVMInstruction;

public class JVMNew extends JVMInstruction {
    
    String method;

    public JVMNew(String method) {
        this.method = method;
    }

    public void outputJVM(int scope) {
        super.outputJVM(scope);
        System.out.println(String.format("new %s", method));
    }
}