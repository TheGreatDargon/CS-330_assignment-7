package parser.ast.expression;

static class Binary extends Expression {
    Binary(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    final Expression left;
    final Token operator;
    final Expression right;
}
