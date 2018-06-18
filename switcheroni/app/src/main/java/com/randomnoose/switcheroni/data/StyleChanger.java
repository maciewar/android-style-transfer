package com.randomnoose.switcheroni.data;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;

public interface StyleChanger {

  Call<ResponseBody> convert(final File image, final Style style);
}
