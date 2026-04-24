package parser;

public abstract class BaseVisitor<R> implements Ast.Visitor<R> {

    // Helper to safely visit a node (handles nulls)
    public R visit(Ast node) {
        if (node == null) return null;
        return node.accept( this);
    }

    @Override
    public R visitProgram(ASTNode.Program node) {
        for (ASTNode.Statement stmt : node.statementList) {
            visit(stmt);
        }
        return null;
    }

    @Override
    public R visitBlockExpr(ASTNode.Block node) {
        for (ASTNode.Statement stmt : node.statementList) {
            visit(stmt);
        }
        return null;
    }

    @Override
    public R visitIfStatement(ASTNode.IfStatement node) {
        visit(node.condition);
        visit(node.ifBlock);

        if (node.elseifConditions != null) {
            for (int i = 0; i < node.elseifConditions.size(); i++) {
                visit(node.elseifConditions.get(i));
                visit(node.elseifBlocks.get(i));
            }
        }

        if (node.elseBlock != null) {
            visit(node.elseBlock);
        }
        return null;
    }

    @Override
    public R visitWhileStatement(ASTNode.WhileStatement node) {
        visit(node.condition);
        visit(node.body);
        return null;
    }

    @Override
    public R visitForStatement(ASTNode.ForStatement node) {
        visit(node.initialize);
        visit(node.condition);
        visit(node.body);
        // Note: updateVariable is a Token, not a node, so no visit() needed
        return null;
    }

    @Override
    public R visitBinary(ASTNode.Binary node) {
        visit(node.left);
        visit(node.right);
        return null;
    }

    @Override
    public R visitUnary(ASTNode.Unary node) {
        visit(node.right);
        return null;
    }

    @Override
    public R visitGrouping(ASTNode.Grouping node) {
        visit(node.expression);
        return null;
    }

    @Override
    public R visitCall(ASTNode.Call node) {
        for (ASTNode.Expression arg : node.arguments) {
            visit(arg);
        }
        return null;
    }

    @Override
    public R visitFunctionDeclaration(ASTNode.FunctionDeclaration node) {
        // Parameters are usually just metadata (Tokens), but we visit the body
        visit(node.body);
        return null;
    }

    @Override
    public R visitReturnStatement(ASTNode.ReturnStatement node) {
        if (node.value != null) visit(node.value);
        return null;
    }

    @Override
    public R visitPrintStatement(ASTNode.PrintStatement node) {
        visit(node.expression);
        return null;
    }

    @Override
    public R visitExpressionStatement(ASTNode.ExpressionStatement node) {
        visit(node.expression);
        return null;
    }

    @Override
    public R visitAssignment(ASTNode.Assignment node) {
        visit(node.value);
        return null;
    }

    // --- LEAF NODES (Nothing to visit inside them) ---

    @Override public R visitLiteral(ASTNode.Literal node) { return null; }
    @Override public R visitIdentifier(ASTNode.Identifier node) { return null; }
    @Override public R visitBreakStatement(ASTNode.BreakStatement node) { return null; }
    @Override public R visitContinueStatement(ASTNode.ContinueStatement node) { return null; }
    @Override public R visitPokemonLoad(ASTNode.PokemonLoad node) { return null; }
    @Override public R visitMoveStatement(ASTNode.MoveStatement node) { return null; }
    @Override public R visitTypeExpr(ASTNode.Type node) { return null; }
    @Override public R visitAttribute(ASTNode.Attribute node) { return null; }
    @Override public R visitSpellDatabaseStatement(ASTNode.SpellDatabaseStatement node) { return null; }
    @Override public R visitShowStatement(ASTNode.ShowStatement node) { return null; }
    @Override public R visitStatement(ASTNode.Statement node) { return null; }
    @Override public R visitExpression(ASTNode.Expression node) { return null; }
}