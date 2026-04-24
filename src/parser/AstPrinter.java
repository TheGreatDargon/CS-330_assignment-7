package parser;

import parser.ASTNode.*;

public class AstPrinter extends BaseVisitor<String> {

    public String print(Ast ast) {
        return visit(ast);
    }

    private String parenthesize(String name, Ast... asts) {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(name);
        for (Ast ast : asts) {
            builder.append(" ");
            builder.append(visit(ast));
        }
        builder.append(")");
        return builder.toString();
    }

    @Override
    public String visitProgram(Program expr) {
        StringBuilder builder = new StringBuilder();
        builder.append("Program:\n");
        for (Statement statement : expr.statementList) {
            builder.append(" ").append(visit(statement)).append("\n");
        }
        return builder.toString();
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
        for (Statement statement : expr.statementList) {
            builder.append(" ").append(visit(statement)).append("\n");
        }
        return builder.toString();
    }

    @Override
    public String visitIfStatement(IfStatement expr) {
        StringBuilder builder = new StringBuilder();
        builder.append("(if ").append(visit(expr.condition)).append("\n");
        builder.append(" then ").append(visit(expr.ifBlock));

        if (expr.elseifConditions != null) {
            for (int i = 0; i < expr.elseifConditions.size(); i++) {
                builder.append("\n elif ").append(visit(expr.elseifConditions.get(i)));
                builder.append(" then ").append(visit(expr.elseifBlocks.get(i)));
            }
        }
        if (expr.elseBlock != null) {
            builder.append("\n else ").append(visit(expr.elseBlock));
        }
        builder.append(")");
        return builder.toString();
    }

    @Override
    public String visitWhileStatement(WhileStatement expr) {
        return parenthesize("while", expr.condition, expr.body);
    }

    @Override
    public String visitForStatement(ForStatement expr) {
        return parenthesize("for", expr.initialize, expr.condition, expr.body);
    }

    @Override
    public String visitPrintStatement(PrintStatement expr) {
        return parenthesize("print", expr.expression);
    }

    @Override
    public String visitReturnStatement(ReturnStatement expr) {
        return parenthesize("return", expr.value);
    }

    @Override
    public String visitFunctionDeclaration(FunctionDeclaration expr) {
        StringBuilder builder = new StringBuilder();
        builder.append("(fn ").append(expr.name.value).append(" [");
        for (ASTNode.Parameter parameter : expr.parameters) {
            builder.append(parameter.type.value).append(" ").append(parameter.name.value).append(" ");
        }
        builder.append("] ").append(visit(expr.body)).append(")");
        return builder.toString();
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
        return expr.value == null ? "nil" : expr.value.toString();
    }

    @Override
    public String visitIdentifier(Identifier expr) {
        return expr.name;
    }

    @Override
    public String visitGrouping(Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitCall(Call expr) {
        StringBuilder builder = new StringBuilder();
        builder.append("(call ").append(expr.name.value).append(" ");
        for (Expression argument : expr.arguments) {
            builder.append(visit(argument)).append(" ");
        }
        builder.append(")");
        return builder.toString();
    }

    @Override
    public String visitPokemonLoad(PokemonLoad expr) {
        return parenthesize("load-pokemon " + expr.path.value);
    }

    @Override
    public String visitMoveStatement(MoveStatement expr) {
        return "(move " + expr.pokemonId.value + " slot:" + expr.moveSlot.value + " " + expr.moveName.value + ")";
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
    public String visitAttribute(Attribute expr) {
        return expr.base + "." + expr.attribute;
    }

    @Override
    public String visitExpressionStatement(ExpressionStatement expr) {
        return visit(expr.expression);
    }

    @Override
    public String visitSpellDatabaseStatement(SpellDatabaseStatement expr) {
        return parenthesize("spelldatabase " + expr.literal.value);
    }

    @Override
    public String visitShowStatement(ShowStatement expr) {
        return parenthesize("show " + expr.identifier.value);
    }
}