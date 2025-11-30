package ru.nsu.smolin;

import org.junit.jupiter.api.Test;
import ru.nsu.smolin.impl.Adjacent;
import ru.nsu.smolin.impl.AdjacentList;
import ru.nsu.smolin.impl.Incidence;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

    private void testGraph(Graph g, String name) {
        System.out.println("Тестирование " + name);

        assertTrue(g.addVertex("A"), "addVertex fail");
        assertTrue(g.addVertex("B"), "addVertex fail");
        assertFalse(g.addVertex("A"), "addVertex duplicate fail");

        assertTrue(g.hasVertex("A"), "hasVertex fail A");
        assertFalse(g.hasVertex("Z"), "hasVertex fail Z");

        assertTrue(g.addEdge("A", "B"), "addEdge fail");
        assertTrue(g.hasEdge("A", "B"), "hasEdge fail");
        assertFalse(g.hasEdge("A", "Z"), "hasEdge non-existent fail");

        assertEquals(1, g.neighbourVertecies("A").size(), "neighbour size fail");

        assertTrue(g.getVerticies().contains("A"), "getVerticies fail");
        assertTrue(g.getVerticies().contains("B"), "getVerticies fail");

        assertTrue(g.deleteEdge("A", "B"), "deleteEdge fail");
        assertFalse(g.hasEdge("A", "B"), "hasEdge after delete fail");

        assertTrue(g.deleteVertex("A"), "deleteVertex fail");
        assertFalse(g.hasVertex("A"), "hasVertex after delete fail");

        System.out.println("ОК (" + name + ")\n");
    }

    @Test
    void testAdjacent() {
        Graph g = new Adjacent();
        testGraph(g, "Adjacent");
    }

    @Test
    void testAdjacentList() {
        Graph g = new AdjacentList();
        testGraph(g, "AdjacentList");
    }

    @Test
    void testIncidence() {
        Graph g = new Incidence();
        testGraph(g, "Incidence");
    }

}
