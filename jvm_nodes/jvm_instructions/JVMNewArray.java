package jvm_nodes.jvm_instructions;

import jvm_nodes.JVMInstruction;

public class JVMNewArray extends JVMInstruction {

    String type;
    // if type == String: anewarray, else newarray

    public JVMNewArray(char type) {
        switch (type) {
            case 'I':
                this.type = "int";
                break;
            case 'C':
                this.type = "char";
                break;
            case 'Z':
                this.type = "boolean";
                break;
            case 'U':
                this.type = "String";
                break;
            default:
                this.type = "float";
                break;
        }
    }

    public void outputJVM(int scope) {
        super.outputJVM(scope);
        if (type.equals("String")) {
            System.out.println("anewarray java/lang/String");
        } else {
            System.out.println(String.format("newarray %s", type));
        }
    }
}