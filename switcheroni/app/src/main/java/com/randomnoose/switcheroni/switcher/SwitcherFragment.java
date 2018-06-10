package com.randomnoose.switcheroni.switcher;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.randomnoose.switcheroni.R;
import com.randomnoose.switcheroni.di.ActivityScoped;
import com.randomnoose.switcheroni.styles.StyleActivity;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

@ActivityScoped
public class SwitcherFragment extends DaggerFragment implements SwitcherContract.View {

  private Button btn_picture;
  private Button btn_style;
  private Button btn_redraw;
  private ImageView img_raw_photo;

  private SwitcherContract.Presenter presenter;

  @Inject
  public SwitcherFragment() {
  }

  @Override
  public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                        Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    android.view.View root = inflater.inflate(R.layout.fragment_switcher, container, false);
    btn_picture = root.findViewById(R.id.btn_picture);
    btn_picture.setOnClickListener(event -> {
      presenter.takePhoto();
    });

    btn_style = root.findViewById(R.id.btn_style);
    btn_style.setOnClickListener(event -> {
      presenter.changeStyle();
    });

    btn_redraw = root.findViewById(R.id.btn_redraw);
    btn_redraw.setOnClickListener(event -> {
      presenter.swapImageStyle();
    });
    img_raw_photo = root.findViewById(R.id.img_raw_photo);
    return root;
  }

  @Override
  public void onResume() {
    super.onResume();
    presenter.takeView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    presenter.dropView();
  }

  @Override
  public void showTakePhoto() {
    startTakePictureActivity();
  }

  private void startTakePictureActivity() {
    Intent photoTaker = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (photoTaker.resolveActivity(getActivity().getPackageManager()) != null) {
      startActivityForResult(photoTaker, 1);
    }
  }

  @Override
  public void updateImageButton(Bitmap imageBitmap) {
    img_raw_photo.setImageBitmap(imageBitmap);
  }

  @Override
  public void showChangeStyle() {
    final Intent styleChanger = new Intent(getActivity(), StyleActivity.class);
    startActivityForResult(styleChanger, 2);
  }

  @Override
  public void updateStyleButton(String styleName) {
    btn_style.setText(styleName);
  }

  @Override
  public void showImageWithNewStyle() {

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
      final Bundle extras = data.getExtras();
      final Bitmap imageBitmap = (Bitmap) extras.get("data");
      presenter.updatePhoto(imageBitmap);
    }

    if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
      final Bundle extras = data.getExtras();
      final String style = extras.getString("style");
      presenter.updateStyle();
    }
  }

  @Inject
  public void setPresenter(SwitcherContract.Presenter presenter) {
    this.presenter = presenter;
  }
}
