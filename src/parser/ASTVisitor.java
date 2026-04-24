package parser;
import parser.ASTNode.*;

public interface ASTVisitor<R> {
    // Top level
    R visitProgram(Program node);

    // Statements
    R visitVariableDeclaration(VariableDeclaration node);
    R visitAssignment(Assignment node);
    R visitTypeExpr(Type node);
    R visitBlockExpr(Block node);
    R visitIfStatement(IfStatement node);
    R visitWhileStatement(WhileStatement node);
    R visitForStatement(ForStatement node);
    R visitPrintStatement(PrintStatement node);
    R visitReturnStatement(ReturnStatement node);
    R visitExpressionStatement(ExpressionStatement node);

    // Commands/Special Statements
    R visitPokemonLoad(PokemonLoad node);
    R visitMoveStatement(MoveStatement node);
    R visitBreakStatement(BreakStatement node);
    R visitContinueStatement(ContinueStatement node);
    R visitSpellDatabaseStatement(SpellDatabaseStatement node);
    R visitShowStatement(ShowStatement node);
    R visitFunctionDeclaration(FunctionDeclaration node);

    // Expressions
    R visitBinary(Binary node);
    R visitUnary(Unary node);
    R visitLiteral(Literal node);
    R visitIdentifier(Identifier node);
    R visitGrouping(Grouping node);
    R visitAttribute(Attribute node);
    R visitCall(Call node);

    // Base catch-alls (Optional, but helpful for generic logic)
    R visitStatement(Statement node);
    R visitExpression(Expression node);
}

