package com.randomnoose.switcheroni.data;

import android.util.Base64;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

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
  public Call<ResponseBody> convert(final File image, final Style style) {
    try {
      final byte[] bytes = FileUtils.readFileToByteArray(image);
      final String imageBase64 = Base64.encodeToString(bytes, Base64.URL_SAFE | Base64.NO_WRAP);
      return service.convert(imageBase64, style.getType());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
