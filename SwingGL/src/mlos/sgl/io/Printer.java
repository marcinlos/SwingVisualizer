package mlos.sgl.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Printer {

    private final Writer output;

    public Printer(Writer output) {
        this.output = output;
    }

    public Printer(OutputStream stream) {
        this.output = new OutputStreamWriter(stream);
    }

    public <T> Printer write(Format<T> format, T value) throws IOException {
        String s = format.print(value);
        output.write(s);
        return this;
    }

    public <T> Printer write(Format<T> format, Iterable<? extends T> items)
            throws IOException {
        for (T item : items) {
            write(format, item);
            output.write('\n');
        }
        return this;
    }

}
