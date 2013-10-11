package mlos.sgl.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {
    
    private final Scanner input;

    public Parser(Scanner scanner) {
        this.input = scanner;
    }
    
    public Parser(Reader input) {
        this(new Scanner(input));
    }
    
    public Parser(String string) {
        this(new Scanner(string));
    }
    
    public Parser(File file) throws FileNotFoundException {
        this(new Scanner(file));
    }
    
    public Parser(InputStream stream) {
        this(new Scanner(stream));
    }

    
    public <T> T parse(Format<T> format) {
        return format.parse(input);
    }
    
    public <T> List<T> parseAll(Format<T> format) {
        List<T> items = new ArrayList<>();
        T item;
        
        while ((item = format.parse(input)) != null) {
            items.add(item);
        }
        return items;
    }

}
