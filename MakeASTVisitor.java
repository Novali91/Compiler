import java.util.List;
import java.util.ArrayList;
import nodes.*;

// Questions for in-person testing: Do I need EmptyStmtNode and ExprStmtNode, do I need IdentNode?
// More: Do I need BlockNode? I use it for scope for PrettyPrinter, but it could be done w/o that

public class MakeASTVisitor extends CeelishBaseVisitor<ASTNode> {

    ASTNode root = null;

    public ASTNode visitProgram(CeelishParser.ProgramContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();

        ArrayList<ASTNode> funcs = new ArrayList<ASTNode>();

        for (CeelishParser.FuncContext func : ctx.func()) {
            ASTNode newFunc = visit(func);
            funcs.add(newFunc);
        }

        ProgramNode newNode = new ProgramNode(line, column, "program", funcs);
        root = newNode;
        return root;
    }

    public ASTNode visitFunc(CeelishParser.FuncContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();

        CeelishParser.FuncDeclContext funcDecl = ctx.funcDecl();

        ASTNode returnType = visit(funcDecl.compoundType());
        String ident = funcDecl.IDENT().getText();
        IdentNode identNode = new IdentNode(line, column, ident, ident);

        ArrayList<ASTNode> params = formalParamsHelper(funcDecl.formalParams());

        ASTNode funcBlock = visit(ctx.funcBlock());

        FuncNode newFunc = new FuncNode(line, column, "func", returnType, identNode, params, funcBlock);
        return newFunc;
    }

    public ASTNode visitBasicType(CeelishParser.BasicTypeContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        
        TypeNode newNode = new TypeNode(line, column, "type", ctx.type().getText());
        return newNode;
    }

    public ASTNode visitArrayType(CeelishParser.ArrayTypeContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();

        ArrayTypeNode newNode = new ArrayTypeNode(line, column, "type", ctx.type().getText(), Integer.parseInt(ctx.INTEGERCONSTANT().getText()));
        return newNode;
    }

    public ArrayList<ASTNode> formalParamsHelper(CeelishParser.FormalParamsContext ctx) {
        ArrayList<ASTNode> params = new ArrayList<ASTNode>();

        if (ctx.compoundType() == null) {
            return params;
        }

        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();

        String ident = ctx.IDENT().getText();
        IdentNode identNode = new IdentNode(line, column, ident, ident);
        ASTNode firstParam = new ParamNode(line, column, "param", identNode, visit(ctx.compoundType()));
        params.add(firstParam);
        
        for (CeelishParser.MoreFormalsContext moreParam : ctx.moreFormals()) {
            ASTNode newParam = visit(moreParam);
            params.add(newParam);
        }

        return params;
    }

    public ASTNode visitMoreFormals(CeelishParser.MoreFormalsContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();

        String ident = ctx.IDENT().getText();
        IdentNode identNode = new IdentNode(line, column, ident, ident);

        ASTNode param = new ParamNode(line, column, "param", identNode, visit(ctx.compoundType()));
        return param;
    }

    public ASTNode visitFuncBlock(CeelishParser.FuncBlockContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();

        ArrayList<ASTNode> varDecls = new ArrayList<ASTNode>();
        ArrayList<ASTNode> stmts = new ArrayList<ASTNode>();

        for (CeelishParser.VarDeclContext varDecl : ctx.varDecl()) {
            ASTNode newDecl = visit(varDecl);
            varDecls.add(newDecl);
        }

        for (CeelishParser.StatementContext stmt : ctx.statement()) {
            ASTNode newStmt = visit(stmt);
            stmts.add(newStmt);
        }

        FuncBlockNode newBlock = new FuncBlockNode(line, column, "func block", varDecls, stmts);
        return newBlock;
    }

    public ASTNode visitVarDecl(CeelishParser.VarDeclContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();

        ASTNode type = visit(ctx.compoundType());
        String ident = ctx.IDENT().getText();
        IdentNode identNode = new IdentNode(line, column, ident, ident);

        DeclNode newDecl = new DeclNode(line, column, "var declaration", identNode, type);
        return newDecl;
    }
    
