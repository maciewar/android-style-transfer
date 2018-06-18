package com.randomnoose.switcheroni.switcher;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.TransitionManager;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.randomnoose.switcheroni.R;
import com.randomnoose.switcheroni.commons.ActivityRequestCodes;
import com.randomnoose.switcheroni.data.Style;
import com.randomnoose.switcheroni.di.ActivityScoped;
import com.randomnoose.switcheroni.styles.StyleActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

@ActivityScoped
public class SwitcherFragment extends DaggerFragment implements SwitcherContract.View {

  private final ConstraintSet defaultLayoutConstrains = new ConstraintSet();
  private final ConstraintSet previewLayoutConstrains = new ConstraintSet();

  private ConstraintLayout constraintLayout;
  private ImageButton btnPicture;
  private ImageButton btnStyle;
  private ImageButton btnRedraw;
  private ImageView imgConvertedImage;

  private SwitcherContract.Presenter presenter;

  @Inject
  public SwitcherFragment() {
  }

  @Override
  public android.view.View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                        Bundle savedInstanceState) {
    final android.view.View root = inflater.inflate(R.layout.fragment_switcher, container, false);
    constraintLayout = root.findViewById(R.id.main);
    defaultLayoutConstrains.clone(constraintLayout);
    previewLayoutConstrains.clone(root.getContext(), R.layout.fragment_switcher_alt);
    initImageButtonListener(root);
    initStyleButtonListener(root);
    initConvertButtonListener(root);
    imgConvertedImage = root.findViewById(R.id.img_converted_image);
    return root;
  }

  private void initImageButtonListener(View root) {
    btnPicture = root.findViewById(R.id.btn_picture);
    btnPicture.setOnClickListener(event -> presenter.takePhoto());
  }

  private void initStyleButtonListener(View root) {
    btnStyle = root.findViewById(R.id.btn_style);
    btnStyle.setOnClickListener(event -> presenter.changeStyle());
  }

  private void initConvertButtonListener(View root) {
    btnRedraw = root.findViewById(R.id.btn_redraw);
    btnRedraw.setOnClickListener(event -> presenter.swapImageStyle());
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
  public void showTakePhoto(File resultImage) {
    startTakePictureActivity(resultImage);
  }

  private void startTakePictureActivity(File image) {
    Intent photoTaker = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (photoTaker.resolveActivity(getActivity().getPackageManager()) != null) {
      if (image != null) {
        final Uri imageUri = FileProvider.getUriForFile(this.getContext(), "com.randomnoose.switcheroni", image);
        photoTaker.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(photoTaker, ActivityRequestCodes.TAKE_PHOTO_REQUEST);
      }
    }
  }

  @Override
  public void updateImageButton(RequestCreator requestCreator) {
    requestCreator.into(btnPicture);
  }

  @Override
  public void showChangeStyle() {
    final Intent styleChanger = new Intent(getActivity(), StyleActivity.class);
    startActivityForResult(styleChanger, 2);
  }

  @Override
  public void updateStyleButton(Style style) {
    Picasso.get()
        .load(style.getStyleId())
        .fit()
        .into(btnStyle);
  }

  @Override
  public void showImageWithNewStyle(RequestCreator requestCreator) {
    requestCreator.fit()
        .centerInside()
        .into(imgConvertedImage);
    TransitionManager.beginDelayedTransition(constraintLayout);
    previewLayoutConstrains.applyTo(constraintLayout);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    presenter.onActivityResult(requestCode, resultCode, data);
  }

  @Inject
  public void setPresenter(SwitcherContract.Presenter presenter) {
    this.presenter = presenter;
  }
}
