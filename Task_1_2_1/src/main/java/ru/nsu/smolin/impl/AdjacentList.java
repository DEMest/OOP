package ru.nsu.smolin.impl;

import ru.nsu.smolin.Graph;
import java.util.*;

/**
 * Реализация представления ориентированного графа списком смежности.
 * Для вершины v хранятся только исходящие соседи (куда идут дуги v -> u).
 */
public class AdjacentList implements Graph {
    private final Map<String, Set<String>> adj = new LinkedHashMap<>();

    @Override
    public boolean addVertex(String name) {
        if (adj.containsKey(name)) return false;
        adj.put(name, new LinkedHashSet<>());
        return true;
    }

    @Override
    public boolean deleteVertex(String name) {
        Set<String> removed = adj.remove(name);
        if (removed == null) {
            return false;
        }
        for (Set<String> s : adj.values()) {
            s.remove(name);
        }
        return true;
    }

    @Override
    public boolean addEdge(String v1, String v2) {
        if (!adj.containsKey(v1) || !adj.containsKey(v2)) return false;
        Set<String> out = adj.get(v1);
        boolean existed = out.contains(v2);
        out.add(v2);
        return !existed;
    }

    @Override
    public boolean deleteEdge(String v1, String v2) {
        if (!adj.containsKey(v1) || !adj.containsKey(v2)) {
            return false;
        }
        return adj.get(v1).remove(v2);
    }

    @Override
    public List<String> neighbourVertecies(String name) {
        Set<String> s = adj.get(name);
        if (s == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(s);
    }

    @Override
    public Set<String> getVerticies() {
        return adj.keySet();
    }

    @Override
    public boolean hasVertex(String name) {
        return adj.containsKey(name);
    }

    @Override
    public boolean hasEdge(String v1, String v2) {
        Set<String> s = adj.get(v1);
        return s != null && s.contains(v2);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (var e : adj.entrySet()) {
            sb.append(e.getKey()).append(": ");
            for (String n : e.getValue()) {
                sb.append(n).append(' ');
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
        if (!(o instanceof AdjacentList that)) {
            return false;
        }
        return Objects.equals(adj, that.adj);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(adj);
    }
}