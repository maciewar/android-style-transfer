package com.randomnoose.switcheroni.commons;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.squareup.picasso.Transformation;

public class CircleCropTransformation implements Transformation {

  private CircleCropTransformation() {
  }

  @Override
  public Bitmap transform(Bitmap source) {
    final int radius = source.getWidth();
    final Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
    final Canvas canvas = new Canvas(output);
    final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setColor(0XFF000000);
    canvas.drawCircle(radius / 2, radius / 2, radius / 2, paint);
    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    canvas.drawBitmap(source, 0, 0, paint);
    if (output != source) {
      source.recycle();
    }

    return output;
  }

  @Override
  public String key() {
    return "circleCrop()";
  }

  public static CircleCropTransformation getInstance() {
    return InstanceHolder.INSTANCE;
  }

  private static class InstanceHolder {

    private static final CircleCropTransformation INSTANCE = new CircleCropTransformation();

  }
}
