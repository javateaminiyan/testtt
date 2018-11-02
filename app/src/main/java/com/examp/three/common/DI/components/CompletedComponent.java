package com.examp.three.common.DI.components;



import com.examp.three.common.DI.modules.CompletedModule;
import com.examp.three.common.DI.modules.FilterModule;
import com.examp.three.common.DI.modules.NetworkModule;
import com.examp.three.fragment.Grievances_fragments.Completed;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {CompletedModule.class,NetworkModule.class,FilterModule.class})
@Singleton
public interface CompletedComponent {
    void inject(Completed completed);
}
