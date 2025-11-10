package ru.nsu.smolin;

import ru.nsu.smolin.impl.Adjacent;
import ru.nsu.smolin.impl.AdjacentList;
import ru.nsu.smolin.impl.Incidence;

/**
 * Фабричныйы простейший класс для создания объектов
 * <p>
 * Создает объекты приобращении:
 * {@code Graph g = Factory.adjacentMatrix();}
 * </p>
 * После можно работать с g как с экземпляром класса Adjacent.
 * Поддерживается Adjacent, AdjacentList и Incidence.
 * Для добавления нового метода фабрики, необходимо написать класс реализации
 * графа и вернуть его в новом методе здесь в виде {@code return new yourimpl();}
 */
public final class Factory {
    public static Graph adjacentMatrix() {
        return new Adjacent();
    }
    public static Graph adjacentList() {
        return new AdjacentList();
    }
    public static Graph incidence() {
        return new Incidence();
    }
}
