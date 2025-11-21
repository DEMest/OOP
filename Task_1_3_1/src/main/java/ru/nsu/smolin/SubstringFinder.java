package ru.nsu.smolin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SubstringFinder {

    public List<Integer> find(String filename, String substring) throws IOException {
        List<Integer> indexes = new ArrayList<>();

        int[] prefixTable = buildPrefixTable(substring);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8))) {

            int position = 0;
            int matched = 0;
            int c;

            while ((c = reader.read()) != -1) {
                char ch = (char) c;

                while (matched > 0 && ch != substring.charAt(matched)) {
                    matched = prefixTable[matched - 1];
                }

                if (ch == substring.charAt(matched)) {
                    matched++;
                }

                if (matched == substring.length()) {
                    indexes.add(position - substring.length() + 1);
                    matched = prefixTable[matched - 1];
                }

                position++;
            }
        }

        return indexes;
    }

    private int[] buildPrefixTable(String pattern) {
        int[] table = new int[pattern.length()];
        int len = 0;

        for (int i = 1; i < pattern.length(); i++) {
            while (len > 0 && pattern.charAt(i) != pattern.charAt(len)) {
                len = table[len - 1];
            }

            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
            }

            table[i] = len;
        }

        return table;
    }
}
