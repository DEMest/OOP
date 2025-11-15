package ru.nsu.smolin;

import java.util.List;
import java.util.Set;

/**
 * Интерфейс для реализации представления графов
 * <h2>Для пользователей</h2>
 * Готовые реализации - {@link ru.nsu.smolin.impl.Adjacent},
 * {@link ru.nsu.smolin.impl.AdjacentList}, {@link ru.nsu.smolin.impl.Incidence}.
 * Для создания:
 * <pre>
 * {@code
 * Graph g1 = new Adjacent();     // Создает матрицу смежности
 * Graph g2 = new AdjacentList(); // Создает список смежности
 * Graph g3 = new Incidence();    // Создает матрицу инцидентности
 * g1.addVertex("A"); // добавление вершины
 * g1.addVertex("B");
 * g1.addEdge("A", "B"); // добавление ребра
 * }
 * </pre>
 * <h2>Для разработчика</h2>
 * Для добавления новой реализации хранения графа необходимо:
 * <ul>
 *   <li>Создать новый класс, реализующий интерфейс Graph.</li>
 *   <li>Реализовать все методы интерфейса:
 *       <ul>
 *         <li><b>addVertex</b> — добавление вершины в структуру хранения.</li>
 *         <li><b>deleteVertex</b> — удаление вершины и связанных с ней рёбер.</li>
 *         <li><b>addEdge</b> — добавление ребра между двумя вершинами.</li>
 *         <li><b>deleteEdge</b> — удаление ребра между двумя вершинами.</li>
 *         <li><b>neighbourVertecies</b> — получение списка всех соседей заданной вершины.</li>
 *         <li><b>getVerticies</b> — получение множества всех вершин графа.</li>
 *         <li><b>hasVertex</b> — проверка наличия вершины в графе.</li>
 *         <li><b>hasEdge</b> — проверка наличия ребра между двумя вершинами.</li>
 *         <li><b>equals</b> — сравнение графа с объектом (эквивалентность структур
 *         любых реализаций Graph по множеству вершин и рёбер).</li>
 *         <li><b>hashCode</b> — согласованная реализация с equals, чтобы графы с
 *         одинаковым набором вершин и рёбер имели одинаковый hashCode независимо
 *         от внутренней структуры хранения.</li>
 *       </ul>
 *   </li>
 *   <li>Реализовать методы <b>equals</b> и <b>toString</b> для корректного сравнения и вывода графа.</li>
 *   <li>Выбор внутренней структуры хранения (матрица смежности, список смежности, матрица инцидентности или иной способ) осуществляется по требуемой логике.</li>
 *   <li>После реализации все алгоритмы и вспомогательные классы, работающие с интерфейсом Graph, становятся совместимыми с новой реализацией.</li>
 * </ul>
 */
public interface Graph {
    boolean addVertex(String name);
    boolean deleteVertex(String name);

    boolean addEdge(String vertex1, String vertex2);
    boolean deleteEdge(String vertex1, String vertex2);

    List<String> neighbourVertecies(String name);
    Set<String> getVerticies();

    boolean hasVertex(String name);
    boolean hasEdge(String vertex1, String vertex2);
}
