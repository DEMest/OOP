package ru.nsu.smolin;

import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

public class GraphFileUtilTest {
    @Test
    void parsesGraphTxt_adjMatrix() throws Exception {
        Graph g = GraphFileUtil.readFromFile(Path.of("graph.txt"), Factory::adjacentMatrix);
        assertTrue(g.hasEdge("A", "B"));
        assertTrue(g.hasEdge("A", "C"));
        assertTrue(g.hasEdge("C", "D"));
        assertTrue(g.hasEdge("C", "F"));
        assertTrue(g.hasEdge("D", "F"));
        assertFalse(g.hasEdge("B", "A"));
        assertFalse(g.hasEdge("C", "A"));
        assertFalse(g.hasEdge("D", "C"));
    }

    @Test
    void parsesGraphTxt_adjList() throws Exception {
        Graph g = GraphFileUtil.readFromFile(Path.of("graph.txt"), Factory::adjacentList);
        assertTrue(g.hasEdge("A", "B"));
        assertTrue(g.hasEdge("A", "C"));
        assertTrue(g.hasEdge("C", "D"));
        assertTrue(g.hasEdge("C", "F"));
        assertTrue(g.hasEdge("D", "F"));
        assertFalse(g.hasEdge("B", "A"));
    }

    @Test
    void parsesGraphTxt_incidence() throws Exception {
        Graph g = GraphFileUtil.readFromFile(Path.of("graph.txt"), Factory::incidence);
        assertTrue(g.hasEdge("A", "B"));
        assertTrue(g.hasEdge("A", "C"));
        assertTrue(g.hasEdge("C", "D"));
        assertTrue(g.hasEdge("C", "F"));
        assertTrue(g.hasEdge("D", "F"));
        assertFalse(g.hasEdge("F", "C"));
    }
}