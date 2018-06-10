package com.randomnoose.switcheroni.styles;

import com.randomnoose.switcheroni.data.Style;
import com.randomnoose.switcheroni.data.SwitcherRepository;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class StylesPresenter implements StyleContract.Presenter {

  private StyleContract.View view;
  private final SwitcherRepository repository;

  @Inject
  public StylesPresenter(SwitcherRepository repository) {
    this.repository = repository;
  }

  @Override
  public void chooseStyle(String style) {
    repository.setStyle(Style.getByType(style));
    view.finish(style);

  }

  @Override
  public void takeView(StyleContract.View view) {
    this.view = view;
  }

  @Override
  public void dropView() {

  }
}
