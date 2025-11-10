package ru.nsu.smolin.impl;

import ru.nsu.smolin.Graph;
import java.util.*;

/**
 * Реализация представления ориентированного графа матрицей инцидентности.
 * Строки — вершины, столбцы — дуги. Для дуги (u -> v): row[u][e] = +1, row[v][e] = -1.
 */
public class Incidence implements Graph {
    private final Map<String, Map<String, Integer>> matrix = new LinkedHashMap<>();
    private final Map<String, Edge> edges = new LinkedHashMap<>();
    private final Map<String, String> pairToEdgeId = new HashMap<>();
    private int edgeSeq = 0;

    @Override
    public boolean addVertex(String name) {
        if (matrix.containsKey(name)) {
            return false;
        }
        Map<String, Integer> row = new LinkedHashMap<>();
        for (String eId : edges.keySet()) {
            row.put(eId, 0);
        }
        matrix.put(name, row);
        return true;
    }

    @Override
    public boolean deleteVertex(String name) {
        Map<String, Integer> row = matrix.remove(name);
        if (row == null) {
            return false;
        }
        List<String> incident = new ArrayList<>();
        for (Map.Entry<String, Integer> e : row.entrySet()) {
            if (e.getValue() != 0) {
                incident.add(e.getKey());
            }
        }
        for (String eId : incident) {
            Edge e = edges.remove(eId);
            if (e != null) {
                pairToEdgeId.remove(key(e.u, e.v));
                for (Map<String, Integer> r : matrix.values()) {
                    r.remove(eId);
                }
            }
        }
        return true;
    }

    @Override
    public boolean addEdge(String v1, String v2) {
        if (!matrix.containsKey(v1) || !matrix.containsKey(v2)) {
            return false;
        }
        if (v1.equals(v2)) {
            return false;
        }

        String dirKey = key(v1, v2);
        if (pairToEdgeId.containsKey(dirKey)) {
            return false;
        }

        String eId = "e" + (edgeSeq++);
        edges.put(eId, new Edge(v1, v2));
        pairToEdgeId.put(dirKey, eId);

        for (Map.Entry<String, Map<String, Integer>> ent : matrix.entrySet()) {
            String v = ent.getKey();
            Map<String, Integer> r = ent.getValue();
            int val = 0;
            if (v.equals(v1)) {
                val += 1;
            }
            else if (v.equals(v2)) {
                val -= 1;
            }
            r.put(eId, val);
        }
        return true;
    }

    @Override
    public boolean deleteEdge(String v1, String v2) {
        String eId = pairToEdgeId.remove(key(v1, v2));
        if (eId == null) return false;
        edges.remove(eId);
        for (Map<String, Integer> r : matrix.values()) {
            r.remove(eId);
        }
        return true;
    }

    @Override
    public List<String> neighbourVertecies(String name) {
        Map<String, Integer> row = matrix.get(name);
        if (row == null) return Collections.emptyList();
        Set<String> res = new LinkedHashSet<>();
        for (Map.Entry<String, Integer> e : row.entrySet()) {
            if (e.getValue() == 1) {
                Edge edge = edges.get(e.getKey());
                if (edge != null) {
                    res.add(edge.v);
                }
            }
        }
        return new ArrayList<>(res);
    }

    @Override
    public Set<String> getVerticies() {
        return Collections.unmodifiableSet(matrix.keySet());
    }

    @Override
    public boolean hasVertex(String name) {
        return matrix.containsKey(name);
    }

    @Override
    public boolean hasEdge(String v1, String v2) {
        return pairToEdgeId.containsKey(key(v1, v2));
    }

    @Override
    public String toString() {
        List<String> vs = new ArrayList<>(matrix.keySet());
        List<String> es = new ArrayList<>(edges.keySet());
        StringBuilder sb = new StringBuilder();
        sb.append("    ");
        for (String e : es) {
            sb.append(e).append(' ');
        }
        sb.append('\n');
        for (String v : vs) {
            sb.append(v).append(": ");
            Map<String, Integer> row = matrix.getOrDefault(v, Collections.emptyMap());
            for (String e : es) {
                int inc = row.getOrDefault(e, 0);
                String cell = (inc == -1) ? "-1" : (inc == 1 ? " 1" : " 0");
                sb.append(cell).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Incidence incidence)) {
            return false;
        }
        return edgeSeq == incidence.edgeSeq
                && Objects.equals(matrix, incidence.matrix)
                && Objects.equals(edges, incidence.edges)
                && Objects.equals(pairToEdgeId, incidence.pairToEdgeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matrix, edges, pairToEdgeId, edgeSeq);
    }

    private static String key(String a, String b) {
        return a + "|" + b;
    }

    private static final class Edge {
        final String u, v;
        Edge(String u, String v) {
            this.u = u; this.v = v;
        }
    }
}