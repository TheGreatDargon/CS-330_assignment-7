package mainapp;

import parser.Ast;
import parser.AstPrinter;
import parser.Parser;
import parser.ScopeVisitor;
import tokenizer.Lexer;
import tokenizer.Token;
import tokenizer.TokenType;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.io.File;

public class Main {
    static boolean hadError = false;
    static boolean hadRuntimeError = false;

    public static void main(String[] args) {


        Scanner input = new Scanner(System.in);
        System.out.print("Please input your code file path: ");
        String codeFilePath = input.nextLine();

        File codeFile = new File(codeFilePath);
        try {
            input = new Scanner(codeFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        StringBuilder code = new StringBuilder();
        input.useDelimiter("");
        while(input.hasNext()) {
            code.append(input.next());
        }

        Lexer lexer = new Lexer(code.toString());
        List<Token> tokens = lexer.tokenize();

        //printTokens(tokens);

        Parser parser = new Parser(tokens);
        Ast ast = parser.parse();

        if(hadError) return;
        if (hadRuntimeError) System.exit(70);

        System.out.println(new AstPrinter().print(ast));

        ScopeVisitor scopeVisitor = new ScopeVisitor();

        try {
            scopeVisitor.visit(ast);
            System.out.println("Semantic analysis successful.");


        } catch (RuntimeException e) {
            System.err.println(" Scope Error: " + e.getMessage());
            hadError = true;
        }

    }

    private static void report(int line, String where, String message) {
        System.err.println("Line: " + line +  " Error" + where + ": " + message);
        hadError = true;
    }

    public static void error(Token token, String message) {
        if (token.getType() == TokenType.EOF) {
            report(token.line, " at end", message);
        } else {
            report(token.line, " at '" + token.getValue() + "'", message);
        }
    }

    private static void printTokens(List<Token> tokens) {
        for (tokenizer.Token token : tokens) {
            System.out.println(token);
        }
    }
}