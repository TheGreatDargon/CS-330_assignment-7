package parser;
import tokenizer.Token;
import tokenizer.TokenType;
import parser.Ast.*;
public class AstPrinter implements Visitor<String> {

    public String print(Ast ast) {
        return ast.accept(this);
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

    @Override
    public String visitProgram(Program expr) {
        StringBuilder builder = new StringBuilder();
        builder.append("Program:\n");

        for (Statement statement : expr.statementList){
            builder.append(" ");
            builder.append(statement.accept(this));
            builder.append("\n");
        }

        return builder.toString();
    }

    @Override
    public String visitStatement(Statement expr) {
        return expr.accept(this);
    }

    @Override
    public String visitVariableDeclaration(VariableDeclaration expr) {
        return parenthesize("variable " + expr.identifier.value, expr.expression);
    }

    @Override
    public String visitAssignment(Assignment expr) {
        return parenthesize("assign " + expr.identifier.value, expr.value);
    }

    @Override
    public String visitTypeExpr(Type expr) {
        return expr.type.toString();
    }

    @Override
    public String visitBlockExpr(Block expr) {
        StringBuilder builder = new StringBuilder();
        builder.append("Block:\n");

        for (Statement statement : expr.statementList){
            builder.append(" ");
            builder.append(statement.accept(this));
            builder.append("\n");
        }

        return builder.toString();
    }

    @Override
    public String visitIfStatement(IfStatement expr) {
        StringBuilder builder = new StringBuilder();
        builder.append("if:\n");

        builder.append(expr.condition.accept(this)).append("\n");

        builder.append(expr.ifBlock.accept(this));

        if(expr.elseifConditions != null) {
            for (int i = 0; i < expr.elseifConditions.size(); i++){
                builder.append(" \n");
                builder.append("else if ").append(expr.elseifConditions.get(i).accept(this));
                builder.append("\n");
                builder.append("else if block ").append(expr.elseifBlocks.get(i).accept(this));
            }
        }

        if(expr.elseBlock != null) {
            builder.append("else \nelse block").append(expr.elseBlock.accept(this));
        }

        return builder.toString();
    }

    @Override
    public String visitPokemonLoad(PokemonLoad expr) {
        return parenthesize("Pokemon " + expr.path.value);
    }

    @Override
    public String visitMoveStatement(MoveStatement expr) {
        return parenthesize("move " + expr.pokemonId.value + "slot: " + expr.moveSlot.value + " " + expr.moveName.value);
    }

    @Override
    public String visitWhileStatement(WhileStatement expr) {
        return parenthesize("while ", expr.condition, expr.body);
    }

    @Override
    public String visitForStatement(ForStatement expr) {
        return parenthesize("for", expr.initialize, expr.condition, expr.body);
    }

    @Override
    public String visitPrintStatement(PrintStatement expr) {
        return parenthesize("print ", expr.expression);
    }

    @Override
    public String visitBreakStatement(BreakStatement expr) {
        return "break";
    }

    @Override
    public String visitContinueStatement(ContinueStatement expr) {
        return "continue";
    }

    @Override
    public String visitReturnStatement(ReturnStatement expr) {
        return parenthesize("return ", expr.value);
    }

    @Override
    public String visitSpellDatabaseStatement(SpellDatabaseStatement expr) {
        return parenthesize("spelldatabase" + expr.literal.value);
    }

    @Override
    public String visitShowStatement(ShowStatement expr) {
        return parenthesize("show " +  expr.identifier.value);
    }

    @Override
    public String visitFunctionDeclaration(FunctionDeclaration expr) {
        StringBuilder builder = new StringBuilder();
        builder.append("Function Declaration:\n");

        builder.append(expr.returnType.value);

        for (Parameter parameter : expr.parameters){
            builder.append(" ");
            builder.append(parameter.type.value+ " ");
            builder.append(parameter.name.value);
            builder.append("\n");
        }

        builder.append(expr.body.accept(this));

        return builder.toString();
    }

    @Override
    public String visitExpressionStatement(ExpressionStatement expr) {
        return expr.expression.accept(this);
    }

    @Override
    public String visitExpression(Expression expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinary(Binary expr) {
        return parenthesize(expr.operator.value, expr.left, expr.right);
    }

    @Override
    public String visitUnary(Unary expr) {
        return parenthesize(expr.operator.value, expr.right);
    }

    @Override
    public String visitLiteral(Literal expr) {
        return expr.value.toString();
    }

    @Override
    public String visitIdentifier(Identifier expr) {
        return expr.name;
    }

    @Override
    public String visitGrouping(Grouping expr) {
        return parenthesize("group ", expr.expression);
    }

    @Override
    public String visitAttribute(Attribute expr) {
        return expr.base + "." + expr.attribute;
    }

    @Override
    public String visitCall(Call expr) {
        StringBuilder builder = new StringBuilder();
        builder.append("Call:\n");

        builder.append(expr.name.value).append("(");
        for (Expression argument : expr.arguments){
            builder.append(argument.accept(this));
            builder.append(", ");
        }

        builder.append(")");

        return builder.toString();
    }
}