package ru.nsu.smolin;


import ru.nsu.smolin.table.Alignment;
import ru.nsu.smolin.table.Table;
import ru.nsu.smolin.table.Text;

public class Main {
    public static void main(String[] args) {
        Table.Builder tableBuilder = new Table.Builder()
                .withAlignments(Alignment.CENTER, Alignment.RIGHT)
                .withRowLimit(8)
                .addRow("Index", "Random");
        for (int i = 0; i < 10; i++) {
            final var value = (int) (Math.random() * 10);
            if (value > 5) {
                tableBuilder.addRow(i, new
                        Text.Bold(String.valueOf(value)));
            } else {
                tableBuilder.addRow(i, (int) (Math.random() * 10));
            }
        }
        System.out.println(tableBuilder.build().toMarkdown());
    }
}
