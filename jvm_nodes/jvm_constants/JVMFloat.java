package jvm_nodes.jvm_constants;

public class JVMFloat extends JVMConstant {

    float val;

    public JVMFloat(float val) {
        this.val = val;
    }

    public void outputJVM(int scope) {
        System.out.print(val);
    }
}