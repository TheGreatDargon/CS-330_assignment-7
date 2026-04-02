import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.io.File;

public class Main {
    public static void main(String[] args) {


        Scanner input = new Scanner(System.in);
        System.out.print("Please input your code file path: ");
        String codeFilePath = input.nextLine();

        File codeFile = new File(codeFilePath);
        try {
            input = new Scanner(codeFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        StringBuilder code = new StringBuilder();
        input.useDelimiter("");
        while(input.hasNext()) {
            code.append(input.next());
        }

        Lexer lexer = new Lexer(code.toString());
        List<Token> tokens = lexer.tokenize();

        for (Token token : tokens) {
            System.out.println(token);
        }

    }
}