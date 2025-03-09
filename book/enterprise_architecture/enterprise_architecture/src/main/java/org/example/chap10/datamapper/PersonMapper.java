package org.example.chap10.datamapper;

import org.example.config.ConnectionFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

    private static final String findLastNameStatement =
            "SELECT " + COLUMNS +
                    " FROM people " +
                    " WHERE UPPER(lastname) like UPPER(?) " +
                    " ORDER BY lastname";

    public List<Person> findByLastName(String name) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = ConnectionFactory.getInstance().prepare(findLastNameStatement);
            stmt.setString(1, name);
            rs = stmt.executeQuery();
            return loadAll(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
