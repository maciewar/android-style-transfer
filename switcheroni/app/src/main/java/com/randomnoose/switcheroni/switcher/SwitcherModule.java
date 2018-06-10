package com.randomnoose.switcheroni.switcher;

import com.randomnoose.switcheroni.di.ActivityScoped;
import com.randomnoose.switcheroni.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SwitcherModule {

  @FragmentScoped
  @ContributesAndroidInjector
  abstract SwitcherFragment switcherFragment();

  @ActivityScoped
  @Binds
  abstract SwitcherContract.Presenter switcherPresenter(SwitcherPresenter switcherPresenter);
}
