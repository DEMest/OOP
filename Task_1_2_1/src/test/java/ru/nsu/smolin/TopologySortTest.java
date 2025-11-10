package ru.nsu.smolin;

import ru.nsu.smolin.impl.TopologySort;
import org.junit.jupiter.api.Test;
import ru.nsu.smolin.impl.Adjacent;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TopologySortTest {

    @Test
    void simpleTopoSort_adjacent() {
        Graph g = new Adjacent();
        g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");

        g.addEdge("A", "B");
        g.addEdge("B", "C");

        TopologySort sorter = new TopologySort();
        List<String> order = sorter.sort(g);

        assertEquals(3, order.size(), "Должно быть 3 вершины");
        assertTrue(order.indexOf("A") < order.indexOf("B"),
                "A должна идти раньше B");
        assertTrue(order.indexOf("B") < order.indexOf("C"),
                "B должна идти раньше C");

        System.out.println("ОК: " + order);
    }

}