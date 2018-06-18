package com.randomnoose.switcheroni.core;

import com.randomnoose.switcheroni.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

// TODO [MG]: Check references - use WeakRef
// TODO [MG]: Better (a.k.a. smarter or any :D) logging
// TODO [MG]: Add EventBus
public class SwitcheroniApplication extends DaggerApplication {

  @Override
  protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
    return DaggerAppComponent.builder().application(this).build();
  }
}
