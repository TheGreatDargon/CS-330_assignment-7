package parser;

import java.util.ArrayList;
import java.util.List;

public abstract class Expr {
    interface Visitor<R> {
        R visitProgramExpr(Program expr);
        R visitStatementExpr(Statement expr);
        R visitStatementTailExpr(StatementTail expr);
        R visitLiteralExpr(Literal expr);
        R visitExpressionExpr(Expression expr);
        R visitAttributeExpr(Attribute expr);
        R visitBlockExpr(Block expr);
        R visitIfStatementExpr(IfStatement expr);
        R visitVariableDeclarationExpr(VariableDeclaration expr);
        R visitIdentifierExpr(Identifier expr);
    }
    abstract <R> R accept(Visitor<R> visitor);

    static class Program extends Expr {
        List<Statement> statementList = new ArrayList<>();

        public Program(List<Statement> statementList){
            this.statementList = statementList;
        }
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitProgramExpr(this);
        }

    }
    static class Statement extends Expr {

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitStatementExpr(this);
        }
    }
    static class Literal extends Expr {
        public Object value;

        Literal(Object value) {
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }
    }
    static class Expression extends Expr {

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpressionExpr(this);
        }
    }
    static class StatementTail extends Statement {
        List<Statement> statementList = new ArrayList<>();

        public StatementTail(List<Statement> statementList){
            this.statementList = statementList;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitStatementTailExpr(this);
        }
    }
    static class Block extends Statement {
        List<Statement> statementList = new ArrayList<>();

        public Block(List<Statement> statementList){
            this.statementList = statementList;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBlockExpr(this);
        }
    }
    static class IfStatement extends Statement {
        Expression condition = new Expression();
        List<Expression> elseifConditions = new ArrayList<>();
        List<Block> elseifBlocks = new ArrayList<>();
        List<Statement> statementList = new ArrayList<>();
        Block thenBlock = new Block(statementList);

        public IfStatement(Expression condition, List<Expression> elseifConditions, List<Block> elseifBlocks, List<Statement> statementList, Block thenBlock) {
            this.condition = condition;
            this.elseifConditions = elseifConditions;
            this.elseifBlocks = elseifBlocks;
            this.statementList = statementList;
            this.thenBlock = thenBlock;
        }
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitIfStatementExpr(this);
        }
    }
    static class VariableDeclaration extends Statement {
        final String type;
        final String name;
        Expression value = new Expression();

        public VariableDeclaration(String type, String name, Expression value) {
            this.type = type;
            this.name = name;
            this.value = value;
        }
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVariableDeclarationExpr(this);
        }
    }
    static class Identifier extends Expression {
        public String name;
        public Object value;

        public Identifier(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitIdentifierExpr(this);
        }
    }
    static class Attribute extends Expression {
        Attribute(String base, String attribute){
            this.base = base;
            this.attribute = attribute;
        }

        final String base;
        final String attribute;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitAttributeExpr(this);
        }
    }
}
