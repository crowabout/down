package com.company.net;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pc on 2017/7/18.
 */
public class DownerHttpClient {


    public static void main(String[] args) {


        Calendar c =Calendar.getInstance();
        c.setTimeInMillis(170803181653L);
        System.out.println(String.format("%s-%s-%s",c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)));

        System.out.println(System.currentTimeMillis());

    }


}
