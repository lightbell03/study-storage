package org.example.chap10.datamapper;

@Table(name = "person")
public class Person {
    @Id
    private Long id;
    private String lastname;
    private String firstname;
    private int numberOfDependents;

    public Person(String lastname, String firstname, int numberOfDependents) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.numberOfDependents = numberOfDependents;
    }

    public Long getId() {
        return id;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public int getNumberOfDependents() {
        return numberOfDependents;
    }
}
