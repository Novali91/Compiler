This is a compiler for a simple language that is similar to C in both syntax and structure. It was originally created as assignment for my university's compilers course, however I have done a few further optimizations (specifically pertaining to the JVM bytecode output) since.
An outline:
It uses ANTLR4 (With the Ceelish grammar file tailored towards the ANTLR4 API) to lex and then parse your input file.
Then, MakeASTVisitor.java is used to turn the output parse tree into an AST.
From this AST, typechecking is then done.
After typechecking, it gets translated into a TAC IR from MakeIRVisitor.java, with some hardware-independent optimizations being done at this step.
Then, it is converted into JVM Bytecode using Ceelish2Jasmin.java, where a few more optimizations are done, and then from there, Jasmin is used to convert this bytecode into a .class file.

To use this compiler:
make grammar
make Ceelish2Jasmin
java Ceelish2Jasmin source_file.cee > source_file.j
java -jar jasmin.jar source_file.j
java source_file.j
