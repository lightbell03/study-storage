package org.example.domainmodel.transactionscript;

import org.example.common.MfDate;
import org.example.common.Money;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Test {
    public static void main(String[] args) {
        Application application = new Application();
        application.connectionInit();

        RecognitionService recognitionService = application.recognitionService();
        Calendar c = new GregorianCalendar();
        c.set(2024, 1, 1);
        Money money = recognitionService.recognizedRevenue(1, new MfDate(c.getTime()));

        System.out.println(money.getAmount());
    }
}
