package org.example.common;

import java.util.Calendar;
import java.util.Date;

public class MfDate {
    private final Date asof;

    public MfDate(Date asof) {
        this.asof = asof;
    }

    public MfDate(java.sql.Date asof) {
        this.asof = new Date(asof.getTime());
    }

    public java.sql.Date toSqlDate() {
        return new java.sql.Date(asof.getTime());
    }

    public MfDate allDays(int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(asof);
        c.add(Calendar.DATE, days);
        return new MfDate(c.getTime());
    }

    public boolean after(MfDate date) {
        return this.asof.after(date.asof);
    }
}
