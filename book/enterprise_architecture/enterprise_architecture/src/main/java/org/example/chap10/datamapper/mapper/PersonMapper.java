package org.example.chap10.datamapper.mapper;

import org.example.chap10.datamapper.entity.Person;
import org.example.chap10.datamapper.StatementSource;
import org.example.common.ApplicationException;
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

    private static final String updateStatementString =
            "UPDATE people " +
                    " SET lastname = ?, firstname = ?, number_of_dependents = ? " +
                    " WHERE id = ?";

    public void update(Person subject) {
        PreparedStatement updateStatement = null;
        try {
            updateStatement = ConnectionFactory.getInstance().prepare(updateStatementString);
            updateStatement.setString(1, subject.getLastname());
            updateStatement.setString(2, subject.getFirstname());
            updateStatement.setInt(3, subject.getNumberOfDependents());
            updateStatement.setInt(4, subject.getId().intValue());
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    protected String insertStatement() {
        return "INSERT INTO people VALUES (? ,?, ?, ?)";
    }

    @Override
    protected void doInsert(Person subject, PreparedStatement insertStatement) throws SQLException {
        insertStatement.setString(2, subject.getLastname());
        insertStatement.setString(3, subject.getFirstname());
        insertStatement.setInt(4, subject.getNumberOfDependents());
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

    public List<Person> findByLastNameUsingStatementSource(String pattern) {
        return findMany(new FindByLastName(pattern));
    }

    static class FindByLastName implements StatementSource {
        private final String lastname;

        public FindByLastName(String lastname) {
            this.lastname = lastname;
        }

        @Override
        public String sql() {
            return "SELECT " + COLUMNS +
                    " FROM people " +
                    " WHERE UPPER(lastname) like UPPER(?) " +
                    " ORDER BY lastname";
        }

        @Override
        public Object[] parameters() {
            Object[] result = {lastname};
            return result;
        }
    }
}
