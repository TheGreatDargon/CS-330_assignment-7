package tokenizer;

public class Token {
    private TokenType type;
    private String value;
    private int number;

    public Token(TokenType type, String value, int number) {
        this.type = type;
        this.value = value;
        this.number = number;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "tokenizer.Token: " + value + " Type: " + type + " " + "Number: " + number;
    }
}
