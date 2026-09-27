package jvm_nodes.jvm_instructions;

import jvm_nodes.JVMInstruction;

public class JVMInvokeSpecial extends JVMInstruction {
    
    String method;

    public JVMInvokeSpecial(String method) {
        this.method = method;
    }

    public void outputJVM(int scope) {
        super.outputJVM(scope);
        System.out.println(String.format("invokespecial %s", method));
    }
}