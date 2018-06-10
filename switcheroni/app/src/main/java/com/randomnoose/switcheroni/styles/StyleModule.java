package com.randomnoose.switcheroni.styles;

import com.randomnoose.switcheroni.di.ActivityScoped;
import com.randomnoose.switcheroni.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class StyleModule {

  @FragmentScoped
  @ContributesAndroidInjector
  abstract StyleFragment styleFragment();

  @ActivityScoped
  @Binds
  abstract StyleContract.Presenter stylePresenter(StylesPresenter stylePresenter);

}
