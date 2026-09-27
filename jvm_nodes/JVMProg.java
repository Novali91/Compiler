package jvm_nodes;

import java.util.ArrayList;

public class JVMProg extends JVMNode {

    String className;
    ArrayList<JVMFunc> funcs;

    public JVMProg(String className, ArrayList<JVMFunc> funcs) {
        this.className = className;
        this.funcs = funcs;
    }

    public void outputJVM(int scope) {
        System.out.println(String.format(".source %s.cee", className));
        System.out.println(String.format(".class public %s", className));
        System.out.println(".super java/lang/Object");
        System.out.println();

        for (JVMFunc func : funcs) {
            func.outputJVM(scope);
        }

        System.out.println(".method public static main([Ljava/lang/String;)V");
        System.out.println("    .limit locals 1");
        System.out.println("    .limit stack 4");
        System.out.println(String.format("    invokestatic %s/__main()V", className));
        System.out.println("    return");
        System.out.print(".end method");
        System.out.println();
        System.out.println(".method public <init>()V");
        System.out.println("    aload_0");
        System.out.println("    invokenonvirtual java/lang/Object/<init>()V");
        System.out.println("    return");
        System.out.println(".end method");
    }
}