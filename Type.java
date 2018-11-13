package com.p22.beast;

public enum Type {

    //single character types
    LPAREN, RPAREN, LBRACE, RBRACE, PLUS, MINUS, MUL, DIV, SEMICOLON,

    //two+ character types
    equalEQUAL, gEQUAL, lEQUAL, BANG, bangEQUAL, LESS, GREATER, EQUAL,

    //literals
    IDENT, NUM,

    //keywords
    AND, OR, IF, ELSE, WHILE, INT, NULL, PRINT, TRUE, FALSE,

    EOF
}
