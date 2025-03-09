package org.example.chap10.datamapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonMapper extends AbstractMapper<Long, Person> {
    private static final String COLUMNS = " id, lastname, firstname, number_of_dependents";

    protected String findStatement() {
        return "SELECT " + COLUMNS +
                " FROM people" +
                " WHERE id = ?";
    }

    public Person find(Long id) {
        return abstractFind(id);
    }

    @Override
    protected Person doLoad(Long id, ResultSet rs) throws SQLException {
        String lastName = rs.getString(2);
        String firstName = rs.getString(3);
        int numDependents = rs.getInt(4);
        return new Person(lastName, firstName, numDependents);
    }
}
