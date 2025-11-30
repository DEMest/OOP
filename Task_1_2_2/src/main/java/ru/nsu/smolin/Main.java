package ru.nsu.smolin;

import ru.nsu.smolin.impl.HashTableImpl;

public class Main {
    public static void main(String[] args) {
        HashTableImpl<String, Number> hashTable = new HashTableImpl<>();

        hashTable.put("one", 1);
        hashTable.update("one", 1.0);
        System.out.println(hashTable.get("one"));
        System.out.println(hashTable.toString());
    }
}