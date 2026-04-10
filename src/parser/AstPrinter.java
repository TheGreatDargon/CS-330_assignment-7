package parser;
import tokenizer.Token;
import tokenizer.TokenType;
public class AstPrinter implements Ast.Visitor<String> {
    public String print(Ast ast) {
        return ast.accept(this);
    }
    @Override
    public String visitBinaryExpr(Ast.Binary expr) {
        return parenthesize(expr.operator.getValue(),
                expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Ast.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Ast.Literal expr) {
        if (expr.value == null) return "nil";
        return String.valueOf(expr.value);
    }

    @Override
    public String visitUnaryExpr(Ast.Unary expr) {
        return parenthesize(expr.operator.getValue(), expr.right);
    }
    @Override
    public String visitVariableExpr(Ast.Variable expr) {
        return parenthesize(expr.operator.getValue(), expr.right);
    }
    @Override
    public String visitGetExpr(Ast.Get expr) {
        return parenthesize(expr.operator.getValue(), expr.right);
    }
    @Override
    public String visitCallExpr(Ast.Call expr) {
        return parenthesize(expr.operator.getValue(), expr.right);
    }
    private String parenthesize(String name, Ast... asts) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Ast ast : asts) {
            builder.append(" ");
            builder.append(ast.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }
    public static void main(String[] args) {
        Ast expression = new Ast.Binary(
                new Ast.Unary(
                        new Token(TokenType.OP_MINUS, "-", null, 1),
                        new Ast.Literal(123)),
                new Token(TokenType.OP_MULTIPLY, "*", null, 1),
                new Ast.Grouping(
                        new Ast.Literal(45.67)));

        System.out.println(new AstPrinter().print(expression));
    }
}