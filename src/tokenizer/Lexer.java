package tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private String input;
    private int currentPosition;
    private int number;
    private int line = 1;
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
                if (currentChar == '\n') {
                    line++;
                }
                currentPosition++;
                continue;
            }
            if (currentChar == '/' && currentPosition + 1 < input.length() && input.charAt(currentPosition + 1) == '/') {
                while (currentPosition < input.length() && input.charAt(currentPosition) != '\n') {
                    currentPosition++;
                }
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

        tokens.add(new Token(TokenType.EOF, "EOF", null, ++number, line));

        return tokens;
    }

    private Token nextToken() {

        String[] tokenPatterns = {
                // Add the regex rules from token table here
                "Pokemon\\b", // Pokemon Keyword
                "int\\b", // int keyword
                "String\\b", // String keyword
                "boolean\\b", // boolean keyword
                "function\\b",
                "void\\b",
                "elseif\\b",
                "else\\b",
                "show\\b",
                "spelldatabase\\b",
                "move1\\b",
                "move2\\b",
                "move3\\b",
                "move4\\b",
                "continue\\b",
                "break\\b",
                "return\\b",
                "print\\b",
                "while\\b",
                "if\\b",
                "for\\b",
                "true\\b",
                "false\\b",
                "[a-zA-Z_]([a-zA-Z_]|[0-9])*", // Identifiers
                "0|[1-9][0-9]*", // Int Literal
                "\"[^\"]*\"", // String Literals
                "=", // Assignment operator
                "==", // Compare Operator
                "\\+\\+",
                "--",
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
                // Keywords
                TokenType.KW_POKEMON,               // "Pokemon"
                TokenType.KW_INT,                   // "int"
                TokenType.KW_STRING,                // "String"
                TokenType.KW_BOOLEAN,               // "boolean"
                TokenType.KW_FUNCTION,              // "function"
                TokenType.KW_VOID,                  // "void"
                TokenType.KW_ELSEIF,                // "elseif"
                TokenType.KW_ELSE,                  // "else"
                TokenType.KW_SHOW,                  // "show"
                TokenType.KW_SPELLDATABASE,         // "spelldatabase"
                TokenType.KW_MOVE1,                 // "move1"
                TokenType.KW_MOVE2,                 // "move2"
                TokenType.KW_MOVE3,                 // "move3"
                TokenType.KW_MOVE4,                 // "move4"
                TokenType.KW_CONTINUE,              // "continue"
                TokenType.KW_BREAK,                 // "break"
                TokenType.KW_RETURN,                // "return"
                TokenType.KW_PRINT,                 // "print"
                TokenType.KW_WHILE,                 // "while"
                TokenType.KW_IF,                    // "if"
                TokenType.KW_FOR,                   // "for"

                // Literals and Identifiers
                TokenType.OP_TRUE,                  // "true"
                TokenType.OP_FALSE,                 // "false"
                TokenType.IDENTIFIER,               // "[a-zA-Z_]([a-zA-Z_]|[0-9])*"
                TokenType.INT_LITERAL,              // "0|[1-9][0-9]*"
                TokenType.STRING_LITERAL,           // "\"[^\"]*\""

                // Operators and Symbols
                TokenType.OP_ASSIGN,                // "="
                TokenType.OP_EQUALS,                // "=="
                TokenType.OP_INCREMENT,             // "++"
                TokenType.OP_DECREMENT,             // "--"
                TokenType.OP_LESS_THAN,             // "<"
                TokenType.OP_GREATER_THAN,          // ">"
                TokenType.OP_LESS_THAN_EQUALS,      // "<="
                TokenType.OP_GREATER_THAN_EQUALS,   // ">="
                TokenType.OP_PLUS_ASSIGN,           // "+="
                TokenType.OP_MINUS_ASSIGN,          // "-="
                TokenType.OP_PLUS,                  // "+"
                TokenType.OP_MINUS,                 // "-"
                TokenType.OP_MULTIPLY,              // "*"
                TokenType.OP_DIVIDE,                // "/"
                TokenType.OP_AND,                   // "&&"
                TokenType.OP_OR,                    // "||"
                TokenType.OP_NOT,                   // "!"
                TokenType.OP_NOTEQUAL,              // "!="
                TokenType.LPAREN,                   // "("
                TokenType.RPAREN,                   // ")"
                TokenType.LBRACE,                   // "{"
                TokenType.RBRACE,                   // "}"
                TokenType.SEMICOLON,                // ";"
                TokenType.COMMA,                    // ","
                TokenType.DOT,                      // "."
                TokenType.WHITESPACE,               // Whitespace regex
                TokenType.EOF                       // "\z"
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

            number++;
            currentPosition += value.length();
            if (value.isEmpty()) {
                throw new RuntimeException("Lexer Error: Unexpected character '" + input.charAt(currentPosition) + "' at line " + line);
            }
            return new Token(type, value, null, number, line);

        } while (true);
    }
}
