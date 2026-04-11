package parser;
import tokenizer.Token;

import java.util.ArrayList;
import java.util.List;

public abstract class Ast {
    interface Visitor<R> {
        // Program
        R visitProgramExpr(Program expr);
        // Statement
        R visitStatementExpr(Statement expr);
        R visitVariableDeclarationExpr(VariableDeclaration expr);
        R visitAssignment(Assignment expr);
        R visitTypeExpr(Type expr);
        R visitBlockExpr(Block expr);
        R visitIfStatementExpr(IfStatement expr);
        R visitPokemonLoad(PokemonLoad expr);
        R visitMoveStatement(MoveStatement expr);
        R visitWhileStatement(WhileStatement expr);
        R visitForStatement(ForStatement expr);
        R visitPrintStatement(PrintStatement expr);
        R visitBreakStatement(BreakStatement expr);
        R visitReturnStatement(ReturnStatement expr);
        // Expression
        R visitExpressionExpr(Expression expr);
        R visitBinary(Binary expr);
        R visitUnary(Unary expr);
        R visitLiteralExpr(Literal expr);
        R visitIdentifierExpr(Identifier expr);
        R visitAttributeExpr(Attribute expr);
    }
    abstract <R> R accept(Visitor<R> visitor);

    /*
    * PROGRAM
    */

    static class Program extends Ast {
        List<Statement> statementList = new ArrayList<>();

        public Program(List<Statement> statementList){
            this.statementList = statementList;
        }
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitProgramExpr(this);
        }

    }

    /*
    * STATEMENT
    */

    static class Statement extends Ast {

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitStatementExpr(this);
        }
    }

    static class VariableDeclaration extends Statement {
        final String type;
        final String identifier;
        final Expression expression;

        public VariableDeclaration(String type, String identifier, Expression expression){
            this.type = type;
            this.identifier = identifier;
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVariableDeclarationExpr(this);
        }
    }
    static class Assignment extends Statement {
        final String identifier;
        final String field;
        final Token operator;
        final Expression value;

        public Assignment(String identifier, String field, Token operator, Expression value) {
            this.identifier = identifier;
            this.field = field;
            this.operator = operator;
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitAssignment(this);
        }

    }
    static class Type extends Statement {
        public Object type;

        public Type(Object type){
            this.type = type;
        }
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitTypeExpr(this);
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

    static class PokemonLoad extends Statement {
        final String identifier;
        final String pokemonName;

        public PokemonLoad(String identifier, String pokemonName) {
            this.identifier = identifier;
            this.pokemonName = pokemonName;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPokemonLoad(this);
        }
    }

    static class MoveStatement extends Statement {
        final String pokemonId;
        final String moveSlot;
        final String moveName;

        public MoveStatement(String pokemonId, String moveSlot, String moveName) {
            this.pokemonId = pokemonId;
            this.moveSlot = moveSlot;
            this.moveName = moveName;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitMoveStatement(this);
        }

    }

    static class WhileStatement extends Statement {
        final Expression condition;
        final Block body;

        public WhileStatement(Expression condition, Block body) {
            this.condition = condition;
            this.body = body;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitWhileStatement(this);
        }
    }

    static class ForStatement extends Statement {
        final VariableDeclaration initialize;
        final Expression condition;
        final Token updateVariable;
        final Block body;

        public ForStatement(VariableDeclaration initialize, Expression condition, Token updateVariable, Block body) {
            this.initialize = initialize;
            this.condition = condition;
            this.updateVariable = updateVariable;
            this.body = body;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitForStatement(this);
        }
    }

    static class PrintStatement extends Statement {
        final Expression expression;
        public PrintStatement(Expression expression){
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrintStatement(this);
        }
    }

    static class BreakStatement extends Statement {
        @Override
        <R> R accept(Visitor<R> visitor) { return visitor.visitBreakStatement(this); }
    }

    static class ReturnStatement extends Statement {
        final Expression value;
        public ReturnStatement(Expression value) { this.value = value; }

        @Override
        <R> R accept(Visitor<R> visitor) { return visitor.visitReturnStatement(this); }
    }

    /*
    * EXPRESSION
    */

    static class Expression extends Ast {

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpressionExpr(this);
        }
    }

    static class Binary extends Expression {
        final Expression left;
        final Token operator;
        final Expression right;

        public Binary(Expression left, Token operator, Expression right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinary(this);
        }
    }

    static class Unary extends Expression {
        final Token operator;
        final Expression right;

        public Unary(Token operator, Expression right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnary(this);
        }
    }

    static class Literal extends Expression {
        public Object value;

        Literal(Object value) {
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
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
