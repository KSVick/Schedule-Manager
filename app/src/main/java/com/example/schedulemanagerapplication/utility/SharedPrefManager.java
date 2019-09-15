package com.example.schedulemanagerapplication.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    public static final String SP_SCHEDULE_APP = "spScheduleApp";

    public static final String SP_USER_KEY = "spUserKey";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;
    private static SharedPrefManager sharedPrefManager = null;


    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(SP_SCHEDULE_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public void removeSP(String keySP){
        spEditor.remove(keySP);
        spEditor.commit();
    }

    public void clearSP(){
        spEditor.clear();
        spEditor.commit();
    }

    public String getSPUserKey(){
        return sp.getString(SP_USER_KEY, "");
    }
}
