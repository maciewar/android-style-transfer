package com.randomnoose.switcheroni.styles;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.randomnoose.switcheroni.R;
import com.randomnoose.switcheroni.commons.FragmentUtils;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;
import dagger.android.support.DaggerFragment;

public class StyleActivity extends DaggerAppCompatActivity {

  private Lazy<StyleFragment> styleFragmentProvider;
  private StyleContract.View view;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_style);

    view = initFragment();
  }


  private StyleContract.View initFragment() {
    StyleContract.View styleFragment = (StyleContract.View) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
    if (styleFragment == null) {
      styleFragment = styleFragmentProvider.get();
      FragmentUtils.addFragmentToActivity((DaggerFragment) styleFragment, getSupportFragmentManager(), R.id.contentFrame);
    }

    return styleFragment;
  }

  @SuppressWarnings("unused")
  @Inject
  public void setStyleFragmentProvider(Lazy<StyleFragment> styleFragmentProvider) {
    this.styleFragmentProvider = styleFragmentProvider;
  }
}
