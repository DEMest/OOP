package ru.nsu.smolin.impl;

import ru.nsu.smolin.Graph;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Collections;
import java.util.Objects;

/**
 * Реализация представления ориентированного графа матрицей смежности.
 * Строка i — исходящие дуги из i, столбец j — входящие дуги в j.
 */
public class Adjacent implements Graph {
    private final Map<String, Map<String, Boolean>> matrix = new HashMap<>();

    @Override
    public boolean addVertex(String name) {
        if (matrix.containsKey(name)) {
            return false;
        }
        Map<String, Boolean> line = new HashMap<>();
        for (String k : matrix.keySet()) {
            line.put(k, Boolean.FALSE);
        }
        for (Map<String, Boolean> row : matrix.values()) {
            row.put(name, Boolean.FALSE);
        }
        line.put(name, Boolean.FALSE);
        matrix.put(name, line);
        return true;
    }

    @Override
    public boolean deleteVertex(String name) {
        if (!matrix.containsKey(name)) {
            return false;
        }
        for (Map<String, Boolean> row : matrix.values()) {
            row.remove(name);
        }
        matrix.remove(name);
        return true;
    }

    @Override
    public boolean addEdge(String vertex1, String vertex2) {
        if (!matrix.containsKey(vertex1) || !matrix.containsKey(vertex2)) {
            return false;
        }
        matrix.get(vertex1).put(vertex2, Boolean.TRUE);
        return true;
    }

    @Override
    public boolean deleteEdge(String vertex1, String vertex2) {
        if (!matrix.containsKey(vertex1) || !matrix.containsKey(vertex2)) {
            return false;
        }
        matrix.get(vertex1).put(vertex2, Boolean.FALSE);
        return true;
    }

    @Override
    public List<String> neighbourVertecies(String name) {
        Map<String, Boolean> row = matrix.get(name);
        if (row == null) {
            return Collections.emptyList();
        }
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Boolean> e : row.entrySet()) {
            if (Boolean.TRUE.equals(e.getValue())) {
                result.add(e.getKey());
            }
        }
        return result;
    }

    @Override
    public Set<String> getVerticies() {
        return matrix.keySet();
    }

    @Override
    public boolean hasVertex(String name) {
        return matrix.containsKey(name);
    }

    @Override
    public boolean hasEdge(String v1, String v2) {
        Map<String, Boolean> row = matrix.get(v1);
        return row != null && Boolean.TRUE.equals(row.get(v2));
    }

    @Override
    public String toString() {
        List<String> vs = new ArrayList<>(matrix.keySet());
        Collections.sort(vs);
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        for (String v : vs) {
            sb.append(v).append(' ');
        }
        sb.append('\n');
        for (String r : vs) {
            sb.append(r).append(' ');
            Map<String, Boolean> row = matrix.getOrDefault(r, Collections.emptyMap());
            for (String c : vs) {
                boolean edge = Boolean.TRUE.equals(row.get(c));
                sb.append(edge ? "1 " : "0 ");
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Adjacent adjacent)) return false;
        return Objects.equals(matrix, adjacent.matrix);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(matrix);
    }
}
