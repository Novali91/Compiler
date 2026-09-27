package jvm_nodes.jvm_instructions;

import jvm_nodes.JVMInstruction;

public class JVMPop extends JVMInstruction {

    public JVMPop() {

    }

    public void outputJVM(int scope) {
        super.outputJVM(scope);
        System.out.println("pop");
    }
    
}