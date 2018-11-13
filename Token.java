package com.p22.beast;
    
public class Token {

    private Type type;
    private Object value;

    public Token(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return this.type;
    }

    public Object getValue() {
        return this.value;
    }

    public void show() {
        System.out.println("<" + this.type + ", " + this.value + ">");
    }

}