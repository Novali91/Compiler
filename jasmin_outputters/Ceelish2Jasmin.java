import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.misc.*;
import java.io.*;
import nodes.*;
import exceptions.*;
import ir_nodes.*;
import ast_visitors.MakeIRVisitor;
import ir_visitors.MakeJVMVisitor;
import jvm_nodes.*;

public class Ceelish2Jasmin {

	public static void main (String[] args) throws Exception {
        String inputFile = null;
        if ( args.length > 0 ) {
            inputFile = args[0];
        }
        InputStream is = System.in;

        String programName = null;

        if (inputFile != null) {
            is = new FileInputStream(inputFile);
            
            File file = new File(inputFile);
            programName = file.getName();

            int dot = programName.lastIndexOf('.');
            if (dot > 0) {
                programName = programName.substring(0, dot);
            }
        }   

        CharStream input = CharStreams.fromStream(is);
        CeelishLexer lexer = new CeelishLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CeelishParser parser = new CeelishParser(tokens);

        
        parser.setBuildParseTree(true);
        ParseTree tree = parser.program();

        if (parser.getNumberOfSyntaxErrors() > 0) {
            throw new RuntimeException("Parsing error");
        }

        MakeASTVisitor build = new MakeASTVisitor();
        ASTNode ast = build.visit(tree);
        ast = (ProgramNode) ast;
        try {
            ast.existenceUniquenessCheck(null);
        } catch (TypecheckException e) {
            System.out.println(e);
            System.exit(1);
        }

        try {
            ast.typeCheck();
        } catch (TypecheckException e) {
            System.out.println(e);
            System.exit(1);
        }

        MakeIRVisitor irVisit = new MakeIRVisitor();
        IRNode prog = ast.accept(irVisit);

        ProgramIRNode progTypecast = (ProgramIRNode) prog;

        progTypecast.setClassName(programName);
        
        
        MakeJVMVisitor visitJVM = new MakeJVMVisitor();

        JVMNode progJVM = progTypecast.accept(visitJVM);

        progJVM.outputJVM(0);

    }
}