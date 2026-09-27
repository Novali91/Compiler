GRAMMAR_NAME=Ceelish
GRAMMAR_SRC=$(GRAMMAR_NAME).g4

all: grammar compiler prettyprinter check generateIR Ceelish2Jasmin

grammar: $(GRAMMAR_SRC)
	java org.antlr.v4.Tool -visitor -no-listener $(GRAMMAR_SRC)

check:
	javac Ceelish*.java
	javac nodes/*.java
	javac symbols/*.java
	javac exceptions/*.java
	javac type_equiv/*.java
	javac MakeASTVisitor.java
	javac Check.java

generateIR:
	javac Ceelish*.java
	javac nodes/*.java
	javac symbols/*.java
	javac exceptions/*.java
	javac type_equiv/*.java
	javac ir_constants/*.java
	javac ir_labels/*.java
	javac ir_nodes/*.java
	javac ir_nodes/ir_instructions/*.java
	javac ir_nodes/ir_instructions/ir_assignments/*.java
	javac ir_nodes/ir_instructions/ir_controlflow/*.java
	javac ast_visitors/MakeIRVisitor.java
	javac ir_ops/*.java
	javac ir_types/*.java
	javac MakeASTVisitor.java
	javac Check.java
	javac GenerateIR.java

Ceelish2Jasmin:
	javac Ceelish*.java
	javac nodes/*.java
	javac symbols/*.java
	javac exceptions/*.java
	javac type_equiv/*.java
	javac ir_constants/*.java
	javac ir_labels/*.java
	javac ir_nodes/*.java
	javac ir_nodes/ir_instructions/*.java
	javac ir_nodes/ir_instructions/ir_assignments/*.java
	javac ir_nodes/ir_instructions/ir_controlflow/*.java
	javac ast_visitors/MakeIRVisitor.java
	javac ir_ops/*.java
	javac ir_types/*.java
	javac MakeASTVisitor.java
	javac ir_visitors/MakeJVMVisitor.java
	javac jasmin_outputters/Ceelish2Jasmin.java
	mv jasmin_outputters/Ceelish2Jasmin.class .


prettyprinter:
	javac Ceelish*.java
	javac nodes/*.java
	javac MakeASTVisitor.java
	javac PrettyPrinter.java

compiler:
	javac Ceelish*.java 
	javac nodes/*.java
	javac MakeASTVisitor.java
	javac Compiler.java

clean:
	\rm -f *.class $(GRAMMAR_NAME)*.java $(GRAMMAR_NAME)*.tokens
	\rm -f $(GRAMMAR_NAME)*.lexer $(GRAMMAR_NAME)*.interp
	\rm -f nodes/*.class
	\rm -f symbols/*.class
	\rm -f exceptions/*.class
	\rm -f symbol_table/*.class
	\rm -f type_equiv/*.class
	\rm -f ir_constants/*.class
	\rm -f ir_labels/*.class
	\rm -f ir_nodes/*.class
	\rm -f ir_nodes/ir_instructions/*.class
	\rm -f ir_nodes/ir_instructions/ir_assignments/*.class
	\rm -f ir_nodes/ir_instructions/ir_controlflow/*.class
	\rm -f ast_visitors/MakeIRVisitor.class
	\rm -f ir_ops/*.class
	\rm -f ir_types/*.class
	\rm -f GenerateIR.class
	\rm -f jvm_nodes/*.class
	\rm -f jvm_nodes/jvm_instructions/*.class
	\rm -f jvm_nodes/jvm_instructions/jvm_binops/*.class
	\rm -f jvm_nodes/jvm_instructions/jvm_if/*.class
	\rm -f ir_visitors/*.class
	\rm -f jvm_nodes/jvm_constants/*.class
	\rm -f *.j

