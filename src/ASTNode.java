import java.util.List;

public class ASTNode {
    Lexer lexer = new Lexer(code.toString());
    List<Token> tokens = lexer.tokenize();
}
