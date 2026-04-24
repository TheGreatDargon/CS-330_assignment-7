package parser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

public class SymbolTable {
    // A list of maps: index 0 is global scope, last index is current innermost scope
    private final LinkedList<Map<String, Symbol>> scopes = new LinkedList<>();

    public SymbolTable() {
        // Initialize with a global scope
        pushScope();
    }

    public void pushScope() {
        scopes.addLast(new HashMap<>());
    }

    public void popScope() {
        if (scopes.size() > 1) {
            scopes.removeLast();
        } else {
            throw new RuntimeException("Cannot pop the global scope.");
        }
    }

    public void define(String name, Symbol symbol) {
        Map<String, Symbol> currentScope = scopes.getLast();
        if (currentScope.containsKey(name)) {
            throw new RuntimeException("Variable '" + name + "' already defined in this scope.");
        }
        currentScope.put(name, symbol);
    }
    public Symbol lookup(String name) {// Search from back to front (innermost to outermost)
        ListIterator<Map<String, Symbol>> it = scopes.listIterator(scopes.size());
        while (it.hasPrevious()) {
            Map<String, Symbol> scope = it.previous();
            if (scope.containsKey(name)) {
                return scope.get(name);}}
        throw new RuntimeException("Undefined variable: " + name);
    }
}