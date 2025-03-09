package org.example.chap10.datamapper;

public class Person {
    private String lastname;
    private String firstname;
    private int numberOfDependents;

    public Person(String lastname, String firstname, int numberOfDependents) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.numberOfDependents = numberOfDependents;
    }
}
