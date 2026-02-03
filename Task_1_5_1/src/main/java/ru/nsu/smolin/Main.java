package ru.nsu.smolin;

import ru.nsu.smolin.block.CodeBlock;
import ru.nsu.smolin.block.Heading;
import ru.nsu.smolin.block.Paragraph;
import ru.nsu.smolin.table.Alignment;
import ru.nsu.smolin.table.Table;
import ru.nsu.smolin.table.Text;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Element title = new Heading(2, new Text.Plain(
                "Example: Computing the Mean and Variance"
        ));

        Element problem = new Paragraph(List.of(
                new Text.Plain("""
                                A player flips a fair coin twice. For each heads they receive 10 dollars,
                                for each tails they receive 0 dollars.
                                Let X be the total payoff after two flips."""
                )
        ));

        Table.Builder tableBuilder = new Table.Builder()
                .withAlignments(
                        Alignment.RIGHT,
                        Alignment.LEFT,
                        Alignment.RIGHT,
                        Alignment.RIGHT
                )
                .withRowLimit(10)
                .addRow("X (payoff)", "P(X)", "X·P(X)", "X^2·P(X)");

        tableBuilder.addRow(0, "1/4", 0, 0);
        tableBuilder.addRow(10, "1/2", 5, 50);
        tableBuilder.addRow(20, "1/4", 5, 100);

        Element distributionTable = tableBuilder.build();

        Element explanation = new Paragraph(List.of(
                new Text.Plain("""
                                From the table we get: E(X) = 0·1/4 + 10·1/2 + 20·1/4 = 10.
                                Next, E(X^2) = 0^2·1/4 + 10^2·1/2 + 20^2·1/4 = 150,
                                so the variance is Var(X) = E(X^2) − [E(X)]^2 = 150 − 10^2 = 50."""
                )
        ));

        String code = """
                record Outcome(int x, double p) {}

                var data = java.util.List.of(
                    new Outcome(0, 0.25),
                    new Outcome(10, 0.5),
                    new Outcome(20, 0.25)
                );

                double mean = data.stream()
                        .mapToDouble(o -> o.x() * o.p())
                        .sum();

                double ex2 = data.stream()
                        .mapToDouble(o -> o.x() * o.x() * o.p())
                        .sum();

                double variance = ex2 - mean * mean;

                System.out.println("E(X) = " + mean);
                System.out.println("Var(X) = " + variance);
                """;

        Element codeBlock = new CodeBlock("java", code);

        Element document = new Document(List.of(
                title,
                problem,
                distributionTable,
                explanation,
                codeBlock
        ));

        System.out.println(document.toMarkdown());
    }
}