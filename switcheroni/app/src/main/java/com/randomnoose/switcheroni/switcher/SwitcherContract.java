package com.randomnoose.switcheroni.switcher;

import android.content.Intent;

import com.randomnoose.switcheroni.BasePresenter;
import com.randomnoose.switcheroni.BaseView;
import com.squareup.picasso.RequestCreator;

import java.io.File;

public interface SwitcherContract {

  interface View extends BaseView<Presenter> {

    void showTakePhoto();

    void updateImageButton(RequestCreator requestCreator);

    void showChangeStyle();

    void updateStyleButton(String styleName);

    void showImageWithNewStyle(RequestCreator requestCreator);
  }

  // TODO [MG]: Change contract methods names - those are stupid and misleading
  interface Presenter extends BasePresenter<View> {

    void takePhoto();

    void loadPhoto();

    void updatePhoto(RequestCreator imageBitmap);

    void changeStyle();

    void updateStyle();

    void swapImageStyle();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void setRawImageFile(File image);
  }
}
