package jvm_nodes.jvm_instructions;

import jvm_nodes.JVMInstruction;

public class JVMLoad extends JVMInstruction {

    int num;

    // 'a', 'i', 'f'
    char type;

    public JVMLoad(int num, char type) {
        this.num = num;
        this.type = type;
    }

    public void outputJVM(int scope) {
        super.outputJVM(scope);
        System.out.println(String.format("%cload %d", type, num));
    }

}