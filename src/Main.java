import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.io.File;

public class Main {
    public static void main(String[] args) {

        File codeFile = new File("./input.txt");
        Scanner s = null;
        try {
            s = new Scanner(codeFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        int count = 1;
        String code = "";
        s.useDelimiter("");
        while(s.hasNext()) {
            code += s.next();
        }

        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.tokenize();

        for (Token token : tokens) {
            System.out.println(token);
            count++;
        }

    }
}