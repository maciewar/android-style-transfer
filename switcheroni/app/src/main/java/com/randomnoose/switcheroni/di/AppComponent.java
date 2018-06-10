package com.randomnoose.switcheroni.di;

import android.app.Application;

import com.randomnoose.switcheroni.core.SwitcheroniApplication;
import com.randomnoose.switcheroni.data.DataModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {ApplicationModule.class,
    ActivityBindingModule.class,
    DataModule.class,
    AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<SwitcheroniApplication> {

  @Component.Builder
  interface Builder {

    @BindsInstance
    AppComponent.Builder application(Application application);

    AppComponent build();
  }
}
