/*
 * Compiler.java
 *
 * Some starter code for a task to be completed as part of
 * Assignment #1, UVic CSC 435, Summer 2026
 *
 */

/* This code assumes that the most recent version of ANTLR4 
 * is already available (as of this writing -- 2026-05-25 -- version
 * 4.13.2 from https://www.antlr.org/).  In practical terms this
 * means that the file `antlr-4.13.2-complete.jar` is in your
 * development environments CLASSPATH.
 *
 */

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.misc.*;
import java.io.*;

public class Compiler {
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

        parser.removeErrorListeners();
        lexer.removeErrorListeners();
        parser.addErrorListener(new BailErrorStrategyListener());

        try {
            parser.program();
            System.out.println("success");
        } catch (Exception e) {
            System.out.println("failure");
        }
    }


    // Custom error listener that throws on syntax errors
    private static class BailErrorStrategyListener 
        extends BaseErrorListener
    {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, 
            Object offendingSymbol,
            int line, 
            int charPositionInLine,
            String msg, 
            RecognitionException e) throws RecognitionException
        {
            throw new RuntimeException("Syntax error at line " 
                + line + ":" + charPositionInLine);
        }
    }
}
