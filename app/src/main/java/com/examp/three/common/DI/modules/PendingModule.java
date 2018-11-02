package com.examp.three.common.DI.modules;


import com.examp.three.presenter.Grievances_admin.contracts.IPendingInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PendingModule {
    IPendingInterface.view view;

    public PendingModule(IPendingInterface.view view) {
        this.view = view;
    }

    @Provides
    @Singleton
    IPendingInterface.view provideIpendingInterfaceView(){
        return view;
    }
}
