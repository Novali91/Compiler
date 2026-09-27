package jvm_nodes.jvm_instructions;

import jvm_nodes.JVMInstruction;
import jvm_nodes.JVMLabel;

public class JVMGoto extends JVMInstruction {

    JVMLabel label;

    public JVMGoto(JVMLabel label) {
        this.label = label;
    }

    public void outputJVM(int scope) {
        super.outputJVM(scope);
        System.out.print(String.format("goto "));
        label.outputJVM();
        System.out.println();
    }
}