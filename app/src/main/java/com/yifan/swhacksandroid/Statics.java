package com.yifan.swhacksandroid;

import java.util.Calendar;

/**
 * Created by Yifan on 3/10/2017.
 */

public class Statics {
    public static String getTimeStampForCalanderObj(Calendar calendar){ //gets timestamp ex)5:00PM
        String minutesString;
        if(calendar.get(Calendar.MINUTE) < 10){
            minutesString = "0" + calendar.get(Calendar.MINUTE);
        }
        else{
            minutesString = String.valueOf(calendar.get(Calendar.MINUTE));
        }
        if(calendar.get(Calendar.AM_PM) == Calendar.AM){ //this is fine, don't know why A.S flags this
            return String.valueOf(calendar.get(Calendar.HOUR) + ":" + minutesString + "AM");
        }
        else{
            return String.valueOf(calendar.get(Calendar.HOUR) + ":" + minutesString + "PM");
        }

    }
}
