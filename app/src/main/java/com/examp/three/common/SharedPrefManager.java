package com.examp.three.common;

import android.content.SharedPreferences;

import com.examp.three.common.DI.modules.AppModule;
import com.examp.three.model.Home.MoveToCommanList;
import com.google.gson.Gson;

import dagger.Module;

@Module(includes = AppModule.class)
public class SharedPrefManager {

    public void storeDatas(SharedPreferences sharedPreferences, MoveToCommanList moveToCommanList, String objectname){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(moveToCommanList);
        editor.putString(objectname,json);
        editor.commit();
    }
    public MoveToCommanList getMovetoCommanListObjects(SharedPreferences sharedPreferences, String objectname){
        Gson gson = new Gson();
        return gson.fromJson(sharedPreferences.getString(objectname,null),MoveToCommanList.class);
    }

}
