package ru.nsu.smolin;

import java.nio.file.Path;
import java.io.IOException;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.util.function.Supplier;

/**
 * Класс для парсинга графов из файлов.
 * <p>
 * Парсит файлы вида *.txt, со структурой
 *
 * <pre>
 * вершина1 вершина2
 * вершина3 вершина4
 * ...
 * </pre>
 *
 * где каждая строка - ребро, описываемое двумя вершинами.
 * Разделители — любые пробельные символы (пробелы, табуляция и т.д.).
 * Пример файла:
 * <pre>
 * A B
 * B C
 * D F
 * F C
 * </pre>
 * <h2>Пример</h2>
 * <pre>{@code
 * Path path = Path.of(filename);
 * Graph g = Parser.readEdgeList(path, Factory::adjacentMatrix);
 * System.out.println("Граф из файла (матрица смежности):");
 * System.out.println(g);
 * }</pre>
 */
public class GraphFileUtil {
    public static <G extends Graph> G readFromFile(Path path, Supplier<G> graphSupplier) throws IOException {
        G g = graphSupplier.get();
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] arr = line.trim().split("\\s+");
                if (arr.length == 2) {
                    String u = arr[0];
                    String v = arr[1];
                    g.addVertex(u);
                    g.addVertex(v);
                    g.addEdge(u, v);
                }
            }
        }
        return g;
    }
}
