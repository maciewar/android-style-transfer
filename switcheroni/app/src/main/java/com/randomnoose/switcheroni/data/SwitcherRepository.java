package com.randomnoose.switcheroni.data;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.randomnoose.switcheroni.core.AndroidFileUtils;

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

  private final AndroidFileUtils androidFileUtils;
  private StyleChanger service;
  private SwitcherRepositoryCallback callback;

  private Style style;
  private File inputImage;
  private File outputImage;

  @Inject
  public SwitcherRepository(AndroidFileUtils androidFileUtils) {
    this.androidFileUtils = androidFileUtils;
  }

  public void convert() {
    if (inputImage == null || style == null) {
      return;
    }

    new Thread(() -> {
      System.out.println(">>>>>>>>>>>>>>>>> " + Thread.currentThread().getName());
      final Call<ResponseBody> convert = service.convert(inputImage, style);
      System.out.println(">>>>>>>>>>>>>>>>> Sending");
      convert.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
          try {
            final String jsonData = response.body().string();
            final JSONObject jsonObject = new JSONObject(jsonData);
            final String encodedBase64image = (String) jsonObject.get("result");

            final byte[] decodedImage = Base64.decode(encodedBase64image, Base64.URL_SAFE | Base64.NO_WRAP);
            FileUtils.writeByteArrayToFile(getOutputImage(true), decodedImage);

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


  public File getInputImage(boolean createNew) {
    if (inputImage == null || createNew) {
      if (inputImage != null) {
        inputImage.delete();
      }
      try {
        inputImage = androidFileUtils.getTempFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return inputImage;

  }

  public File getInputImage() {
    return getInputImage(false);
  }

  private File getOutputImage(boolean createNew) {
    if (outputImage == null || createNew) {
      if (outputImage != null) {
        outputImage.delete();
      }
      try {
        outputImage = androidFileUtils.getTempFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return outputImage;
  }

  public File getOutputImage() {
    return getOutputImage(false);
  }
}
