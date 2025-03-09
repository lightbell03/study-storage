package org.example.chap10.domainmodel.transactionscript;

import org.example.common.MfDate;
import org.example.common.Money;
import org.example.config.ConnectionFactory;

import java.sql.*;

public class Gateway {

    private final Connection db;
    public Gateway() {
        this.db = ConnectionFactory.getInstance().getConnection();
    }
    private static final String findRecognitionStatement = """
            SELECT amount 
             FROM revenue_recognition
             WHERE contract = ? AND recognition_on <= ?
            """;

    public ResultSet findRecognitionFor(long contractID, MfDate asof) throws SQLException {
        PreparedStatement stmt = db.prepareStatement(findRecognitionStatement);
        stmt.setLong(1, contractID);;
        stmt.setDate(2, asof.toSqlDate());
        return stmt.executeQuery();
    }

    private static final String findContractStatement = """
            SELECT *
             FROM contracts c, products p
             WHERE id = ? AND c.product = p.id
            """;

    public ResultSet findContract(long contractId) throws SQLException {
        PreparedStatement stmt = db.prepareStatement(findContractStatement);
        stmt.setLong(1, contractId);
        return stmt.executeQuery();
    }

    private static final String insertRecognitionStatement = """
            INSERT INTO revenue_recognitions VALUES (?, ?, ?)
            """;

    public void insertRecognition(long contractId, Money amount, MfDate asof) throws SQLException {
        PreparedStatement stmt = db.prepareStatement(insertRecognitionStatement);
        stmt.setLong(1, contractId);
        stmt.setBigDecimal(2, amount.getAmount());
        stmt.setDate(3, asof.toSqlDate());
        stmt.executeQuery();
    }
}
