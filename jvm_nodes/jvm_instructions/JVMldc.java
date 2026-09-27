package jvm_nodes.jvm_instructions;

import jvm_nodes.JVMInstruction;
import jvm_nodes.jvm_constants.JVMConstant;

public class JVMldc extends JVMInstruction {
    
    JVMConstant val;

    public JVMldc(JVMConstant val) {
        this.val = val;
    }

    public void outputJVM(int scope) {
        super.outputJVM(scope);
        System.out.print(String.format("ldc "));
        val.outputJVM(scope);
        System.out.println();
    }

}
