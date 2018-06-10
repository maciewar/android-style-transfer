package com.randomnoose.switcheroni.data;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {

  @Singleton
  @Provides
  StyleChanger provideStyleChanger() {
    return new StyleChangerImpl();
  }
}
