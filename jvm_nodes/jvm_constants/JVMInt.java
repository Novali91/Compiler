package jvm_nodes.jvm_constants;

public class JVMInt extends JVMConstant {

    int val;

    public JVMInt(int val) {
        this.val = val;
    }

    public void outputJVM(int scope) {
        System.out.print(val);
    }
}