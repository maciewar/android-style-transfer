package com.randomnoose.switcheroni.data;

import android.support.annotation.NonNull;
import android.util.Base64;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class SwitcherRepository {

  private StyleChanger service;
  private SwitcherRepositoryCallback callback;

  private Style style;
  private File rawImageFile;

  @Inject
  public SwitcherRepository() {
  }

  public void convert() {
    if (rawImageFile == null || style == null) {
      return;
    }

    new Thread(() -> {
      System.out.println(">>>>>>>>>>>>>>>>> " + Thread.currentThread().getName());
      final Call<ResponseBody> convert = service.convert(rawImageFile, style);
      System.out.println(">>>>>>>>>>>>>>>>> Sending");
      convert.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
          try {
            final String jsonData = response.body().string();
            final JSONObject jsonObject = new JSONObject(jsonData);
            final String encodedBase64image = (String) jsonObject.get("result");

            final byte[] decodedImage = Base64.decode(encodedBase64image, Base64.URL_SAFE | Base64.NO_WRAP);
            FileUtils.writeByteArrayToFile(rawImageFile, decodedImage);

            if (callback != null) {
              callback.onConvertSuccess();
            }
          } catch (IOException | JSONException e) {
            // TODO [MG]: Add some nice error box for user
            e.printStackTrace();
            callback.onConvertFailure();
          }
        }

        @Override
        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
          System.out.println("Failure");
          t.printStackTrace();
          System.out.println(call.request());
          System.out.println(t.getMessage());
          if (callback != null) {
            callback.onConvertFailure();
          }
        }
      });
    }).start();
  }

  public Style getStyle() {
    return style;
  }

  public void setStyle(Style style) {
    this.style = style;
  }

  @Inject
  public void setService(StyleChanger service) {
    this.service = service;
  }

  public void setCallback(SwitcherRepositoryCallback callback) {
    this.callback = callback;
  }

  public File getRawImageFile() {
    return rawImageFile;
  }

  public void setRawImageFile(File rawImageFile) {
    this.rawImageFile = rawImageFile;
  }
}
