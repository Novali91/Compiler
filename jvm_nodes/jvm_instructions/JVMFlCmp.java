package jvm_nodes.jvm_instructions;

import jvm_nodes.JVMInstruction;

public class JVMFlCmp extends JVMInstruction {
    // fcmpl
    public JVMFlCmp() {

    }

    public void outputJVM(int scope) {
        super.outputJVM(scope);
        System.out.println("fcmpl");
    }

}