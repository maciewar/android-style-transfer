package com.randomnoose.switcheroni.core;

import android.content.Context;
import android.content.Intent;

import com.randomnoose.switcheroni.di.ActivityScoped;
import com.randomnoose.switcheroni.styles.StyleActivity;

import javax.inject.Inject;

@ActivityScoped
final public class ActivityNavigator {

  private Context context;

  public void dopit() {
//    final Intent styleChanger = new Intent(context, StyleActivity.class);
//    startActivityForResult(styleChanger, 2);
  }

  @Inject
  public void setContext(Context context) {
    this.context = context;
  }
}
