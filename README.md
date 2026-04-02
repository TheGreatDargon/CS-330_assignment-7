# Course Project - Phase 3 Syntax Analysis - Parser

For the next step of phase 3 Syntax Analysis, we will :

Create a parser

- Takes tokens as input

- Checks whether they follow the grammar

- Builds an abstract syntax tree (AST) (or parse tree)



# AST goals
- Abstract away syntax (no tokens, no parentheses, no semicolons)

- Represent meaning, not grammar mechanics

- Easy to:

    - interpret

    - type-check

    - generate output

    - extend

# Structural conventions
- One base ASTNode

- Separate Statements and Expressions



# <code style="color : red">What to submit:</code>
> [!IMPORTANT]
> Java files of your parser program.<br>
> The parser should prompt the user for the path to a text file containing the test code, then report the number of successfully parsed statements and any errors encountered.