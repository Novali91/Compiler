package jvm_nodes.jvm_instructions.jvm_if;

import jvm_nodes.JVMInstruction;
import jvm_nodes.JVMLabel;

public class JVMIfEQ extends JVMInstruction {

    JVMLabel label;

    public JVMIfEQ(JVMLabel label) {
        this.label = label;
    }

    public void outputJVM(int scope) {
        super.outputJVM(scope);
        System.out.print(String.format("ifeq "));
        label.outputJVM();
        System.out.println();
    }

}