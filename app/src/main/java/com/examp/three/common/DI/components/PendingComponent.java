package com.examp.three.common.DI.components;



import com.examp.three.common.DI.modules.FilterModule;
import com.examp.three.common.DI.modules.NetworkModule;
import com.examp.three.common.DI.modules.PendingModule;
import com.examp.three.fragment.Grievances_fragments.Pending;


import javax.inject.Singleton;

import dagger.Component;


@Component(modules = {PendingModule.class,NetworkModule.class,FilterModule.class})
@Singleton
public interface PendingComponent {
   // void injectPendingMethod(Pending pending);

    void injectPendingMethod(Pending pending);
}
