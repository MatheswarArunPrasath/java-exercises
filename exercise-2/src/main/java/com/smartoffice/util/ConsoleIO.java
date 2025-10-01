package com.smartoffice.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ConsoleIO {
    private final BufferedReader reader;
    private final PrintStream out;

    public ConsoleIO(InputStream in, OutputStream out) {
        this.reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        this.out = new PrintStream(out, true, StandardCharsets.UTF_8);
    }

    public String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    public void print(String s) {
        out.print(s);
    }

    public void println(String s) {
        out.println(s);
    }
}
