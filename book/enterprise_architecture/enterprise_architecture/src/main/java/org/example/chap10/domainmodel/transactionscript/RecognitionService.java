package org.example.chap10.domainmodel.transactionscript;

import org.example.common.ApplicationException;
import org.example.common.MfDate;
import org.example.common.Money;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RecognitionService {
    private final Gateway db;
    public RecognitionService(Gateway db) {
        this.db = db;
    }

    public Money recognizedRevenue(long contractNumber, MfDate asOf) {
        try (ResultSet rs = db.findRecognitionFor(contractNumber, asOf)) {
            Money result = Money.dollars(new BigDecimal(0));
            while (rs.next()) {
                result = result.add(Money.dollars(rs.getBigDecimal("amount")));
            }

            return result;
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    public void calculateRevenueRecognition(long contractNumber) {
        try (ResultSet contracts = db.findContract(contractNumber)) {
            contracts.next();
            Money totalRevenue = Money.dollars(contracts.getBigDecimal("revenue"));
            MfDate recognitionDate = new MfDate(contracts.getDate("date_signed"));
            String type = contracts.getString("type");
            if (type.equals("S")) {
                Money[] allocate = totalRevenue.allocate(3);
                db.insertRecognition(contractNumber, allocate[0], recognitionDate);
                db.insertRecognition(contractNumber, allocate[1], recognitionDate.allDays(30));
                db.insertRecognition(contractNumber, allocate[1], recognitionDate.allDays(90));
            } else if (type.equals("W")) {
                db.insertRecognition(contractNumber, totalRevenue, recognitionDate);
            } else if (type.equals("D")) {
                Money[] allocation = totalRevenue.allocate(3);
                db.insertRecognition(contractNumber, allocation[0], recognitionDate);
                db.insertRecognition(contractNumber, allocation[1], recognitionDate.allDays(30));
                db.insertRecognition(contractNumber, allocation[2], recognitionDate.allDays(60));
            }
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }
}
