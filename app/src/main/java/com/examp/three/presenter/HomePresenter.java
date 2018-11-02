package com.examp.three.presenter;

import android.content.SharedPreferences;
import android.util.Log;

import com.examp.three.R;
import com.examp.three.common.SharedPrefManager;
import com.examp.three.model.Home.HomeBean;
import com.examp.three.model.Home.MoveToCommanList;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class HomePresenter implements HomeactivtyContract.presenter {
HomeactivtyContract.view view;
int totalMoreItemsList=6;

@Inject
public HomePresenter(HomeactivtyContract.view view) {
        this.view = view;
    }

    @Override
    public void getCommonList(List<HomeBean> commanList) {
        List<HomeBean> homeBeanList = new ArrayList<>();
        homeBeanList.add(new HomeBean("Property Tax", R.drawable.nproperty));
        homeBeanList.add(new HomeBean("Profession Tax",R.drawable.nprofessional));
        homeBeanList.add(new HomeBean("Water Tax",R.drawable.nwater));
        homeBeanList.add(new HomeBean("Non Tax",R.drawable.nnontax));
        if(commanList.size()>0){
            homeBeanList.addAll(commanList);
        }
        view.returningCommonList(homeBeanList);
    }

    @Override
    public void storeObject(SharedPreferences sharedPreferences, SharedPrefManager sharedPrefManager, MoveToCommanList moveToCommanList, String objectName) {
        sharedPrefManager.storeDatas(sharedPreferences,moveToCommanList,objectName);
    }

    @Override
    public MoveToCommanList getObject(SharedPreferences sharedPreferences, SharedPrefManager sharedPrefManager, String objectName) {
        return sharedPrefManager.getMovetoCommanListObjects(sharedPreferences,objectName);
    }

    @Override
    public void checkCommanListWithMovelist(SharedPreferences sharedPreferences, SharedPrefManager sharedPrefManager) {
        List<HomeBean> homeBeanList = new ArrayList<>();
        for(int i=1;i<totalMoreItemsList;i++){
            MoveToCommanList moveToCommanList1 = sharedPrefManager.getMovetoCommanListObjects(sharedPreferences,"UserIcon~"+i);

            if(moveToCommanList1!=null){
                switch (i){
                    case 1:
                        if(moveToCommanList1.getCount()>=3){
                            homeBeanList.add(new HomeBean("Birth Details",R.drawable.nbirth));
                        }
                        break;
                    case 2:
                        if(moveToCommanList1.getCount()>=3){
                            homeBeanList.add(new HomeBean("Death Details",R.drawable.ndeath));
                        }
                        break;
                    case 3:
                        if(moveToCommanList1.getCount()>=3){
                            homeBeanList.add(new HomeBean("Trade License",R.drawable.ntradelicence));
                        }
                        break;
                    case 4:
                        if(moveToCommanList1.getCount()>=3){
                            homeBeanList.add(new HomeBean("Greivance",R.drawable.ngrievances));
                        }
                        break;
                    case 5:
                        if(moveToCommanList1.getCount()>=3){
                            homeBeanList.add(new HomeBean("Building Plan",R.drawable.nbuildingplan));
                        }
                        break;
                }
            }

        }
        getCommonList(homeBeanList);
    }

}
