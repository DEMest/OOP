package ru.nsu.smolin;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Predicate;

import ru.nsu.smolin.impl.Adjacent;
import ru.nsu.smolin.impl.AdjacentList;
import ru.nsu.smolin.impl.Incidence;


/**
 * Example usage.
 *
 */
public class Main {
    /**
     * Example usage.
     */
    public static void main(String[] args) throws IOException {
        MatrixUsage c = new MatrixUsage();
        c.adjMatrixprint();
        c.adjListprint();
        c.incprintprint();
        c.adjMatrixFromFile("graph.txt");
    }
}

/**
 * Usages of Factories and Parser.
 *
 */
class MatrixUsage {
    public void adjMatrixprint() {
        Graph g = new Adjacent();
        g.addVertex("A");
        g.addVertex("B");
        g.addEdge("A","B");
        System.out.println("Матрица смежности\n" + g);
    }

    public void adjListprint() {
        Graph g = new AdjacentList();
        g.addVertex("A");
        g.addVertex("B");
        g.addEdge("A","B");
        System.out.println("Список смежности:\n" + g);
    }
    
    public void incprintprint() {
        Graph g = new Incidence();
        g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");
        g.addEdge("A","B");
        g.addEdge("A","C");
        System.out.println("Матрица инцидентности:\n" + g);
    }

    public void adjMatrixFromFile(String filename) throws IOException {
        Path path = Path.of(filename);
        Graph g = GraphFileUtil.readFromFile(path, Adjacent::new);
        System.out.println("Граф из файла (матрица смежности):");
        System.out.println(g);
    }
}