    public ASTNode visitEmptyStatement(CeelishParser.EmptyStatementContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        ASTNode emptyStmt = new EmptyStmtNode(line, column, "empty stmt");
        return emptyStmt;
    }

    public ASTNode visitExprStatement(CeelishParser.ExprStatementContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        ASTNode newExpr = visit(ctx.expr());
        ASTNode exprStmt = new ExprStmtNode(line, column, "expr stmt", newExpr);
        return exprStmt;
    }

    public ASTNode visitIfStatement(CeelishParser.IfStatementContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        ASTNode newExpr = visit(ctx.expr());
        ASTNode newBlock = visit(ctx.block());
        ASTNode ifStmt = new IfStmtNode(line, column, "if", newExpr, newBlock);
        return ifStmt;
    }

    public ASTNode visitIfElseStatement(CeelishParser.IfElseStatementContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        ASTNode newExpr = visit(ctx.expr());
        ASTNode newBlockOne = visit(ctx.block(0));
        ASTNode newBlockTwo = visit(ctx.block(1));
        ASTNode ifElseStmt = new IfElseStmtNode(line, column, "if", newExpr, newBlockOne, newBlockTwo);
        return ifElseStmt;
    }

    public ASTNode visitWhileStatement(CeelishParser.WhileStatementContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        ASTNode newExpr = visit(ctx.expr());
        ASTNode newBlock = visit(ctx.block());
        ASTNode whileStmt = new WhileStmtNode(line, column, "if", newExpr, newBlock);
        return whileStmt;
    }

    public ASTNode visitPrintStatement(CeelishParser.PrintStatementContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        ASTNode newExpr = visit(ctx.expr());
        ASTNode printStmt = new PrintStmtNode(line, column, "print", newExpr, false);
        return printStmt;
    }

    public ASTNode visitPrintlnStatement(CeelishParser.PrintlnStatementContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        ASTNode newExpr = visit(ctx.expr());
        ASTNode printStmt = new PrintStmtNode(line, column, "println", newExpr, true);
        return printStmt;
    }

    public ASTNode visitReturnStatement(CeelishParser.ReturnStatementContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        if (ctx.expr() != null) {
            ASTNode newExpr = visit(ctx.expr());
            ASTNode returnStmt = new ReturnStmtNode(line, column, "return", newExpr);
            return returnStmt;
        } else {
            ASTNode returnStmt = new ReturnStmtNode(line, column, "return", null);
            return returnStmt;
        }
    }

    public ASTNode visitAssignmentStatement(CeelishParser.AssignmentStatementContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        String ident = ctx.IDENT().getText();
        IdentNode identNode = new IdentNode(line, column, ident, ident);
        ASTNode newExpr = visit(ctx.expr());
        ASTNode assignmentStmt = new AssignmentStmtNode(line, column, "=", identNode, newExpr);
        return assignmentStmt;

    }

    public ASTNode visitArrayAssignmentStatement(CeelishParser.ArrayAssignmentStatementContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        String ident = ctx.IDENT().getText();
        IdentNode identNode = new IdentNode(line, column, ident, ident);
        ASTNode index = visit(ctx.expr(0));
        ASTNode newExpr = visit(ctx.expr(1));
        ASTNode assignmentStmt = new ArrayAssignmentStmtNode(line, column, "=", identNode, newExpr, index);
        return assignmentStmt;
    }

    public ASTNode visitBlock(CeelishParser.BlockContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();

        ArrayList<ASTNode> stmts = new ArrayList<ASTNode>();

        for (CeelishParser.StatementContext stmt : ctx.statement()) {
            ASTNode newStmt = visit(stmt);
            stmts.add(newStmt);
        }

        BlockNode newBlock = new BlockNode(line, column, "block", stmts);
        return newBlock;

    }

    public ASTNode visitMultiplyExpression(CeelishParser.MultiplyExpressionContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        ASTNode left = visit(ctx.expr(0));
        ASTNode right = visit(ctx.expr(1));
        OperatorNode newNode = new OperatorNode(line, column, "*", "*", left, right);
        return newNode;
    }

