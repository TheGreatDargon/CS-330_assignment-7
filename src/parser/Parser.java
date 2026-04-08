package parser;

import tokenizer.Token;
import tokenizer.TokenType;
import mainapp.Main;


import java.util.List;

import tokenizer.TokenType;

public class Parser {
    private static class ParseError extends RuntimeException {}
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }
    public Expr parse() {
        try {
            return expression();
        } catch (ParseError error) {
            return null;
        }
    }

    private Expr expression() {
        return equality();
    }
    private Expr equality() {
        Expr expr = comparison();

        while (match(TokenType.OP_PLUS_ASSIGN, TokenType.OP_EQUALS)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
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
    private Expr comparison() {
        Expr expr = term();

        while (match(TokenType.OP_GREATER_THAN, TokenType.OP_GREATER_THAN_EQUALS, TokenType.OP_LESS_THAN, TokenType.OP_LESS_THAN_EQUALS)) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }
    private Expr term() {
        Expr expr = factor();

        while (match(TokenType.OP_MINUS, TokenType.OP_PLUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }
    private Expr factor() {
        Expr expr = unary();

        while (match(TokenType.OP_DIVIDE, TokenType.OP_MULTIPLY)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }
    private Expr unary() {
        if (match(TokenType.OP_PLUS, TokenType.OP_MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }

        return primary();
    }
    private Expr primary() {
        if (match(TokenType.OP_FALSE)) return new Expr.Literal(false);
        if (match(TokenType.OP_TRUE)) return new Expr.Literal(true);

        if (match(TokenType.INT_LITERAL)) {
            int value = Integer.parseInt(previous().value);
            return new Expr.Literal(value);
        }

        if (match(TokenType.LPAREN)) {
            Expr expr = expression();
            consume(TokenType.RPAREN, "Expect ')' after expression.");
            return new Expr.Grouping(expr);
        }

        throw error(peek(), "Expect expression.");
    }


}
