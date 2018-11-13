package com.p22.beast;

import static com.p22.beast.Type.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    //final private List<Token> tokens;
    final private Lexer lexer;
    final private List<Stmt> statements = new ArrayList<>();
    private Token currentToken;
    //private int pos = 0;

    Parser(Lexer lexer) {
        this.lexer = lexer;
        currentToken = lexer.getNextToken();
    }

    List<Stmt> parse() {

        while(true) {

            //currentToken = lexer.getNextToken();

            if(currentToken.getType() == EOF) {
                break;   
            }

            statements.add(declaration());
            //currentToken = lexer.getNextToken();
        }

        return statements;

    }

    private Stmt declaration() {
        
        if(currentToken.getType() == INT) {
            return varDeclaration();
        }

        return statement();
    }



    private Stmt varDeclaration() {
        
        eat(INT);

        Token name = eat(IDENT);

        Expr init = null;
        if(currentToken.getType() == EQUAL) {
            eat(EQUAL);
            init = expression();
        }

        return new Stmt.Var(name, init);
    }

    private Stmt statement() {

        if(currentToken.getType() == IF) {
            eat(IF);
            return ifStatement();
        } else if(currentToken.getType() == WHILE) {
            eat(WHILE);
            return whileStatement();
        } else if(currentToken.getType() == PRINT) {
            eat(PRINT);
            return printStatement();
        } else if(currentToken.getType() == LBRACE) {
            return new Stmt.Block(block());
        } else {
            return expressionStatement();
        }

    }

    private Stmt ifStatement() {

        eat(LPAREN);
        Expr condition = expression();
        eat(RPAREN);

        Stmt thenBranch = statement();

        Stmt elseBranch = null;
        if(currentToken.getType() == ELSE) {
            eat(ELSE);
            elseBranch = statement();
        }

        return new Stmt.If(condition, thenBranch, elseBranch);
    }

    private Stmt whileStatement() {

        eat(LPAREN);
        Expr expression = expression();
        eat(RPAREN);

        Stmt body = statement();

        return new Stmt.While(expression, body);

    }

    private Stmt printStatement() {

        return new Stmt.Print(expression());

    }

    private List<Stmt> block() {

        List<Stmt> statements = new ArrayList<>();

        eat(LBRACE);

        while(currentToken.getType() != RBRACE) {
            statements.add(declaration());
        }

        eat(RBRACE);

        return statements;

    }

    private Stmt expressionStatement() {
        
        return new Stmt.Expression(expression());

    }

    private Expr expression() {
        return assignment();
    }

    private Expr assignment() {

        Expr expr = or();

        if(currentToken.getType() == EQUAL) {
            eat(EQUAL);
            Expr value = assignment();

            if(expr instanceof Expr.Variable) {
                Token token = ((Expr.Variable)expr).name;
                return new Expr.Assign(token, value);
            }
        }

        return expr;

    }

    private Expr or() {

        Expr expr = and();

        while(currentToken.getType() == OR) {
            eat(OR);
            Expr right = and();
            expr = new Expr.Logical(expr, OR, right);
        }

        return expr;

    }

    private Expr and() {

        Expr expr = equality();

        while(currentToken.getType() == AND) {
            eat(AND);
            Expr right = equality();
            expr = new Expr.Logical(expr, AND, right);
        }

        return expr;
    }

    private Expr equality() {

        Expr expr = comparison();
        Type op;

        while(currentToken.getType() == bangEQUAL || currentToken.getType() == equalEQUAL) {
            op = currentToken.getType();
            eat(op);
            Expr right = comparison();
            expr = new Expr.Binary(expr, op, right);
        }

        return expr;

    }

    private Expr comparison() {

        Expr expr = plusminus();
        Type op;

        while(currentToken.getType() == GREATER || currentToken.getType() == gEQUAL
            || currentToken.getType() == LESS || currentToken.getType() == lEQUAL) {

                op = currentToken.getType();
                eat(op);
                Expr right = plusminus();
                expr = new Expr.Binary(expr, op, right);

            }

            return expr;

    }

    private Expr plusminus() {

        Expr expr = muldiv();

        while(currentToken.getType() == PLUS || currentToken.getType() == MINUS) {

            Type op = currentToken.getType();
            eat(op);
            Expr right = muldiv();
            expr = new Expr.Binary(expr, op, right);

        }
        return expr;
    }

    private Expr muldiv() {

        Expr expr = unary();

        while(currentToken.getType() == MUL || currentToken.getType() == DIV) {

            Type op = currentToken.getType();
            eat(op);
            Expr right = unary();
            expr = new Expr.Binary(expr, op, right);
        }

        return expr;

    }

    private Expr unary() {
        
        if(currentToken.getType() == BANG || currentToken.getType() == MINUS) {
            Type op = currentToken.getType();
            eat(op);
            Expr right = unary();
            return new Expr.Unary(op, right);
        }

        return primary();
    }

    private Expr primary() {

        if(currentToken.getType() == FALSE) {eat(FALSE); return new Expr.Literal(false);}
        if(currentToken.getType() == TRUE) {eat(TRUE); return new Expr.Literal(true);}
        if(currentToken.getType() == NULL) {eat(NULL); return new Expr.Literal(null);}

        if(currentToken.getType() == NUM) {
            Token token = currentToken;
            eat(NUM);
            return new Expr.Literal(token.getValue());
        }
        
        if(currentToken.getType() == IDENT) {
            Token token = currentToken;
            eat(IDENT);
            return new Expr.Variable(token);
        }

        if(currentToken.getType() == LPAREN) {
            eat(LPAREN);
            Expr expr = expression();
            eat(RPAREN);
            return new Expr.Grouping(expr);
        }

        return null;

    }

    private Token eat(Type type) {

        Token token = currentToken;

        if(type == currentToken.getType()) {
            currentToken = lexer.getNextToken();
            return token;
        }

        return null;

    }

}