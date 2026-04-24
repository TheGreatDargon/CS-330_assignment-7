package parser;

import parser.ASTNode.*;

public class ScopeVisitor extends BaseVisitor<Void> {

    private final SymbolTable symbolTable = new SymbolTable();

    @Override
    public Void visitBlockExpr(Block node) {
        symbolTable.pushScope();

        super.visitBlockExpr(node);

        symbolTable.popScope();
        return null;
    }

    @Override
    public Void visitVariableDeclaration(VariableDeclaration node) {
        if (node.expression != null) {
            visit(node.expression);
        }

        // 2. Register the variable in the symbol table
        String name = node.identifier.value;
        String type = node.type.value; // Assuming Token has a value string

        symbolTable.define(name, new Symbol(name, type));

        return null;
    }

    @Override
    public Void visitIdentifier(Identifier node) {
        try {
            symbolTable.lookup(node.name);
        } catch (RuntimeException e) {
            throw new RuntimeException("Semantic Error: Variable '" + node.name + "' not defined.");
        }
        return null;
    }

    @Override
    public Void visitFunctionDeclaration(FunctionDeclaration node) {
        symbolTable.define(node.name.value, new Symbol(node.name.value, node.returnType.value));

        symbolTable.pushScope();

        for (Parameter param : node.parameters) {
            symbolTable.define(param.name.value, new Symbol(param.name.value, param.type.value));
        }

        visit(node.body);

        symbolTable.popScope();
        return null;
    }

    @Override
    public Void visitAssignment(Assignment node) {
        // Verify the variable being assigned to exists
        symbolTable.lookup(node.identifier.value);

        // Visit the value being assigned
        visit(node.value);
        return null;
    }
    @Override
    public Void visitAttribute(Attribute node) {
        try {
            symbolTable.lookup(node.base);
        } catch (RuntimeException e) {
            throw new RuntimeException("Semantic Error: Variable '" + node.base + "' not defined.");
        }
        return null;
    }
}