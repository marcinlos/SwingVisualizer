package mlos.sgl.io;

import java.util.Scanner;

public interface Format<T> {

    String print(T value);
    
    T parse(Scanner scanner);
    
}
