package ru.nsu.smolin.impl;

import ru.nsu.smolin.Graph;

import java.util.*;

/**
 * Реализация алгоритма топологической сортировки для ориентированного ацикличного графа.
 * Для каждой вершины вычисляет количесто входящих рёбер (indegree), после чего формирует порядок,
 * при котором каждое ребро идёт от более ранней вершины к более поздней.
 * Если граф содержит цикл, возвращённый порядок будет содержать не все вершины.
 *
 */
public class TopologySort {
    public List<String> sort(Graph g) {
        Map<String, Integer> indegree = new HashMap<>();
        for (String v : g.getVerticies()) {
            indegree.put(v, 0);
        }
        for (String v : g.getVerticies()) {
            for (String n : g.neighbourVertecies(v)) {
                indegree.put(n, indegree.get(n) + 1);
            }
        }
        Queue<String> q = new ArrayDeque<>();
        for (String v : indegree.keySet()) {
            if (indegree.get(v) == 0) {
                q.add(v);
            }
        }
        List<String> answer = new ArrayList<>();
        while (!q.isEmpty()) {
            String v = q.remove();
            answer.add(v);
            for (String n : g.neighbourVertecies(v)) {
                indegree.put(n, indegree.get(n) - 1);
                if (indegree.get(n) == 0) {
                    q.add(n);
                }
            }
        }
        return answer;
    }
}
