package  jvm_nodes.jvm_instructions;

import jvm_nodes.JVMInstruction;

public class JVMDup extends JVMInstruction{

    int num;

    public JVMDup(int num) {
        this.num = num;
    }

    public void outputJVM(int scope) {
        super.outputJVM(scope);
        if (num == 0) {
            System.out.println("dup");
        } else {
            System.out.println("dup_x2");
        }
    }
}