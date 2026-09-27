package jvm_nodes;

public class JVMInstruction extends JVMNode {

    public void outputJVM(int scope) {
        for (int i = 0; i < scope; i++) {
            System.out.print("    ");
        }
    }

}