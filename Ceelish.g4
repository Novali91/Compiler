grammar Ceelish;

// Grammar Rules:

program: func+ ;
func: funcDecl funcBlock ;
funcDecl: compoundType IDENT LPAREN formalParams RPAREN ;
formalParams: compoundType IDENT moreFormals*
            |
            ;
moreFormals: COMMA compoundType IDENT ;
funcBlock: LBRACE varDecl* statement* RBRACE ;
varDecl: compoundType IDENT SEMICOLON ;
compoundType: type # basicType
            | type '['INTEGERCONSTANT']' # arrayType ; 
type: INT
    | FLOAT
    | CHAR
    | STRING
    | BOOLEAN
    | VOID ;
statement: SEMICOLON # emptyStatement
         | expr SEMICOLON # exprStatement
         | IF LPAREN expr RPAREN block # ifStatement
         | IF LPAREN expr RPAREN block ELSE block # ifElseStatement
         | WHILE LPAREN expr RPAREN block # whileStatement
         | PRINT expr SEMICOLON # printStatement
         | PRINTLN expr SEMICOLON # printlnStatement
         | RETURN expr? SEMICOLON # returnStatement
         | IDENT '=' expr SEMICOLON # assignmentStatement
         | IDENT '[' expr ']' '=' expr SEMICOLON # arrayAssignmentStatement ;

block: LBRACE statement* RBRACE ;
expr: expr MULTIPLY expr # multiplyExpression
    | expr addOrSub expr # addOrSubExpression
    | expr LESSTHAN expr # lessThanExpression
    | expr EQUIVALENT expr # equivalentExpression
    | IDENT '[' expr ']' # arrayAccessExpression
    | IDENT LPAREN exprList RPAREN # funcCallExpression
    | IDENT # identExpression
    | literal # literalExpression
    | LPAREN expr RPAREN # parenExpression ;
addOrSub: PLUS
        | MINUS ;
literal: STRINGCONSTANT # litString
       | INTEGERCONSTANT # litInt
       | FLOATCONSTANT # litFloat
       | CHARACTERCONSTANT # litChar
       | TRUE # litBoolean
       | FALSE # litBoolean ;
exprList: expr exprMore*
        |
        ;
exprMore: COMMA expr ;


// Lexical Elements:

// Keywords: 

INT : 'int' ;
FLOAT : 'float' ;
CHAR : 'char' ;
STRING : 'string' ;
BOOLEAN : 'boolean' ;
VOID : 'void' ;
IF : 'if' ;
ELSE : 'else' ;
WHILE : 'while' ;
PRINT : 'print' ;
PRINTLN : 'println' ;
RETURN : 'return' ;
TRUE : 'true' ;
FALSE : 'false' ;

// Punctuation Symbols:

LPAREN : '(' ;
RPAREN : ')' ;
COMMA : ',' ;
LBRACE : '{' ;
RBRACE : '}' ;
SEMICOLON : ';' ;

// Identifiers

IDENT : [_a-zA-Z][_a-zA-Z0-9]* ;

// Binary Operators

EQUIVALENT : '==' ;
LESSTHAN : '<' ;
PLUS : '+' ;
MINUS : '-' ;
MULTIPLY : '*' ;

// Constants

INTEGERCONSTANT : [0-9]+ ;
STRINGCONSTANT : '"'[a-zA-Z0-9!,.:_{} ]*'"' ;
CHARACTERCONSTANT : '\''[a-zA-Z0-9!,.:+{} ]'\'' ;
FLOATCONSTANT : [0-9]*'.'[0-9]+ ;

// Comments/Whitespace/Newlines

COMMENT : '//'~[\r\n]* -> skip ;
WS : [ \t]+ -> skip ;
NL : '\r'? '\n' -> skip ; // I think by definition of the grammar it seems like newlines are to be skipped