package com.randomnoose.switcheroni.di;

import com.randomnoose.switcheroni.styles.StyleActivity;
import com.randomnoose.switcheroni.styles.StyleModule;
import com.randomnoose.switcheroni.switcher.SwitcherActivity;
import com.randomnoose.switcheroni.switcher.SwitcherModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

  @ActivityScoped
  @ContributesAndroidInjector(modules = SwitcherModule.class)
  abstract SwitcherActivity switcherActivity();

  @ActivityScoped
  @ContributesAndroidInjector(modules = StyleModule.class)
  abstract StyleActivity styleActivity();
}
