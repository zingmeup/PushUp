package com.example.zingme.pushup.appdata;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SP {

    public static SharedPreferences sp;
    public static SharedPreferences.Editor ed;
    static Context context;

    public SP(Context context) {
        SP.context = context;
        sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        ed = sp.edit();
        setDefaults();
    }

    private void setDefaults() {
        if (!sp.contains("playerName")) {
        }
        if (!sp.contains("firstTime")) {
            ed.putBoolean("firstTime", true).commit();
        }
        ;
        if (!sp.contains("dailyGoal")) {
            ed.putInt("dailyGoal", 50).commit();
        }
        ;
        if (!sp.contains("setSize")) {
            ed.putInt("setSize", 10).commit();
        }
        ;
        if (!sp.contains("tts123")) {
            ed.putBoolean("tts123", true).commit();
        }
        ;
        if (!sp.contains("ttsudu")) {
            ed.putBoolean("ttsudu", true).commit();
        }
        if (!sp.contains("ting")) {
            ed.putBoolean("ting", true).commit();
        }
        ;
        if (!sp.contains("color")) {
            ed.putBoolean("color", true).commit();
        }
        ;
        /*if (!sp.contains("dispTime")) {
            ed.putBoolean("dispTime", true).commit();
        }
        ;
        if (!sp.contains("dispCals")) {
            ed.putBoolean("dispCals", true).commit();
        }
        if (!sp.contains("dispPerc")) {
            ed.putBoolean("dispPerc", true).commit();
        }
        if (!sp.contains("dispComp")) {
            ed.putBoolean("dispComp", true).commit();
        }*/
        ;
        if (!sp.contains("setup")) {
            ed.putBoolean("setup", false).commit();
        }
        ;
        if (!sp.contains("cals")) {
            ed.putString("cals", "0").commit();
        }
        ;
        if (!sp.contains("time")) {
            ed.putInt("time", 0).commit();
        }
        ;
        if (!sp.contains("dailyCompleted")) {
            ed.putInt("dailyCompleted", 0).commit();
        }
        ;

        SimpleDateFormat df = new SimpleDateFormat("dd");
        Date c = Calendar.getInstance().getTime();
        String formattedDate = df.format(c);
        if (!sp.contains("date")) {
            ed.putString("date", formattedDate).commit();
        }else {
            if(!sp.getString("date","0").equals(formattedDate)){
                Toast.makeText(context, "It's a new Day", Toast.LENGTH_SHORT).show();
                ed.putInt("dailyCompleted", 0).commit();
                ed.putString("cals", "0").commit();
                ed.putInt("time", 0).commit();
                SP.ed.putString("date", formattedDate).commit();

            }
        }


        /* else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date lastDate= null;
            Date todayDate=new Date();
            Date currentdate=new Date();
            try {
                lastDate = sdf.parse(sp.getString("date",""));
                currentdate=sdf.parse(todayDate.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (currentdate.after(lastDate)){

            }else{

                Toast.makeText(context, "Let's Continue", Toast.LENGTH_SHORT).show();
            }
        }*/

        Log.e("DATE", new Date().toString());

    }

    public SharedPreferences getSp() {
        return sp;
    }
}
