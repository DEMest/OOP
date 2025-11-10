package ru.nsu.smolin;

public class GraphTest {
    public static void main(String[] args) {
        testGraph("Adjacent", Factory::adjacentMatrix);
        testGraph("Incidence", Factory::incidence);
        testGraph("AdjacentList", Factory::adjacentList);
    }

    static void testGraph(String name, GraphSupplier supplier) {
        Graph g = supplier.get();
        System.out.println("Тестирование " + name);

        assert g.addVertex("A") : "addVertex fail";
        assert g.addVertex("B") : "addVertex fail";
        assert !g.addVertex("A") : "addVertex duplicate fail";
        assert g.hasVertex("A") : "hasVertex fail A";
        assert !g.hasVertex("Z") : "hasVertex fail Z";

        assert g.addEdge("A", "B") : "addEdge fail";
        assert g.hasEdge("A", "B") : "hasEdge fail";
        assert g.hasEdge("B", "A") : "hasEdge symmetry fail";
        assert !g.hasEdge("A", "Z") : "hasEdge non-existent fail";

        assert g.neighbourVertecies("A").contains("B") : "neighbour fail";
        assert g.neighbourVertecies("B").contains("A") : "neighbour fail";
        assert g.neighbourVertecies("A").size() == 1 : "neighbour size fail";

        assert g.getVerticies().contains("A") : "getVerticies fail";
        assert g.getVerticies().contains("B") : "getVerticies fail";

        assert g.deleteEdge("A", "B") : "deleteEdge fail";
        assert !g.hasEdge("A", "B") : "hasEdge after delete fail";

        assert g.deleteVertex("A") : "deleteVertex fail";
        assert !g.hasVertex("A") : "hasVertex after delete fail";

        System.out.println("ОК (" + name + ")\n");
    }
}
