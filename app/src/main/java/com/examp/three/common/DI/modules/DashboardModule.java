package com.examp.three.common.DI.modules;


import com.examp.three.presenter.Grievances_admin.contracts.IdashboardInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DashboardModule {
    IdashboardInterface.view view;

    public DashboardModule(IdashboardInterface.view view) {
        this.view = view;
    }
    @Provides
    @Singleton
    IdashboardInterface.view provideView(){
        return view;
    }
}
