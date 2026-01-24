package com.example.app.Helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveState {

    Context context;
    String saveName;
    SharedPreferences sp;

    public SaveState(Context context, String saveName) {
        this.context = context;
        this.saveName = saveName;
        sp = context.getSharedPreferences(saveName, Context.MODE_PRIVATE);
    }

    public void setState(int value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("state", value);
        editor.apply();
    }

    public int getState() {
        return sp.getInt("state", 0);
    }

}
