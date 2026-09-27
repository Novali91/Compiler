package jvm_nodes.jvm_instructions;

import jvm_nodes.JVMInstruction;

public class JVMPrint extends JVMInstruction {
    
    boolean ln;
    char type; // Use IR types

    public JVMPrint(boolean ln, char type) {
        this.ln = ln;
        this.type = type;
    }

    // java/io/PrintStream/println(I)V
    // Ljava/lang/String;

    public void outputJVM(int scope) {
        super.outputJVM(scope);

        String line;

        if (ln) {
            line = "println";
        } else {
            line = "print";
        }

        if (type == 'U') {
            System.out.println(String.format("invokevirtual java/io/PrintStream/%s(Ljava/lang/String;)V", line));
        } else {
            System.out.println(String.format("invokevirtual java/io/PrintStream/%s(%c)V", line, type));
        }
    }

}