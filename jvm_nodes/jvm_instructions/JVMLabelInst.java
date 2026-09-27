package jvm_nodes.jvm_instructions;

import jvm_nodes.JVMInstruction;
import jvm_nodes.JVMLabel;

public class JVMLabelInst extends JVMInstruction {

    JVMLabel label;

    public JVMLabelInst(JVMLabel label) {
        this.label = label;
    }

    public void outputJVM(int scope) {
        label.outputJVM();
        System.out.println(":");
    }

}