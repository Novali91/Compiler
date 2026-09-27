package jvm_nodes.jvm_instructions.jvm_binops;

import jvm_nodes.JVMInstruction;

public class JVMRem extends JVMInstruction {

    char type; // 'i' or 'f'

    public JVMRem(char type) {
        switch (type) {
            case 'F':
                this.type = 'f';
                break;
            default:
                this.type = 'i';
                break;
        }
    }

    public void outputJVM(int scope) {
        super.outputJVM(scope);
        System.out.println(String.format("%srem", type));
    }
}