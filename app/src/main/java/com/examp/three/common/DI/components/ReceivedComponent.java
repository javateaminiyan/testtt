package com.examp.three.common.DI.components;



import com.examp.three.common.DI.modules.FilterModule;
import com.examp.three.common.DI.modules.NetworkModule;
import com.examp.three.common.DI.modules.ReceivedModule;
import com.examp.three.fragment.Grievances_fragments.Received;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {ReceivedModule.class,NetworkModule.class,FilterModule.class})
@Singleton
public interface ReceivedComponent {
    void injectMethod(Received received);
}
