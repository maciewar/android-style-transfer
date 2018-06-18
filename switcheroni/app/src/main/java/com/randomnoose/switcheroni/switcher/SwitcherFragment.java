package com.randomnoose.switcheroni.switcher;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.TransitionManager;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.randomnoose.switcheroni.R;
import com.randomnoose.switcheroni.commons.ActivityRequestCodes;
import com.randomnoose.switcheroni.di.ActivityScoped;
import com.randomnoose.switcheroni.styles.StyleActivity;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

@ActivityScoped
public class SwitcherFragment extends DaggerFragment implements SwitcherContract.View {

  private final ConstraintSet defaultLayoutConstrains = new ConstraintSet();
  private final ConstraintSet previewLayoutConstrains = new ConstraintSet();

  private ConstraintLayout constraintLayout;
  private ImageButton btnPicture;
  private Button btnStyle;
  private Button btnRedraw;
  private ImageView imgConvertedImage;

  private SwitcherContract.Presenter presenter;

  @Inject
  public SwitcherFragment() {
  }

  @Override
  public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
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
    btnPicture.setOnClickListener(event -> {
      presenter.takePhoto();
    });
  }

  private void initStyleButtonListener(View root) {
    btnStyle = root.findViewById(R.id.btn_style);
    btnStyle.setOnClickListener(event -> {
      presenter.changeStyle();
    });
  }

  private void initConvertButtonListener(View root) {
    btnRedraw = root.findViewById(R.id.btn_redraw);
    btnRedraw.setOnClickListener(event -> {
      presenter.swapImageStyle();
    });
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
    File image = null;
    try {
      image = getTempFile();
    } catch (IOException e) {
      e.printStackTrace();
    }

    presenter.setRawImageFile(image);
    startTakePictureActivity(image);
  }

  private File getTempFile() throws IOException {
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    String imageFileName = "JPEG_" + timeStamp;
    File storageDir = this.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    return File.createTempFile(imageFileName, ".jpg", storageDir);
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

  @NonNull
  private Bitmap resizeAndCropPreview(Bitmap srcImage, int outputSize, int boxSize) {
    final Bitmap cropped = Bitmap.createBitmap(srcImage, 0, 0, boxSize, boxSize);
    final Bitmap scaled = Bitmap.createScaledBitmap(cropped, outputSize, outputSize, true);
    final Bitmap output = Bitmap.createBitmap(outputSize, outputSize, Bitmap.Config.ARGB_8888);
    final Canvas canvas = new Canvas(output);
    final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setColor(0XFF000000);
    final int temp = outputSize / 2;
    canvas.drawCircle(temp, temp, temp, paint);
    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    canvas.drawBitmap(scaled, 0, 0, paint);
    return output;
  }

  @Override
  public void showChangeStyle() {
    final Intent styleChanger = new Intent(getActivity(), StyleActivity.class);
    startActivityForResult(styleChanger, 2);
  }

  @Override
  public void updateStyleButton(String styleName) {
    btnStyle.setText(styleName);
  }

  @Override
  public void showImageWithNewStyle(RequestCreator requestCreator) {
    requestCreator.fit()
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
