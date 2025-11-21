package ru.nsu.smolin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestSubstring {

    @TempDir
    Path tempDir;

    @Test
    void testSimpleCase() throws IOException {
        Path testFile = tempDir.resolve("test.txt");
        Files.writeString(testFile, "abracadabra", StandardCharsets.UTF_8);

        SubstringFinder finder = new SubstringFinder();
        List<Integer> result = finder.find(testFile.toString(), "bra");

        assertEquals(List.of(1, 8), result);
    }

    @Test
    void testLargeFile() throws IOException {
        Path largeFile = tempDir.resolve("large.txt");

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(largeFile.toFile()),
                        StandardCharsets.UTF_8))) {

            String pattern = "test";
            int repeatCount = 1_000_000;

            for (int i = 0; i < repeatCount; i++) {
                writer.write(pattern);
                if (i % 1000 == 0) {
                    writer.write("\n");
                }
            }
        }

        SubstringFinder finder = new SubstringFinder();
        List<Integer> result = finder.find(largeFile.toString(), "test");

        assertFalse(result.isEmpty());
        assertTrue(result.size() > 1000);
    }

    @Test
    void testNoMatches() throws IOException {
        Path testFile = tempDir.resolve("test.txt");
        Files.writeString(testFile, "hello world", StandardCharsets.UTF_8);

        SubstringFinder finder = new SubstringFinder();
        List<Integer> result = finder.find(testFile.toString(), "xyz");

        assertTrue(result.isEmpty());
    }

    @Test
    void testOverlappingMatches() throws IOException {
        Path testFile = tempDir.resolve("test.txt");
        Files.writeString(testFile, "aaa", StandardCharsets.UTF_8);

        SubstringFinder finder = new SubstringFinder();
        List<Integer> result = finder.find(testFile.toString(), "aa");

        assertEquals(List.of(0, 1), result);
    }


    @Test
    void testVeryLargeFile() throws IOException {
        Path veryLargeFile = tempDir.resolve("very_large.txt");

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(veryLargeFile.toFile()),
                        StandardCharsets.UTF_8))) {

            String chunk = "x".repeat(1000) + "needle" + "y".repeat(1000);

            for (int i = 0; i < 25_000; i++) {
                writer.write(chunk);
            }
        }

        SubstringFinder finder = new SubstringFinder();
        List<Integer> result = finder.find(veryLargeFile.toString(), "needle");

        assertFalse(result.isEmpty());
        assertEquals(25_000, result.size());
    }

    @Test
    void testMemoryEfficiency() throws IOException {
        Path testFile = tempDir.resolve("memory_test.txt");

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(testFile.toFile()),
                        StandardCharsets.UTF_8))) {

            writer.write("x".repeat(5_000_000));
            writer.write("TARGET");
            writer.write("x".repeat(1_000_000));
        }

        SubstringFinder finder = new SubstringFinder();
        List<Integer> result = finder.find(testFile.toString(), "TARGET");

        assertEquals(1, result.size());
        assertEquals(5_000_000, result.get(0));
    }

    @Test
    void testEmojiSimple() throws IOException {
        Path testFile = tempDir.resolve("emoji.txt");
        Files.writeString(testFile, "😀😀", StandardCharsets.UTF_8);

        SubstringFinder finder = new SubstringFinder();
        List<Integer> result = finder.find(testFile.toString(), "😀");

        assertEquals(List.of(0, 4), result);
    }


}
