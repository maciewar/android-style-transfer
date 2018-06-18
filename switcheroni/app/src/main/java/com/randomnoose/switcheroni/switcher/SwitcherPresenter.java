package com.randomnoose.switcheroni.switcher;

import android.app.Activity;
import android.content.Intent;

import com.randomnoose.switcheroni.R;
import com.randomnoose.switcheroni.commons.ActivityRequestCodes;
import com.randomnoose.switcheroni.data.SwitcherRepository;
import com.randomnoose.switcheroni.data.SwitcherRepositoryCallback;
import com.randomnoose.switcheroni.di.ActivityScoped;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;

import javax.inject.Inject;

@ActivityScoped
final class SwitcherPresenter implements SwitcherContract.Presenter, SwitcherRepositoryCallback {

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
  public void updatePhoto(RequestCreator requestCreator) {
    view.updateImageButton(requestCreator);
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
    repository.setCallback(this);
    repository.convert();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == ActivityRequestCodes.TAKE_PHOTO_REQUEST && resultCode == Activity.RESULT_OK) {
      final RequestCreator requestCreator = Picasso.get()
          .load(repository.getRawImageFile())
          .resizeDimen(R.dimen.bitmapButtonPreviewSize, R.dimen.bitmapButtonPreviewSize)
          .centerCrop();
      updatePhoto(requestCreator);
    }

    if (requestCode == ActivityRequestCodes.CHOOSE_STYLE_REQUEST && resultCode == Activity.RESULT_OK) {
      updateStyle();
    }
  }

  @Override
  public void setRawImageFile(File image) {
    repository.setRawImageFile(image);
  }

  @Override
  public void takeView(SwitcherContract.View view) {
    this.view = view;
  }

  @Override
  public void dropView() {
    view = null;
  }

  @Override
  public void onConvertSuccess() {
    final RequestCreator requestCreator = Picasso.get()
        .load(repository.getRawImageFile());
    view.showImageWithNewStyle(requestCreator);
  }

  @Override
  public void onConvertFailure() {

  }
}
