package ru.nsu.smolin.table;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static ru.nsu.smolin.table.Alignment.*;

class TableRendererTest {

    @Test
    void headerAndAlignmentRowRendered() {
        Table table = new Table.Builder()
                .withAlignments(RIGHT, LEFT)
                .withRowLimit(5)
                .addRow("Index", "Random")
                .addRow(1, 8)
                .build();
        String md = table.toMarkdown();
        String[] lines = md.split("\n");
        assertEquals("| Index | Random |", lines[0].trim());
        assertTrue(lines[1].contains("---:"));
        assertTrue(lines[1].contains(":---"));
    }

    @Test
    void centerAlignmentWithColons() {
        Table table = new Table.Builder()
                .withAlignments(CENTER)
                .withRowLimit(2)
                .addRow("X")
                .addRow(1)
                .build();

        String md = table.toMarkdown();
        String[] lines = md.split("\n");
        assertTrue(lines[1].trim().matches("\\| :-: \\|"));
    }
}