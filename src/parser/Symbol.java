package parser;

public class Symbol {
    public final String name;
    public final String type; // e.g., "int", "string"

    public Symbol(String name, String type) {
        this.name = name;
        this.type = type;
    }
}
