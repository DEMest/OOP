package ru.nsu.smolin;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            SubstringFinder finder = new SubstringFinder();
            System.out.println(finder.find("src/main/java/ru/nsu/smolin/file.txt", "sdjhjkl"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
