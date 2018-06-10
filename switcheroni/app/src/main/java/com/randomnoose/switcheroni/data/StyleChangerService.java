package com.randomnoose.switcheroni.data;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface StyleChangerService {

  @POST("/convert")
  @FormUrlEncoded
  Call<ResponseBody> convert(@Field("image") String imageBase64, @Field("style") String style);
}
