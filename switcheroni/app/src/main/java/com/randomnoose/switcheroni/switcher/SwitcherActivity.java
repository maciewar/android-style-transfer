package com.randomnoose.switcheroni.switcher;

import android.os.Bundle;

import com.randomnoose.switcheroni.R;
import com.randomnoose.switcheroni.commons.FragmentUtils;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

public class SwitcherActivity extends DaggerAppCompatActivity {

  private Lazy<SwitcherFragment> switcherFragmentProvider;
  private SwitcherFragment switcherSwitcherFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_switcher);
    switcherSwitcherFragment = initFragment();
  }

  private SwitcherFragment initFragment() {
    SwitcherFragment switcherSwitcherFragment = (SwitcherFragment) getSupportFragmentManager()
        .findFragmentById(R.id.contentFrame);
    if (switcherSwitcherFragment == null) {
      switcherSwitcherFragment = switcherFragmentProvider.get();
      FragmentUtils.addFragmentToActivity(switcherSwitcherFragment, getSupportFragmentManager(), R.id.contentFrame);
    }
    return switcherSwitcherFragment;
  }

  @SuppressWarnings("unused")
  @Inject
  public void setSwitcherFragmentProvider(Lazy<SwitcherFragment> switcherFragmentProvider) {
    this.switcherFragmentProvider = switcherFragmentProvider;
  }
}
