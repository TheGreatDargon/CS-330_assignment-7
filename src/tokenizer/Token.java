package tokenizer;

public class Token {
    public TokenType type;
    public String value;
    public Object literal;
    public int line;

    public Token(TokenType type, String value, Object literal, int number) {
        this.type = type;
        this.value = value;
        this.literal = literal;
        this.line = number;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "tokenizer.Token: " + value + " Type: " + type + " " + "Number: " + line;
    }
}
