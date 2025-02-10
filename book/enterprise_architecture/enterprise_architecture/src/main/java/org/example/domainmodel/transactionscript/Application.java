package org.example.domainmodel.transactionscript;

import org.example.config.ConnectionFactory;

public class Application {
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost:3306/enterprise_arch";
    private static final String user = "user01";
    private static final String password = "test01!";

    public void connectionInit() {
        ConnectionFactory instance = ConnectionFactory.getInstance();
        instance.setDriver(driver);
        instance.setUrl(url);
        instance.setUser(user);
        instance.setPassword(password);
    }

    private Gateway gateway() {
        return new Gateway();
    }

    public RecognitionService recognitionService() {
        return new RecognitionService(gateway());
    }
}
