package tokenizer;

public class Token {
    public TokenType type;
    public String value;
    public Object literal;
    public int tokenNumber;
    public int line;

    public Token(TokenType type, String value, Object literal, int number, int line) {
        this.type = type;
        this.value = value;
        this.literal = literal;
        this.tokenNumber = number;
        this.line = line;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "tokenizer.Token: " + value + " Type: " + type + " " + "Number: " + tokenNumber;
    }
}
