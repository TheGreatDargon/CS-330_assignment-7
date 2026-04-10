package parser;

import tokenizer.Token;
import tokenizer.TokenType;
import mainapp.Main;


import java.util.List;

public class Parser {
    private static class ParseError extends RuntimeException {}
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }
    public Ast parse() {
        try {
            return expression();
        } catch (ParseError error) {
            return null;
        }
    }

    private Ast expression() {
        return equality();
    }
    private Ast equality() {
        Ast ast = comparison();

        while (match(TokenType.OP_PLUS_ASSIGN, TokenType.OP_EQUALS)) {
            Token operator = previous();
            Ast right = comparison();
            ast = new Ast.Binary(ast, operator, right);
        }

        return ast;
    }
    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();

        throw error(peek(), message);
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }
    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }
    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private Token peek() {
        if (current >= tokens.size()) return tokens.get(tokens.size() - 1);
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private ParseError error(Token token, String message) {
        Main.error(token, message);
        return new ParseError();
    }
    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().getType() == TokenType.SEMICOLON) return;

            switch (peek().getType()) {
                case TokenType.IDENTIFIER:
                case TokenType.KW_FOR:
                case TokenType.KW_IF:
                case TokenType.KW_WHILE:
                case TokenType.KW_PRINT:
                case TokenType.KW_RETURN:
                    return;
            }

            advance();
        }
    }
    private Ast comparison() {
        Ast ast = term();

        while (match(TokenType.OP_GREATER_THAN, TokenType.OP_GREATER_THAN_EQUALS, TokenType.OP_LESS_THAN, TokenType.OP_LESS_THAN_EQUALS)) {
            Token operator = previous();
            Ast right = term();
            ast = new Ast.Binary(ast, operator, right);
        }

        return ast;
    }
    private Ast term() {
        Ast ast = factor();

        while (match(TokenType.OP_MINUS, TokenType.OP_PLUS)) {
            Token operator = previous();
            Ast right = factor();
            ast = new Ast.Binary(ast, operator, right);
        }

        return ast;
    }
    private Ast factor() {
        Ast ast = unary();

        while (match(TokenType.OP_DIVIDE, TokenType.OP_MULTIPLY)) {
            Token operator = previous();
            Ast right = unary();
            ast = new Ast.Binary(ast, operator, right);
        }

        return ast;
    }
    private Ast unary() {
        if (match(TokenType.OP_PLUS, TokenType.OP_MINUS)) {
            Token operator = previous();
            Ast right = unary();
            return new Ast.Unary(operator, right);
        }

        return primary();
    }
    private Ast primary() {
        if (match(TokenType.OP_FALSE)) return new Ast.Literal(false);
        if (match(TokenType.OP_TRUE)) return new Ast.Literal(true);

        if (match(TokenType.INT_LITERAL)) {
            int value = Integer.parseInt(previous().value);
            return new Ast.Literal(value);
        }

        if (match(TokenType.LPAREN)) {
            Ast ast = expression();
            consume(TokenType.RPAREN, "Expect ')' after expression.");
            return new Ast.Grouping(ast);
        }

        throw error(peek(), "Expect expression.");
    }


}
