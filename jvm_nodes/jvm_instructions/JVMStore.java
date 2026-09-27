package jvm_nodes.jvm_instructions;

import jvm_nodes.JVMInstruction;

public class JVMStore extends JVMInstruction {

    int num;

    // 'a', 'i', 'f'
    char type;

    public JVMStore(int num, char type) {
        this.num = num;
        this.type = type;
    }

    public void outputJVM(int scope) {
        super.outputJVM(scope);
        System.out.println(String.format("%cstore %d", type, num));
    }

}