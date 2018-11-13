package com.p22.beast;

import java.util.List;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class P22 {

    private static final Interpreter interpreter = new Interpreter();

    public static void main(String args[]) throws IOException {

        //System.out.println("hello");
        runFile(args[0]);

    }

    private static void runFile(String path) throws IOException {

        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

    }

    private static void run(String source) {

        //System.out.println(source);

        Lexer lexer = new Lexer(source);

        Parser parser = new Parser(lexer);

        List<Stmt> statements = parser.parse();

        interpreter.interpret(statements);

    }

}