package parser;

import tokenizer.Token;
import tokenizer.TokenType;
import mainapp.Main;
import parser.Ast.*;

import java.util.ArrayList;
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
            return program();
        } catch (ParseError error) {
            return null;
        }
    }
    private Ast program() {
        List<Statement> statementList = new ArrayList<>();

        while(!isAtEnd()) {
            statementList.add(statement());
        }

        return new Ast.Program(statementList);
    }

    private Statement statement() {
        if(match(TokenType.KW_IF)) {return ifStatement();}
        if(match(TokenType.KW_WHILE)) {return whileStatement();}
        if(match(TokenType.KW_FOR)) {return forStatement();}
        if(match(TokenType.KW_BREAK)) {return breakStatement();}
        if(match(TokenType.KW_CONTINUE)) {return continueStatement();}
        if(match(TokenType.KW_PRINT)) {return printStatement();}
        if(match(TokenType.KW_SHOW)) {return showStatement();}
        if(match(TokenType.KW_SPELLDATABASE)) {return spellDatabaseStatement();}
        if(match(TokenType.KW_RETURN)) {return returnStatement();}

        if(isType()) {return variableDeclaration();}
        if(match(TokenType.KW_FUNCTION)){return functionDeclaration();}

        if(check(TokenType.IDENTIFIER)) {return identifierStatement();}

        throw error(peek(), "Expected statement.");
    }


    private Statement ifStatement() {
        consume(TokenType.LPAREN, "Expected '(' after if");
        Expression condition = (Expression) expression();
        consume(TokenType.RPAREN, "Expected ')' after if");

        Block ifBlock = block();

        List<Expression> elseifConditions = new ArrayList<>();
        List<Block> elseifBlocks = new ArrayList<>();

        while(match(TokenType.KW_ELSEIF)) {
            consume(TokenType.LPAREN, "Expected '(' after if");
            elseifConditions.add((Expression) expression());
            consume(TokenType.RPAREN, "Expected ')' after expression");
            elseifBlocks.add(block());
        }

        Block elseBlock = null;
        if (match(TokenType.KW_ELSE)) {
            elseBlock = block();
        }

        return new IfStatement(condition, elseifConditions, ifBlock, elseifBlocks, elseBlock);
    }


    private Statement whileStatement() {
        consume(TokenType.LPAREN, "Expected '(' after while");
        Expression condition = expression();
        consume(TokenType.RPAREN, "Expected ')' after condition");
        Block whileBlock = block();

        return new WhileStatement(condition, whileBlock);
    }

    private Statement forStatement() {
        consume(TokenType.LPAREN, "Expected '(' after for");
        VariableDeclaration initilize = null;
        if(isType()) {initilize = (VariableDeclaration) variableDeclaration();}
        Expression condition = (Expression) expression();
        consume(TokenType.SEMICOLON, "Expected ';' after condition variable");
        Token updateVariable = consume(TokenType.IDENTIFIER, "Expected IDENTIFIER in update variable");
        Token operator = null;
        if(match(TokenType.OP_INCREMENT, TokenType.OP_DECREMENT)){
            operator = previous();
        }
        else{
            throw error(peek(), "Expected '++' or '--' operator after Identifier");
        }
        consume(TokenType.RPAREN, "Expected ')' after updateVariable");

        Block forBlock = block();

        return new ForStatement(initilize, condition, updateVariable, operator, forBlock);
    }

    private Statement variableDeclaration() {
        Token type = advance();

        Token identifier = consume(TokenType.IDENTIFIER, "Expected an IDENTIFIER after type");

        consume(TokenType.OP_ASSIGN, "Expected assignment operator after identifier");

        if (match(TokenType.KW_POKEMON)) {
            consume(TokenType.LPAREN, "Expect '('");
            Token path = consume(TokenType.STRING_LITERAL, "Expect file path");
            consume(TokenType.RPAREN, "Expect ')'");
            consume(TokenType.SEMICOLON, "Expect ';'");

            return new PokemonLoad(path);
        }

        Expression expression = (Expression) expression();

        consume(TokenType.SEMICOLON, "Expected ';' after expression ");

        return new VariableDeclaration(type, identifier, expression);

    }

    private Statement breakStatement() {
        consume(TokenType.SEMICOLON, "Expected ';' after break keyword");
        return new BreakStatement();
    }

    private Statement continueStatement() {
        consume(TokenType.SEMICOLON, "Expected ';' after continue keyword");
        return new ContinueStatement();
    }

    private Statement printStatement() {
        Expression value = expression();
        consume(TokenType.SEMICOLON, "Expected ';' after print statement");

        return new PrintStatement(value);
    }

    private Statement showStatement() {
        Token identifier = consume(TokenType.IDENTIFIER, "Expected a pokemon Identifier");

        consume(TokenType.SEMICOLON, "Expected ';' after pokemon identifier");

        return new ShowStatement(identifier);
    }

    private Statement spellDatabaseStatement() {
        Token literal = consume(TokenType.STRING_LITERAL, "Expected String literal after spelldatabase keyword");
        consume(TokenType.SEMICOLON, "Expected ';' after String literal");

        return new SpellDatabaseStatement(literal);
    }

    private Statement returnStatement() {
        Expression value = (Expression) expression();
        consume(TokenType.SEMICOLON, "Expected ';' after return statement");
        return new ReturnStatement(value);
    }

    private Statement functionDeclaration() {
        Token returnType = advance();
        Token name = consume(TokenType.IDENTIFIER, "Expected funtion name");

        consume(TokenType.LPAREN, "Expected '(' after function name");
        List<Parameter> parameters = parameter();
        consume(TokenType.RPAREN, "Expected ')' after parameters");

        Block body = block();

        return new FunctionDeclaration(returnType, name, parameters, body);
    }

    private List<Parameter> parameter() {
        List<Parameter> parameters = new ArrayList<>();

        do{
            Token type = advance();
            Token name = consume(TokenType.IDENTIFIER, "Expected parameter name after type");

            parameters.add(new Parameter(type, name));
        }
        while (match(TokenType.COMMA));

        return parameters;
    }

    private Statement identifierStatement() {
        Token id = consume(TokenType.IDENTIFIER, "Expected IDENTIFIER statement");

        // Function Call
        if (match(TokenType.LPAREN)) {
            List<Expression> arguments = new ArrayList<>();

            if (!check(TokenType.RPAREN)) {
                do {
                    arguments.add(expression());

                } while (match(TokenType.COMMA));
            }

            consume(TokenType.RPAREN, "Expected ')' after arguments");
            consume(TokenType.SEMICOLON, "Expected ';' after function call");

            return new ExpressionStatement(new Call(id, arguments));
        }

        // DOT access assignment
        if(match(TokenType.DOT)){
            Token property = consume(TokenType.IDENTIFIER, "Expected property of pokemon");
            if (check(TokenType.OP_ASSIGN) || check(TokenType.OP_PLUS_ASSIGN) || check(TokenType.OP_MINUS_ASSIGN)) {
                return assignment(id, property);
            }
        }
        // Move Assignment
        if (match(TokenType.KW_MOVE1, TokenType.KW_MOVE2, TokenType.KW_MOVE3, TokenType.KW_MOVE4)){
            Token moveSlot = previous();
            Token target = consume(TokenType.IDENTIFIER, "Expected target pokemon after move keyword");
            consume(TokenType.SEMICOLON, "Expected ';' after target");

            return new MoveStatement(id, moveSlot, target);
        }

        //Regular Assignment
        return assignment(id, null);
    }

    private Assignment assignment(Token id, Token property){
        if(match(TokenType.OP_ASSIGN, TokenType.OP_PLUS_ASSIGN, TokenType.OP_MINUS_ASSIGN)){
            Token operator = previous();
            Expression value = expression();
            consume(TokenType.SEMICOLON, "Expected ';' after value");

            return new Assignment(id, property, operator, value);
        }
        else{
            throw error(peek(), "Expected assignment operator after name");
        }
    }

    private Expression grouping() {
        consume(TokenType.LPAREN, "Expect '(' before expression.");
        Expression expr = expression();
        consume(TokenType.RPAREN, "Expect ')' after expression.");
        return new Grouping(expr);
    }

    private Block block() {
        consume(TokenType.LBRACE, "Expected '{' at start of block");
        List<Statement> statementList = new ArrayList<>();

        while(!check(TokenType.RBRACE) && !isAtEnd()){
            statementList.add(statement());
        }
        consume(TokenType.RBRACE, "Expected '}' at start of block");

        return new Block(statementList);
    }

    private boolean isType() {
        return check(TokenType.KW_INT) ||
                check(TokenType.KW_BOOLEAN) ||
                check(TokenType.KW_STRING) ||
                check(TokenType.KW_POKEMON);
    }

    private Expression expression() {
        return or();
    }
    private Expression or() {
        Expression expr = and();

        while(match(TokenType.OP_OR)){
            Token operator = previous();
            Expression right = and();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }
    private Expression and(){
        Expression expr = equality();

        while(match(TokenType.OP_AND)){
            Token operator = previous();
            Expression right = equality();
            expr = new Binary(expr, operator, right);
        }

        return expr;
    }
    private Expression equality() {
        Expression ast = comparison();

        while (match(TokenType.OP_PLUS_ASSIGN, TokenType.OP_EQUALS)) {
            Token operator = previous();
            Expression right = comparison();
            ast = new Binary(ast, operator, right);
        }

        return ast;
    }

    private Expression comparison() {
        Expression ast = term();

        while (match(TokenType.OP_GREATER_THAN, TokenType.OP_GREATER_THAN_EQUALS, TokenType.OP_LESS_THAN, TokenType.OP_LESS_THAN_EQUALS)) {
            Token operator = previous();
            Expression right = term();
            ast = new Binary(ast, operator, right);
        }

        return ast;
    }
    private Expression term() {
        Expression ast = factor();

        while (match(TokenType.OP_MINUS, TokenType.OP_PLUS)) {
            Token operator = previous();
            Expression right = factor();
            ast = new Ast.Binary(ast, operator, right);
        }

        return ast;
    }
    private Expression factor() {
        Expression ast = unary();

        while (match(TokenType.OP_DIVIDE, TokenType.OP_MULTIPLY)) {
            Token operator = previous();
            Expression right = unary();
            ast = new Ast.Binary(ast, operator, right);
        }

        return ast;
    }
    private Expression unary() {
        if (match(TokenType.OP_NOT, TokenType.OP_PLUS, TokenType.OP_MINUS)) {
            Token operator = previous();
            Expression right = unary();
            return new Ast.Unary(operator, right);
        }

        return primary();
    }
    private Expression primary() {
        if (match(TokenType.OP_FALSE)) return new Literal(false);
        if (match(TokenType.OP_TRUE)) return new Literal(true);

        if (match(TokenType.STRING_LITERAL)) {
            return new Literal(previous().getValue());
        }

        if (match(TokenType.INT_LITERAL)) {
            int value = Integer.parseInt(previous().value);
            return new Literal(value);
        }

        if (match(TokenType.IDENTIFIER)){
            Token name = previous();
            Expression expr = new Identifier(name.getValue(), null);

            if (match(TokenType.LPAREN)) {
                List<Expression> arguments = new ArrayList<>();
                if (!check(TokenType.RPAREN)) {
                    do {
                        arguments.add(expression());
                    } while (match(TokenType.COMMA));
                }
                consume(TokenType.RPAREN, "Expected ')' after arguments");
                return new Call(name, arguments);
            }

            if (match(TokenType.DOT)){
                Token property = consume(TokenType.IDENTIFIER, "Expected property after DOT operator");
                expr = new Attribute(((Identifier)expr).name, property.getValue());
            }

            return expr;
        }

        if (match(TokenType.LPAREN)) {
            Expression ast = expression();
            consume(TokenType.RPAREN, "Expect ')' after expression.");
            return new Grouping(ast);
        }

        throw error(peek(), "Expect expression.");
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



}
