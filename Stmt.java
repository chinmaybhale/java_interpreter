package com.p22.beast;

import java.util.List;

abstract class Stmt {

    abstract <T> T accept(Visitor<T> visitor);

    interface Visitor<T> {

        T visitBlockStmt(Block stmt);
        T visitExpressionStmt(Expression stmt);
        T visitIfStmt(If stmt);
        T visitPrintStmt(Print stmt);
        T visitVarStmt(Var stmt);
        T visitWhileStmt(While stmt);
    }

    static class Block extends Stmt {

        final List<Stmt> statements;

        Block(List<Stmt> statements) {
            this.statements = statements;
        }

        @Override
        <T> T accept(Visitor<T> visitor) {
            return visitor.visitBlockStmt(this);
        }

    }

    static class Expression extends Stmt {
        
        final Expr expression;

        Expression(Expr expression) {
            this.expression = expression;
        }

        @Override
        <T> T accept(Visitor<T> visitor) {
            return visitor.visitExpressionStmt(this);
        }
    }

    static class If extends Stmt {

        final Expr condition;
        final Stmt thenBranch;
        final Stmt elseBranch;

        If(Expr condition, Stmt thenBranch, Stmt elseBranch) {
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        @Override
        <T> T accept(Visitor<T> visitor) {
            return visitor.visitIfStmt(this);
        }

    }

    static class Print extends Stmt {

        final Expr expression;

        Print(Expr expression) {
            this.expression = expression;
        }

        @Override
        <T> T accept(Visitor<T> visitor) {
            return visitor.visitPrintStmt(this);
        }
    }

    static class Var extends Stmt {

        final Token name;
        final Expr init;

        Var(Token name, Expr init) {
            this.name = name;
            this.init = init;
        }

        @Override
        <T> T accept(Visitor<T> visitor) {
            return visitor.visitVarStmt(this);
        }

    }

    static class While extends Stmt {

        final Expr expression;
        final Stmt body;

        While(Expr expression, Stmt body) {
            this.expression = expression;
            this.body = body;
        }

        @Override
        <T> T accept(Visitor<T> visitor) {
            return visitor.visitWhileStmt(this);
        }

    }
}