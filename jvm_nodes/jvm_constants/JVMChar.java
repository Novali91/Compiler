package jvm_nodes.jvm_constants;

public class JVMChar extends JVMConstant {

    char val;

    public JVMChar(char val) {
        this.val = val;
    }

    public void outputJVM(int scope) {
        int valInt = val;
        System.out.print(valInt);
    }
}