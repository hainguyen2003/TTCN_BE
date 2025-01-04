package com.example.ttcn2etest.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyUtils {
    public static Date convertDateFromString(String dateStr, String dateTimeFormat) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimeFormat);
        simpleDateFormat.setLenient(false);
        return simpleDateFormat.parse(dateStr);
    }

    public static Date validateDateFromString(String dateStr, String dateTimeFormat) throws ParseException {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimeFormat);
            simpleDateFormat.setLenient(false);
            return simpleDateFormat.parse(dateStr);
        }catch (ParseException e){
            return null;
        }
    }

    public static String convertDateToString(Date inputDate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(inputDate);
    }
}
