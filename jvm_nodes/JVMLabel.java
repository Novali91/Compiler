package jvm_nodes;

public class JVMLabel {

    int num;

    public JVMLabel(int num) {
        this.num = num;
    }

    public void outputJVM() {
        System.out.print(String.format("L_%d", num));
    }

}