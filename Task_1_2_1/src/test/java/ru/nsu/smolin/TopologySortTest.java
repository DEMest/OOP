package ru.nsu.smolin;

import ru.nsu.smolin.impl.TopologySort;

public class TopologySortTest {
    public static void main(String[] args) {
        testTopoSort();
    }

    static void testTopoSort() {
        Graph g = Factory.adjacentMatrix();
        g.addVertex("A"); g.addVertex("B"); g.addVertex("C");
        g.addEdge("A", "B");
        g.addEdge("B", "C");

        TopologySort sorter = new TopologySort();
        java.util.List<String> order = sorter.sort(g);

        assert order.size() == 3 : "Должно быть 3 вершины";
        assert order.indexOf("A") < order.indexOf("B") : "A должна идти раньше B";
        assert order.indexOf("B") < order.indexOf("C") : "B должна идти раньше C";

        System.out.println("ОК: " + order);
    }
}
