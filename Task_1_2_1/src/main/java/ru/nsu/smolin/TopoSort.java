package ru.nsu.smolin;

import java.util.List;

/**
 * Интейрфейс для реализации топологической сортировки.
 *
 */
public interface TopoSort {
    List<String> sort(Graph g);
}
