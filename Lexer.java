package com.p22.beast;

import static com.p22.beast.Type.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Lexer {

    private int pos = 0;
    private String source;
    private static final Map<String, Type> keywords;
    private static final Map<String, Type> operators;

    static {

        keywords = new HashMap<>();

        keywords.put("and", AND);
        keywords.put("or", OR);
        keywords.put("if", IF);
        keywords.put("else", ELSE);
        keywords.put("while", WHILE);
        keywords.put("int", INT);
        keywords.put("print", PRINT);
        keywords.put("null", NULL);
        keywords.put("true", TRUE);
        keywords.put("false", FALSE);

        operators = new HashMap<>();

        operators.put("(", LPAREN);
        operators.put(")", RPAREN);
        operators.put("{", LBRACE);
        operators.put("}", RBRACE);
        operators.put("+", PLUS);
        operators.put("-", MINUS);
        operators.put("*", MUL);
        operators.put("/", DIV);
        operators.put("!", BANG);
        operators.put("!=", bangEQUAL);
        operators.put("=", EQUAL);
        operators.put("==", equalEQUAL);
        operators.put("<", LESS);
        operators.put("<=", lEQUAL);
        operators.put(">", GREATER);
        operators.put(">=", gEQUAL);

    }

    Lexer(String source) {
        this.source = source;
    }

    Token getNextToken() {

        while(pos < source.length()) {

            char c = source.charAt(pos);

            if(Character.isWhitespace(c)) {
                pos++;
                continue;
            }

            switch(c) {

                //single character types
                case '(' : pos++; return sendToken(Character.toString(c)); 
                case ')' : pos++; return sendToken(Character.toString(c));
                case '{' : pos++; return sendToken(Character.toString(c));
                case '}' : pos++; return sendToken(Character.toString(c));
                case '+' : pos++; return sendToken(Character.toString(c)); 
                case '-' : pos++; return sendToken(Character.toString(c)); 
                case '*' : pos++; return sendToken(Character.toString(c)); 
                case '/' : pos++; return sendToken(Character.toString(c)); 
                case ';' : pos++; return sendToken(Character.toString(c));

                //two+ character types
                case '!' : return sendToken(match('=') ? "!=" : "!"); 
                case '=' : return sendToken(match('=') ? "==" : "="); 
                case '<' : return sendToken(match('=') ? "<=" : "<"); 
                case '>' : return sendToken(match('=') ? ">=" : ">"); 

                //number and identifiers
                default: 

                    if(Character.isDigit(c)) {
                        return number();
                    } else {
                        return identifier();
                    }

            }

        }

        return new Token(EOF, "EOF");
        
    }

    private Token number() {

        String integer = new String();

        while(pos < source.length() && Character.isDigit(source.charAt(pos))) {
            integer += source.charAt(pos);
            pos++;
        }

        return sendToken(NUM, Integer.parseInt(integer));

    }

    private Token identifier() {


        String word = new String();

        while(pos < source.length() && Character.isLetter(source.charAt(pos))) {
            word += source.charAt(pos);
            pos++;
        }

        Type type = keywords.get(word);
        if(type == null) 
            type = IDENT;
            
        return sendToken(type, word);
    }

    private Token sendToken(String key) {
        //tokens.add(new Token(type, source.substring(start, pos)));
        return new Token(operators.get(key), key);
    }

    private Token sendToken(Type type, Object value) {

        //tokens.add(new Token(type, value));
        return new Token(type, value);

    }

    private boolean match(char c) {
        
        if(source.charAt(pos + 1) == c) {
            pos = pos + 2;
            return true;
        }
        pos++;
        return false;
    }

}