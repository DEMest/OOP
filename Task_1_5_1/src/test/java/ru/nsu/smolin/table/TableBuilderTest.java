package ru.nsu.smolin.table;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.nsu.smolin.table.Alignment.LEFT;
import static ru.nsu.smolin.table.Alignment.RIGHT;

class TableBuilderTest {

    @Test
    void addRowSuccess() {
        Table table = new Table.Builder()
                .withAlignments(LEFT, LEFT)
                .withRowLimit(5)
                .addRow("Index", "Value")
                .addRow(1, 10)
                .build();

        String md = table.toMarkdown();

        assertTrue(md.contains("| Index | Value |"));
        assertTrue(md.contains("| 1     | 10    |"));
    }

    @Test
    void addRowWithBoldSuccess() {
        Table table = new Table.Builder()
                .withAlignments(RIGHT, LEFT)
                .withRowLimit(3)
                .addRow("Index", "Value")
                .addRow(1, new Text.Bold("8"))
                .build();
        assertTrue(table.toMarkdown().contains("|     1 | **8** |"));
    }

    @Test
    void rowLimit() {
        Table table = new Table.Builder()
                .withAlignments(LEFT, LEFT)
                .withRowLimit(2)
                .addRow("Index", "Value")
                .addRow(1, 10)
                .addRow(2, 20)
                .build();
        assertTrue(table.toMarkdown().contains("| 1     | 10    |"));
        assertFalse(table.toMarkdown().contains("| 2     | 20    |"));
    }
}