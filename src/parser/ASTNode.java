package parser;

import tokenizer.Token;

import java.util.ArrayList;
import java.util.List;

public class ASTNode {
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
            return visitor.visitProgram(this);
        }

    }

    /*
     * STATEMENT
     */

    static class Statement extends Ast {
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitStatement(this);
        }
    }

    static class VariableDeclaration extends Statement {
        final Token type;
        final Token identifier;
        final Expression expression;

        public VariableDeclaration(Token type, Token identifier, Expression expression){
            this.type = type;
            this.identifier = identifier;
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVariableDeclaration(this);
        }
    }
    static class Assignment extends Statement {
        final Token identifier;
        final Token property;
        final Token field;
        final Expression value;

        public Assignment(Token identifier, Token property, Token field, Expression value) {
            this.identifier = identifier;
            this.property = property;
            this.field = field;
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
        final Expression condition;
        final List<Expression> elseifConditions;
        final Block ifBlock;
        final List<Block> elseifBlocks;
        final Block elseBlock;

        public IfStatement(Expression condition, List<Expression> elseifConditions, Block ifBlock, List<Block> elseifBlocks, Block elseBlock) {
            this.condition = condition;
            this.elseifConditions = elseifConditions;
            this.ifBlock = ifBlock;
            this.elseifBlocks = elseifBlocks;
            this.elseBlock = elseBlock;
        }
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitIfStatement(this);
        }
    }

    static class PokemonLoad extends Statement {
        final Token path;

        public PokemonLoad(Token path) {
            this.path = path;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPokemonLoad(this);
        }
    }

    static class MoveStatement extends Statement {
        final Token pokemonId;
        final Token moveSlot;
        final Token moveName;

        public MoveStatement(Token pokemonId, Token moveSlot, Token moveName) {
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
        final Token operator;
        final Block body;

        public ForStatement(VariableDeclaration initialize, Expression condition, Token updateVariable, Token operator, Block body) {
            this.initialize = initialize;
            this.condition = condition;
            this.updateVariable = updateVariable;
            this.operator = operator;
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

    static class ContinueStatement extends Statement {
        @Override
        <R> R accept(Visitor<R> visitor) { return visitor.visitContinueStatement(this); }
    }

    static class ReturnStatement extends Statement {
        final Expression value;
        public ReturnStatement(Expression value) { this.value = value; }

        @Override
        <R> R accept(Visitor<R> visitor) { return visitor.visitReturnStatement(this); }
    }

    static class SpellDatabaseStatement extends Statement {
        final Token literal;

        public SpellDatabaseStatement(Token literal) {
            this.literal = literal;
        }

        @Override
        <R> R accept(Visitor<R> visitor) { return visitor.visitSpellDatabaseStatement(this); }
    }

    static class ShowStatement extends Statement {
        final Token identifier;

        public ShowStatement(Token identifier) {
            this.identifier = identifier;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitShowStatement(this);
        }
    }

    static class FunctionDeclaration extends Statement {
        final Token returnType;
        final Token name;
        final List<Parameter> parameters;
        final Block body;

        public FunctionDeclaration(Token returnType, Token name, List<Parameter> parameters, Block body){
            this.returnType = returnType;
            this.name = name;
            this.parameters = parameters;
            this.body = body;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitFunctionDeclaration(this);
        }
    }

    static class ExpressionStatement extends Statement {
        final Expression expression;
        public ExpressionStatement(Expression expression) {
            this.expression = expression;
        }
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpressionStatement(this);
        }
    }

    static class Parameter {
        final Token type;
        final Token name;
        public Parameter(Token type, Token name) {
            this.type = type;
            this.name = name;
        }
    }

    /*
     * EXPRESSION
     */

    static class Expression extends Ast {

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpression(this);
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
            return visitor.visitLiteral(this);
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
            return visitor.visitIdentifier(this);
        }
    }

    static class Grouping extends Expression {
        public Expression expression;

        public Grouping(Expression expr){this.expression = expr;}
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGrouping(this);
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
            return visitor.visitAttribute(this);
        }
    }

    static class Call extends Expression {
        final Token name;
        final List<Expression> arguments;
        public Call(Token name, List<Expression> arguments){
            this.name = name;
            this.arguments = arguments;
        }
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitCall(this);
        }
    }
}
