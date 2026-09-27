package jvm_nodes.jvm_instructions;

import jvm_nodes.JVMInstruction;

public class JVMSwap extends JVMInstruction {

    public JVMSwap() {

    }

    public void outputJVM(int scope) {
        super.outputJVM(scope);
        System.out.println("swap");
    }
    
}