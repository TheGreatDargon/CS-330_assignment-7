package parser;
import tokenizer.Token;
import tokenizer.TokenType;
public class AstPrinter implements Expr.Visitor<String> {
    public String print(Expr expr) {
        return expr.accept(this);
    }
    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.getValue(),
                expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "nil";
        return String.valueOf(expr.value);
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.getValue(), expr.right);
    }
    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        return parenthesize(expr.operator.getValue(), expr.right);
    }
    @Override
    public String visitGetExpr(Expr.Get expr) {
        return parenthesize(expr.operator.getValue(), expr.right);
    }
    @Override
    public String visitCallExpr(Expr.Call expr) {
        return parenthesize(expr.operator.getValue(), expr.right);
    }
    private String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }
    public static void main(String[] args) {
        Expr expression = new Expr.Binary(
                new Expr.Unary(
                        new Token(TokenType.OP_MINUS, "-", null, 1),
                        new Expr.Literal(123)),
                new Token(TokenType.OP_MULTIPLY, "*", null, 1),
                new Expr.Grouping(
                        new Expr.Literal(45.67)));

        System.out.println(new AstPrinter().print(expression));
    }
}