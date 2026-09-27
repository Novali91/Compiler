package jvm_nodes.jvm_instructions;

import jvm_nodes.JVMInstruction;

public class JVMTypecast extends JVMInstruction {

    char typeFrom;
    char typeTo;

    public JVMTypecast(char typeFrom, char typeTo) {
        this.typeFrom = typeFrom;
        this.typeTo = typeTo;
    }

    public void outputJVM(int scope) {
        super.outputJVM(scope);
        System.out.println(String.format("%c2%c", typeFrom, typeTo));
    }

}