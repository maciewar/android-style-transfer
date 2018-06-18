package com.randomnoose.switcheroni.styles;

import com.randomnoose.switcheroni.data.Style;
import com.randomnoose.switcheroni.data.SwitcherRepository;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

public class StylesPresenter implements StyleContract.Presenter {

  private final SwitcherRepository repository;
  private WeakReference<StyleContract.View> view;

  @Inject
  public StylesPresenter(SwitcherRepository repository) {
    this.repository = repository;
  }

  @Override
  public void chooseStyle(String style) {
    repository.setStyle(Style.getByType(style));
    view.get().finish(style);

  }

  @Override
  public void takeView(StyleContract.View view) {
    this.view = new WeakReference<>(view);
  }

  @Override
  public void dropView() {

  }
}
