package org.example.domainmodel.domainmodel;

import org.example.common.Money;

public class ThreeWayRecognitionStrategy extends RecognitionStrategy {
    private int firstRecognitionOffset;
    private int secondRecognitionOffset;

    public ThreeWayRecognitionStrategy(int firstRecognitionOffset, int secondRecognitionOffset) {
        this.firstRecognitionOffset = firstRecognitionOffset;
        this.secondRecognitionOffset = secondRecognitionOffset;
    }

    @Override
    void calculateRevenueRecognitions(Contract contract) {
        Money[] allocation = contract.getRevenue().allocate(3);
        contract.addRevenueRecognition(new RevenueRecognition(allocation[0], contract.getWhenSigned()));
        contract.addRevenueRecognition(new RevenueRecognition(allocation[0], contract.getWhenSigned().allDays(firstRecognitionOffset)));
        contract.addRevenueRecognition(new RevenueRecognition(allocation[0], contract.getWhenSigned().allDays(secondRecognitionOffset)));
    }
}
