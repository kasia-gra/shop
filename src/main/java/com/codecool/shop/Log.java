package com.codecool.shop;

import java.util.Date;

public class Log {
    private final Date date;
    private final LogType logType;
    private final boolean isSuccessful;

    public Log(Date date, LogType logType, boolean isSuccessful) {
        this.date = date;
        this.logType = logType;
        this.isSuccessful = isSuccessful;
    }

}
