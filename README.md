This is a compiler for a simple language that is similar to C in both syntax and structure. It was originally created as assignment for my university's compilers course, however I have done a few further optimizations (specifically pertaining to the JVM bytecode output) since. <br /> 
An outline: <br /> 
It uses ANTLR4 (With the Ceelish grammar file tailored towards the ANTLR4 API) to lex and then parse your input file.<br /> 
Then, MakeASTVisitor.java is used to turn the output parse tree into an AST.<br /> 
From this AST, typechecking is then done.<br /> 
After typechecking, it gets translated into a TAC IR from MakeIRVisitor.java, with some hardware-independent optimizations being done at this step.<br /> 
Then, it is converted into JVM Bytecode using Ceelish2Jasmin.java, where a few more optimizations are done, and then from there, Jasmin is used to convert this bytecode into a .class file.<br /> 
<br /> 
To use this compiler:<br /> 
make grammar<br /> 
make Ceelish2Jasmin<br /> 
java Ceelish2Jasmin source_file.cee > source_file.j<br /> 
java -jar jasmin.jar source_file.j<br /> 
java source_file.j<br /> 
