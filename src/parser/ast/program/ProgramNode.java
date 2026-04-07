package parser.ast.program;

import parser.ast.ASTNode;
import parser.ast.statement.StatementNode;

import java.util.List;

public class ProgramNode extends ASTNode {
    StatementNode statement = new StatementNode();
    List<StatementNode> StatementNode = new List<StatementNode>();
}