    public ASTNode visitAddOrSubExpression(CeelishParser.AddOrSubExpressionContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        ASTNode left = visit(ctx.expr(0));
        ASTNode right = visit(ctx.expr(1));
        String operator = ctx.addOrSub().getText();
        OperatorNode newNode = new OperatorNode(line, column, operator, operator, left, right);
        return newNode;
    }

    public ASTNode visitLessThanExpression(CeelishParser.LessThanExpressionContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        ASTNode left = visit(ctx.expr(0));
        ASTNode right = visit(ctx.expr(1));
        OperatorNode newNode = new OperatorNode(line, column, "<", "<", left, right);
        return newNode;
    }

    public ASTNode visitEquivalentExpression(CeelishParser.EquivalentExpressionContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        ASTNode left = visit(ctx.expr(0));
        ASTNode right = visit(ctx.expr(1));
        OperatorNode newNode = new OperatorNode(line, column, "==", "==", left, right);
        return newNode;
    }

    public ASTNode visitArrayAccessExpression(CeelishParser.ArrayAccessExpressionContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();

        String ident = ctx.IDENT().getText();
        IdentNode identNode = new IdentNode(line, column, ident, ident);
        ASTNode index = visit(ctx.expr());

        ArrayAccessNode newNode = new ArrayAccessNode(line, column, "array access", identNode, index);
        return newNode;
    }

    public ASTNode visitFuncCallExpression(CeelishParser.FuncCallExpressionContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();

        String ident = ctx.IDENT().getText();
        IdentNode identNode = new IdentNode(line, column, ident, ident);

        ArrayList<ASTNode> exprs = exprListHelper(ctx.exprList());

        FuncCallNode newNode = new FuncCallNode(line, column, "call", identNode, exprs);
        return newNode;
    }

    public ASTNode visitIdentExpression(CeelishParser.IdentExpressionContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        String ident = ctx.IDENT().getText();
        IdentNode identNode = new IdentNode(line, column, ident, ident);
        return identNode;
    }

    public ASTNode visitLiteralExpression(CeelishParser.LiteralExpressionContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        ASTNode litNode = visit(ctx.literal());
        return litNode;
    }

    public ASTNode visitParenExpression(CeelishParser.ParenExpressionContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        ASTNode exprNode = visit(ctx.expr());
        return exprNode;
    }

    public ArrayList<ASTNode> exprListHelper(CeelishParser.ExprListContext ctx) {
        ArrayList<ASTNode> exprList = new ArrayList<ASTNode>();

        if (ctx.expr() == null) {
            return exprList;
        }

        ASTNode firstExpr = visit(ctx.expr());
        exprList.add(firstExpr);

        List<CeelishParser.ExprMoreContext> exprMore = ctx.exprMore();
        
        if (exprMore == null) {
            return exprList;
        }
        for (CeelishParser.ExprMoreContext moreExpr : exprMore) {
            ASTNode newExpr = visit(moreExpr.expr());
            exprList.add(newExpr);
        }

        return exprList;
    }

    public ASTNode visitLitString(CeelishParser.LitStringContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        String value = ctx.STRINGCONSTANT().getText();

        LitStringNode newNode = new LitStringNode(line, column, value, value);
        return newNode;
    }

    public ASTNode visitLitInt(CeelishParser.LitIntContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        String value = ctx.INTEGERCONSTANT().getText();

        LitIntNode newNode = new LitIntNode(line, column, value, Integer.parseInt(value));
        return newNode;
    }

    public ASTNode visitLitFloat(CeelishParser.LitFloatContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        String value = ctx.FLOATCONSTANT().getText();

        LitFloatNode newNode = new LitFloatNode(line, column, value, Float.parseFloat(value));
        return newNode;
    }

    public ASTNode visitLitChar(CeelishParser.LitCharContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        String value = ctx.CHARACTERCONSTANT().getText();

        LitCharNode newNode = new LitCharNode(line, column, value, value.charAt(1));
        return newNode;
    }

    public ASTNode visitLitBoolean(CeelishParser.LitBooleanContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        String value = ctx.getText();

        LitBooleanNode newNode = new LitBooleanNode(line, column, value, Boolean.parseBoolean(value));
        return newNode;
    }

}
