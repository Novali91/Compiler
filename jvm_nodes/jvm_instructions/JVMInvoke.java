package jvm_nodes.jvm_instructions;

import jvm_nodes.JVMInstruction;

public class JVMInvoke extends JVMInstruction {
    
    String method;

    public JVMInvoke(String method) {
        this.method = method;
    }

    public void outputJVM(int scope) {
        super.outputJVM(scope);
        System.out.println(String.format("invokevirtual %s", method));
    }
}