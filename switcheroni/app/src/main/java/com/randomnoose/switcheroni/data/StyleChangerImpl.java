package com.randomnoose.switcheroni.data;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import javax.inject.Singleton;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

@Singleton
public class StyleChangerImpl implements StyleChanger {

  private StyleChangerService service;

  public StyleChangerImpl() {
    final Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://192.168.0.143:5000/")
        .build();

    service = retrofit.create(StyleChangerService.class);
  }

  @Override
  public Call<ResponseBody> convert(final Bitmap image, final Style style) {
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    image.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
    final byte[] bytes = outputStream.toByteArray();

    final String imageBase64 = Base64.encodeToString(bytes, Base64.URL_SAFE | Base64.NO_WRAP);

    return service.convert(imageBase64, style.getType());
  }
}
