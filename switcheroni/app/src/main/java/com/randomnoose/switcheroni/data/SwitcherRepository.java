package com.randomnoose.switcheroni.data;

import android.graphics.Bitmap;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class SwitcherRepository {

  private StyleChanger service;

  private Bitmap image;
  private Style style;

  @Inject
  public SwitcherRepository() {
  }

  public void convert() {
    if (image == null || style == null) {
      return;
    }
    System.out.println("Convert with style: " + style.getType());
    final Call<ResponseBody> convert = service.convert(image, style);
    convert.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        System.out.println("OK");
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        System.out.println("Failure");
        System.out.println(call.request());
        System.out.println(t.getMessage());
      }
    });
  }

  public Bitmap getImage() {
    return image;
  }

  public void setImage(Bitmap image) {
    this.image = image;
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
}
