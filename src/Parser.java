import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList
import java.util.List;
import java.util.Scanner;

public class Parser {
    public List<Token> tokens;
    public int token;
    public LinkedList<Integer> linesIndicators;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.token = 0;
    }
    public void parse(List<Token> toks, LinkedList<Integer> indis) {
        this.tokens = toks;
        this.linesIndicators = indis;
        project_def();
        if (tokens[token]..compareTo(".") == 0) {
            System.out.println("successful parsing");
        } else {
            System.out.println("parsing failed: unexpected EOF");
        }
    }
    public void error(String expstr) {
        int firstie = linesIndicators.getFirst();
        int linesnum = 1;
        while (firstie < token) {
            linesIndicators.removeFirst();
            firstie = linesIndicators.getFirst();
            linesnum++;
        }
        System.out.println("error accured at token: " + (token + 1) + ",Line: " + linesnum + ", a (" + expstr
                + ") token expected but (" + tokens[token] + ") was scanned instead");
        System.exit(0);
    }
}
