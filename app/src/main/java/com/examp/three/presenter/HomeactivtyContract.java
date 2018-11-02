package com.examp.three.presenter;

import android.content.SharedPreferences;

import com.examp.three.common.SharedPrefManager;
import com.examp.three.model.Home.HomeBean;
import com.examp.three.model.Home.MoveToCommanList;

import java.util.List;


public interface HomeactivtyContract {
    interface view{
        void returningCommonList(List<HomeBean> commanList);
    }
    interface presenter{
        void getCommonList(List<HomeBean> commanList);
        void storeObject(SharedPreferences sharedPreferences, SharedPrefManager sharedPrefManager, MoveToCommanList moveToCommanList, String objectName);
        MoveToCommanList getObject(SharedPreferences sharedPreferences, SharedPrefManager sharedPrefManager, String objectName);
        void checkCommanListWithMovelist(SharedPreferences sharedPreferences, SharedPrefManager sharedPrefManager);
    }
}
