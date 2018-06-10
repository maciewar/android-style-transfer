package com.randomnoose.switcheroni.data;

import android.graphics.Bitmap;

import okhttp3.ResponseBody;
import retrofit2.Call;

public interface StyleChanger {

   Call<ResponseBody> convert(final Bitmap image, final Style style);
}
