package jvm_nodes.jvm_instructions;

import jvm_nodes.JVMInstruction;

public class JVMGetStatic extends JVMInstruction {
    
    String str;

    public JVMGetStatic(String str) {
        this.str = str;
    }

    public void outputJVM(int scope) {
        super.outputJVM(scope);
        System.out.println(String.format("getstatic %s", str));
    }
}