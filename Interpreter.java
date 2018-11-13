package com.p22.beast;

import java.util.List;
import java.util.ArrayList;

public class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {

    private Environment environment = new Environment();


    void interpret(List<Stmt> statements) {

        for(Stmt statement : statements) {
            execute(statement);
        }
    }

    private void execute(Stmt statement) {

        statement.accept(this);
    }

    private Object evaluate(Expr expr) {

        return expr.accept(this);

    }

    @Override
    public Void visitBlockStmt(Stmt.Block stmt) {
        executeBlock(stmt.statements);
        return null;
    }

    void executeBlock(List<Stmt> statements) {

        for(Stmt statement : statements) {
            execute(statement);
        }

    }
    
    @Override
    public Void visitExpressionStmt(Stmt.Expression stmt) {
        evaluate(stmt.expression);
        return null;
    }

    @Override
    public Void visitIfStmt(Stmt.If stmt) {

        if(isTrue(evaluate(stmt.condition))) {
            execute(stmt.thenBranch);
        } else if(stmt.elseBranch != null) {
            execute(stmt.elseBranch);
        }
        return null;
    }

    @Override
    public Void visitPrintStmt(Stmt.Print stmt) {

      Object value = evaluate(stmt.expression);

      System.out.println(stringify(value));

      return null;
      
    }

    @Override
    public Void visitVarStmt(Stmt.Var stmt) {

        Object value = null;
        if(stmt.init != null) {
            value = evaluate(stmt.init);
        }

        environment.define(stmt.name, value);
        return null;

    }

    @Override
    public Void visitWhileStmt(Stmt.While stmt) {

        while(isTrue(evaluate(stmt.expression))) {
            execute(stmt.body);
        }
        return null;

    }

    @Override
    public Object visitAssignExpr(Expr.Assign expr) {

        Object value = evaluate(expr.value);

        environment.assign(expr.token, value);

        return value;
    }

    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {

        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch(expr.op) {

            //logical operators
            case bangEQUAL: return !isEqual(left,right);
            case equalEQUAL: return isEqual(left, right);
            case gEQUAL: return (int)left >= (int)right;
            case lEQUAL: return (int)left <= (int)right;
            case GREATER: return (int)left > (int)right;
            case LESS: return (int)left < (int)right;

            //arithmatic operators
            case PLUS: return (int)left + (int)right;
            case MINUS: return (int)left - (int)right;
            case DIV: return (int)left / (int)right;
            case MUL: return (int)left * (int)right;

        }

        return null;

    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {

        return evaluate(expr.expressions);
        
    }

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {

        return expr.value;

    }

    @Override
    public Object visitLogicalExpr(Expr.Logical expr) {

        Object left = evaluate(expr.left);

        if(expr.op == Type.OR) {
            if(isTrue(left)) return left;
        } else {
            if(!isTrue(left)) return left;
        }

        return evaluate(expr.right);

    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {

        Object right = evaluate(expr.expr);

        switch(expr.op) {

            case BANG: return !isTrue(right);
            case MINUS: return -(int)right;
        }

        return null;

    }

    @Override
    public Object visitVariableExpr(Expr.Variable expr) {

        return environment.get(expr.name);

    }

    private boolean isTrue(Object a) {

        if(a == null) return false;
        if(a instanceof Boolean) return (boolean)a;

        return true;

    }

    private boolean isEqual(Object a, Object b) {

        if(a == null && b ==  null) return true;
        if(a == null) return false;

        return a.equals(b);

    }

    private String stringify(Object obj) {

        if(obj == null) return null;

        return obj.toString();

    }

}