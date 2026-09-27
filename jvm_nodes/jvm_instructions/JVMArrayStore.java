package jvm_nodes.jvm_instructions;

import jvm_nodes.JVMInstruction;

public class JVMArrayStore extends JVMInstruction {

    char type;

    public JVMArrayStore(char type) {
        switch (type) {
            case 'I':
                this.type = 'i';
                break;
            case 'C':
                this.type = 'c';
                break;
            case 'Z':
                this.type = 'b';
                break;
            case 'U':
                this.type = 'a';
                break;
            default:
                this.type = 'f';
                break;
        }
    }

    public void outputJVM(int scope) {
        super.outputJVM(scope);
        System.out.println(String.format("%castore", type));
    }
}