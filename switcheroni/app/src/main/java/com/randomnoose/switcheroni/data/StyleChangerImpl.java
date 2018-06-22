package com.randomnoose.switcheroni.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.media.ExifInterface;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.inject.Singleton;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

@Singleton
public class StyleChangerImpl implements StyleChanger {

  private static final int DESIRED_SIZE_PIXELS = 512;

  private StyleChangerService service;

  public StyleChangerImpl() {
    final Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://192.168.43.84:5000/")
        .build();

    service = retrofit.create(StyleChangerService.class);
  }

  @Override
  public Call<ResponseBody> convert(final File image, final Style style) {
    try {
      final Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(image));
      final Bitmap scaledBitmap = scaleImage(bitmap);

      final ExifInterface exif = new ExifInterface(new FileInputStream(image));
      final String orientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
      Bitmap rotatedScaledBitmap = null;
      if (orientation != null) {
        switch (orientation) {
          case "3":
            rotatedScaledBitmap = rotateImage(scaledBitmap, 180);
            break;
          case "6":
            rotatedScaledBitmap = rotateImage(scaledBitmap, 90);
            break;
          case "8":
            rotatedScaledBitmap = rotateImage(scaledBitmap, 270);
            break;
          default:
            rotatedScaledBitmap = scaledBitmap;
        }
      } else {
        rotatedScaledBitmap = scaledBitmap;
      }


      final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      rotatedScaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
      final byte[] bytes = outputStream.toByteArray();

      bitmap.recycle();
      scaledBitmap.recycle();
      rotatedScaledBitmap.recycle();
      final String imageBase64 = Base64.encodeToString(bytes, Base64.URL_SAFE | Base64.NO_WRAP);

      return service.convert(imageBase64, style.getType());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private Bitmap scaleImage(Bitmap bitmap) {
    final int height = bitmap.getHeight();
    final int width = bitmap.getWidth();

    final double scalingFactor = height < width ? (double) DESIRED_SIZE_PIXELS / width : (double) DESIRED_SIZE_PIXELS / height;
    return Bitmap.createScaledBitmap(bitmap, (int) (width * scalingFactor), (int) (height * scalingFactor), true);
  }

  private Bitmap rotateImage(Bitmap scaledBitmap, int angle) {
    final Matrix matrix = new Matrix();
    matrix.postRotate(angle);
    return Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
  }
}
