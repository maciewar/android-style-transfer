package com.randomnoose.switcheroni.switcher;

import android.graphics.Bitmap;

import com.randomnoose.switcheroni.data.SwitcherRepository;
import com.randomnoose.switcheroni.di.ActivityScoped;

import javax.inject.Inject;

@ActivityScoped
final class SwitcherPresenter implements SwitcherContract.Presenter {

  private SwitcherContract.View view;
  private final SwitcherRepository repository;

  @Inject
  SwitcherPresenter(SwitcherRepository repository) {
    this.repository = repository;
  }


  @Override
  public void takePhoto() {
    view.showTakePhoto();
  }

  @Override
  public void updatePhoto(Bitmap imageBitmap) {
    view.updateImageButton(imageBitmap);
    repository.setImage(imageBitmap);
  }

  @Override
  public void loadPhoto() {

  }

  @Override
  public void changeStyle() {
    view.showChangeStyle();
  }

  @Override
  public void updateStyle() {
    view.updateStyleButton(repository.getStyle().name());

  }

  @Override
  public void swapImageStyle() {
    repository.convert();
  }

  @Override
  public void takeView(SwitcherContract.View view) {
    this.view = view;
  }

  @Override
  public void dropView() {
    view = null;
  }
}
