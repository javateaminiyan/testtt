package com.examp.three.common.DI.modules;


import com.examp.three.presenter.Grievances_admin.contracts.IReceivedInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ReceivedModule {
    IReceivedInterface.view view;

    public ReceivedModule(IReceivedInterface.view view) {
        this.view = view;
    }
    @Provides
    @Singleton
    IReceivedInterface.view provideIReceivedInterfaceView(){
        return this.view;
    }
}
