package jvm_nodes.jvm_constants;

public class JVMString extends JVMConstant {

    String val;

    public JVMString(String val) {
        this.val = val;
    }

    public void outputJVM(int scope) {
        System.out.print(val);
    }
}