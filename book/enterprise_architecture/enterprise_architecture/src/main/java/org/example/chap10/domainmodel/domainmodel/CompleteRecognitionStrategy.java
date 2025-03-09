package org.example.chap10.domainmodel.domainmodel;

public class CompleteRecognitionStrategy extends RecognitionStrategy {
    @Override
    void calculateRevenueRecognitions(Contract contract) {
        contract.addRevenueRecognition(new RevenueRecognition(contract.getRevenue(), contract.getWhenSigned()));
    }
}
