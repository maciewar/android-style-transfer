package com.randomnoose.switcheroni.switcher;

import android.app.Activity;
import android.content.Intent;

import com.randomnoose.switcheroni.R;
import com.randomnoose.switcheroni.commons.ActivityRequestCodes;
import com.randomnoose.switcheroni.commons.CircleCropTransformation;
import com.randomnoose.switcheroni.data.SwitcherRepository;
import com.randomnoose.switcheroni.data.SwitcherRepositoryCallback;
import com.randomnoose.switcheroni.di.ActivityScoped;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

@ActivityScoped
final class SwitcherPresenter implements SwitcherContract.Presenter, SwitcherRepositoryCallback {

  private final SwitcherRepository repository;
  private WeakReference<SwitcherContract.View> view;
  private boolean imgIsSwapping;

  @Inject
  SwitcherPresenter(SwitcherRepository repository) {
    this.repository = repository;
  }

  @Override
  public void takePhoto() {
    view.get().showTakePhoto(repository.getInputImage(true));
  }

  @Override
  public void updatePhoto(RequestCreator requestCreator) {
    requestCreator.transform(CircleCropTransformation.getInstance());
    view.get().updateImageButton(requestCreator);
  }

  @Override
  public void loadPhoto() {

  }

  @Override
  public void changeStyle() {
    view.get().showChangeStyle();
  }

  @Override
  public void updateStyle() {
    view.get().updateStyleButton(repository.getStyle());
  }

  @Override
  public void swapImageStyle() {
    if (imgIsSwapping) {
      return;
    }
    imgIsSwapping = true;
    repository.setCallback(this);
    repository.convert();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == ActivityRequestCodes.TAKE_PHOTO_REQUEST && resultCode == Activity.RESULT_OK) {
      final RequestCreator requestCreator = Picasso.get()
          .load(repository.getInputImage())
          .resizeDimen(R.dimen.bitmapButtonPreviewSize, R.dimen.bitmapButtonPreviewSize)
          .centerCrop();
      updatePhoto(requestCreator);
    }

    if (requestCode == ActivityRequestCodes.CHOOSE_STYLE_REQUEST && resultCode == Activity.RESULT_OK) {
      updateStyle();
    }
  }

  @Override
  public void takeView(SwitcherContract.View view) {
    this.view = new WeakReference<>(view);
  }

  @Override
  public void dropView() {
    view = null;
  }

  @Override
  public void onConvertSuccess() {
    final RequestCreator requestCreator = Picasso.get()
        .load(repository.getOutputImage());
    view.get().showImageWithNewStyle(requestCreator);
    imgIsSwapping = false;
  }

  @Override
  public void onConvertFailure() {
    imgIsSwapping = false;
  }
}
