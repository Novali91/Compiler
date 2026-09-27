.source strcmp.cee
.class public strcmp
.super java/lang/Object

.method public static __main()V
    .limit locals 2
    .limit stack 26
    ldc 5
    anewarray java/lang/String
    astore 0
    ldc 0
    ldc "a"
    aload 0
    dup_x2
    pop
    aastore
    ldc 1
    ldc "b"
    aload 0
    dup_x2
    pop
    aastore
    ldc 2
    ldc "c"
    aload 0
    dup_x2
    pop
    aastore
    ldc 0
    aload 0
    swap
    aaload
    getstatic java/lang/System/out Ljava/io/PrintStream;
    swap
    invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
    ldc 1
    aload 0
    swap
    aaload
    getstatic java/lang/System/out Ljava/io/PrintStream;
    swap
    invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
    ldc 2
    aload 0
    swap
    aaload
    getstatic java/lang/System/out Ljava/io/PrintStream;
    swap
    invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
    ldc 0
    aload 0
    swap
    aaload
    ldc 1
    aload 0
    swap
    aaload
    invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
    istore 1
    iload 1
    getstatic java/lang/System/out Ljava/io/PrintStream;
    swap
    invokevirtual java/io/PrintStream/println(Z)V
    ldc 0
    aload 0
    swap
    aaload
    ldc 1
    aload 0
    swap
    aaload
    invokevirtual java/lang/String/compareTo(Ljava/lang/String;)I
    iflt L_0
    ldc 0
    goto L_1
L_0:
    ldc 1
L_1:
    istore 1
    iload 1
    getstatic java/lang/System/out Ljava/io/PrintStream;
    swap
    invokevirtual java/io/PrintStream/println(Z)V
    ldc 1
    aload 0
    swap
    aaload
    ldc 0
    aload 0
    swap
    aaload
    invokevirtual java/lang/String/compareTo(Ljava/lang/String;)I
    iflt L_2
    ldc 0
    goto L_3
L_2:
    ldc 1
L_3:
    istore 1
    iload 1
    getstatic java/lang/System/out Ljava/io/PrintStream;
    swap
    invokevirtual java/io/PrintStream/println(Z)V
    return
.end method

.method public static main([Ljava/lang/String;)V
    .limit locals 1
    .limit stack 4
    invokestatic strcmp/__main()V
    return
.end method
.method public <init>()V
    aload_0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method
