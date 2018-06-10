package com.randomnoose.switcheroni.switcher;

import android.graphics.Bitmap;

import com.randomnoose.switcheroni.BasePresenter;
import com.randomnoose.switcheroni.BaseView;

public interface SwitcherContract {

  interface View extends BaseView<Presenter> {

    void showTakePhoto();

    void updateImageButton(Bitmap imageBitmap);

    void showChangeStyle();

    void updateStyleButton(String styleName);

    void showImageWithNewStyle();
  }

  interface Presenter extends BasePresenter<View> {

    void takePhoto();

    void loadPhoto();

    void updatePhoto(Bitmap imageBitmap);

    void changeStyle();

    void updateStyle();

    void swapImageStyle();
  }
}
