package org.example.domainmodel.transactionscript;

public class Application {
    public Gateway gateway() {
        return new Gateway();
    }

    public RecognitionService recognitionService() {
        return new RecognitionService(gateway());
    }
}
