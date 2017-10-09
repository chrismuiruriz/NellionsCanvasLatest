package com.nellions.nellionscanvas.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Chris Muiruri on 2/6/2016.
 */
public class Util {

    public static String USER_PREF = "USER_DETAILS";

    //user details
    public static String USER_ID = "id_user";
    public static String U_NAME = "name";
    public static String USER_EMAIL = "email";
    public static String USER_NAME = "username";

    //moves details
    public static String MOVES_ID = "moveid";

    public static String round(String value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        //convert string to double
        Double dValue = Double.parseDouble(value);
        BigDecimal bigDecimal = new BigDecimal(dValue);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return String.valueOf(bigDecimal.doubleValue());
    }

}
