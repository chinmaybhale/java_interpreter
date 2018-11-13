package com.p22.beast;

import java.util.List;

abstract class Expr {

    abstract <T> T accept(Visitor<T> visitor);

    interface Visitor<T> {

        T visitAssignExpr(Assign expr);
        T visitBinaryExpr(Binary expr);
        T visitGroupingExpr(Grouping expr);
        T visitLiteralExpr(Literal expr);
        T visitLogicalExpr(Logical expr);
        T visitUnaryExpr(Unary expr);
        T visitVariableExpr(Variable expr);
    }

    static class Assign extends Expr {

        final Token token;
        final Expr value;

        Assign(Token token, Expr value) {
            this.token = token;
            this.value = value;
        }

        @Override
        <T> T accept(Visitor<T> visitor) {
            return visitor.visitAssignExpr(this);
        }

    }

    static class Binary extends Expr {

        final Expr left;
        final Type op;
        final Expr right;

        Binary(Expr left, Type op, Expr right) {

            this.left = left;
            this.op = op;
            this.right = right;

        }

        @Override
        <T> T accept(Visitor<T> visitor) {
            return visitor.visitBinaryExpr(this);
        }
    }

    static class Grouping extends Expr {
        
        final Expr expressions;

        Grouping(Expr expressions) {
            this.expressions = expressions;
        }

        @Override
        <T> T accept(Visitor<T> visitor) {
            return visitor.visitGroupingExpr(this);
        }

    }

    static class Literal extends Expr {

        final Object value;

        Literal(Object value) {
            this.value = value;
        }

        @Override
        <T> T accept(Visitor<T> visitor) {
            return visitor.visitLiteralExpr(this);
        }

    }

    static class Logical extends Expr {

        final Expr left;
        final Type op;
        final Expr right;

        Logical(Expr left, Type op, Expr right) {
            
            this.left = left;
            this.op = op;
            this.right = right;
        }

        @Override
        <T> T accept(Visitor<T> visitor) {
            return visitor.visitLogicalExpr(this);
        }
    }

    static class Unary extends Expr {

        final Type op;
        final Expr expr;

        Unary(Type op, Expr expr) {
            this.op = op;
            this.expr = expr;
        }

        @Override
        <T> T accept(Visitor<T> visitor) {
            return visitor.visitUnaryExpr(this);
        }

    }

    static class Variable extends Expr {

        final Token name;

        Variable(Token name) {

            this.name = name;
        }

        @Override
        <T> T accept(Visitor<T> visitor) {
            return visitor.visitVariableExpr(this);
        }
    }
}