package com.randomnoose.switcheroni;

public interface BasePresenter<T> {

  void takeView(T view);

  void dropView();
}
