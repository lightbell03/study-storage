package org.example.chap11.uniquemap;

import java.util.HashMap;
import java.util.Map;

public class UniqueMap {
    private Map<Long, Person> people = new HashMap<>();

    private static UniqueMap soleInstance = new UniqueMap();

    public static void addPerson(Person person) {
        soleInstance.people.put(person.getId(), person);
    }

    public static Person getPerson(long id) {
        return soleInstance.people.get(id);
    }
}
