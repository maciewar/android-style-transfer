package com.randomnoose.switcheroni.styles;

import com.randomnoose.switcheroni.BasePresenter;
import com.randomnoose.switcheroni.BaseView;

public interface StyleContract {

  interface View extends BaseView<Presenter> {

    void finish(String style);
  }

  interface Presenter extends BasePresenter<View> {

    void chooseStyle(String style);
  }
}
