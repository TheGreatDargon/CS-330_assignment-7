package tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private String input;
    private int currentPosition;
    private int number;
    private String value = "";
    private Object literal = null;
    private TokenType type;
    private int start;
    private int end;

    public Lexer(String input) {
        this.input = input;
        this.currentPosition = 0;
        this.number = 0;
    }

    public List<Token> tokenize(){
        List<Token> tokens = new ArrayList<>();

        while(currentPosition < input.length()){
            char currentChar = input.charAt(currentPosition);

            if (Character.isWhitespace(currentChar)){
                currentPosition++;
                continue;
            }

            Token token = nextToken();
            if (token != null) {
                tokens.add(token);
            }
            else {
                throw new RuntimeException("Unknown character: " + currentChar);
            }
        }

        return tokens;
    }

    private Token nextToken() {

        if (currentPosition >= input.length()) {
            return new Token(TokenType.EOF, "EOF", null, ++number);
        }

        String[] tokenPatterns = {
                // Add the regex rules from token table here
                "Pokemon", // Pokemon Keyword
                "int", // int keyword
                "String", // String keyword
                "boolean", // boolean keyword
                "function",
                "void",
                "elseif",
                "else",
                "show",
                "spelldatabase",
                "move1",
                "move2",
                "move3",
                "move4",
                "continue",
                "break",
                "return",
                "print",
                "while",
                "if",
                "for",
                "true",
                "false",
                "[a-zA-Z_]([a-zA-Z_]|[0-9])*", // Identifiers
                "0|[1-9][0-9]*", // Int Literal
                "\"[^\"]*\"", // String Literals
                "=", // Assignment operator
                "==", // Compare Operator
                "<",  // Less than
                ">",  // Greater than
                "<=", // Less than or equal
                ">=", // Greater than or equal
                "\\+=", // Add-and-assign operator
                "-=", // Subtract-and-assign operator
                "\\+", // Addition operator
                "-", // Subtraction operator
                "\\*", // Multiply operator
                "/", // Divide operator
                "&&", // Logical And operator
                "\\|\\|", // Logical Or operator
                "!", // Logical Not operator
                "!=", // Compare operator
                "\\(", // Left Parenthesis
                "\\)", // Right Parenthesis
                "\\{", // Left Bracket
                "\\}", // Right Bracket
                ";", // Statement Terminator
                ",", // Parameter separator
                "\\.", // Field access operator
                "[ \n\r\t]|//[^\\n]*\n?", // Whitespace
                "\\z" // EOF
        };

        TokenType[] tokenTypes = {
                TokenType.KW_POKEMON, TokenType.KW_INT, TokenType.KW_STRING, TokenType.KW_BOOLEAN,
                TokenType.KW_FOR, TokenType.KW_IF, TokenType.KW_WHILE, TokenType.KW_PRINT, TokenType.KW_RETURN,
                TokenType.KW_FUNCTION, TokenType.KW_VOID, TokenType.KW_ELSEIF, TokenType.KW_ELSE, TokenType.KW_SHOW, TokenType.KW_SPELLDATABASE,
                TokenType.KW_MOVE1, TokenType.KW_MOVE2, TokenType.KW_MOVE3, TokenType.KW_MOVE4,
                TokenType.KW_CONTINUE, TokenType.KW_BREAK,
                TokenType.KEYWORD, TokenType.IDENTIFIER, TokenType.OP_TRUE, TokenType.OP_FALSE,
                TokenType.INT_LITERAL, TokenType.STRING_LITERAL,
                TokenType.OP_ASSIGN, TokenType.OP_EQUALS, TokenType.OP_LESS_THAN, TokenType.OP_GREATER_THAN, TokenType.OP_LESS_THAN_EQUALS, TokenType.OP_GREATER_THAN_EQUALS,
                TokenType.OP_PLUS_ASSIGN, TokenType.OP_MINUS_ASSIGN, TokenType.OP_PLUS, TokenType.OP_MINUS, TokenType.OP_MULTIPLY, TokenType.OP_DIVIDE, TokenType.OP_AND, TokenType.OP_OR, TokenType.OP_NOT, TokenType.OP_NOTEQUAL,
                TokenType.LPAREN, TokenType.RPAREN, TokenType.LBRACE, TokenType.RBRACE,
                TokenType.SEMICOLON, TokenType.COMMA, TokenType.DOT, TokenType.WHITESPACE, TokenType.EOF
        };

        do {
            value = "";
            for (int i = 0; i < tokenPatterns.length; i++) {  // Flipped taking length of longest token and trying to match it to current position
                Pattern pattern = Pattern.compile("^" + tokenPatterns[i]);
                Matcher matcher = pattern.matcher(input);

                matcher.region(currentPosition, input.length());

                if (matcher.lookingAt() && matcher.group().length() > value.length()) {
                    type = tokenTypes[i];
                    value = matcher.group();
                }
            }
            if (type == TokenType.WHITESPACE) {
                currentPosition += value.length();
                return nextToken();
            }
            number++;
            currentPosition += value.length();
            return new Token(type, value, null, number);

        } while (true);
    }
}
