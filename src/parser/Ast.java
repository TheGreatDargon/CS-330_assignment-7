package parser;
import tokenizer.Token;

import java.util.ArrayList;
import java.util.List;
import parser.ASTNode.*;

public abstract class Ast {
    interface Visitor<R> {
        // Program
        R visitProgram(Program expr);
        // Statement
        R visitStatement(Statement expr);
        R visitVariableDeclaration(VariableDeclaration expr);
        R visitAssignment(Assignment expr);
        R visitTypeExpr(Type expr);
        R visitBlockExpr(Block expr);
        R visitIfStatement(IfStatement expr);
        R visitPokemonLoad(PokemonLoad expr);
        R visitMoveStatement(MoveStatement expr);
        R visitWhileStatement(WhileStatement expr);
        R visitForStatement(ForStatement expr);
        R visitPrintStatement(PrintStatement expr);
        R visitBreakStatement(BreakStatement expr);
        R visitContinueStatement(ContinueStatement expr);
        R visitReturnStatement(ReturnStatement expr);
        R visitSpellDatabaseStatement(SpellDatabaseStatement expr);
        R visitShowStatement(ShowStatement expr);
        R visitFunctionDeclaration(FunctionDeclaration expr);
        R visitExpressionStatement(ExpressionStatement expr);
        // Expression
        R visitExpression(Expression expr);
        R visitBinary(Binary expr);
        R visitUnary(Unary expr);
        R visitLiteral(Literal expr);
        R visitIdentifier(Identifier expr);
        R visitGrouping(Grouping expr);
        R visitAttribute(Attribute expr);
        R visitCall(Call expr);
    }
    abstract <R> R accept(Visitor<R> visitor);


}
