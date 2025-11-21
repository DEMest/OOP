package ru.nsu.smolin;

import ru.nsu.smolin.impl.HashTable;

public class Main {
    public static void main(String[] args) {
        HashTable<String, Number> hashTable = new HashTable<>();

        hashTable.put("one", 1);
        hashTable.update("one", 1.0);
        System.out.println(hashTable.get("one"));
        System.out.println(hashTable.toString());
    }
}