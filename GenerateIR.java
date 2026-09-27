import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.misc.*;
import java.io.*;
import nodes.*;
import exceptions.*;
import ir_nodes.*;
import ast_visitors.MakeIRVisitor;

public class GenerateIR {

	public static void main (String[] args) throws Exception {
        String inputFile = null;
        if ( args.length > 0 ) {
            inputFile = args[0];
        }
        InputStream is = System.in;
        if ( inputFile != null ) {
            is = new FileInputStream(inputFile);
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
        prog.outputIR();
    }
}